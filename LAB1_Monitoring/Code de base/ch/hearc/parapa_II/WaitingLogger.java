package ch.hearc.parapa_II;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class WaitingLogger {
	// Singleton lock
	private final static ReentrantLock lockSingleton = new ReentrantLock();
	private static WaitingLogger instance;
	private static final String DIAGRAM_SEPARATOR_CHAR = "-";

	// Log storage
	private BlockingQueue<Log> logs;

	private BlockingQueue<Person> waitingLists;
	private BlockingQueue<Person> processingLists;
	private BlockingQueue<Person> finishedLists;

	// Variables
	private ArrayList<Person> persons;
	private Set<Document> documents;

	// Singletons
	private Database db;

	private FutureTask<String> consoleFuture;

	private int stepPerSecDiagram;

	/**
	 * Constructor
	 */
	private WaitingLogger() {
		db = Database.getInstance();

		// Initialiser les structures de donnees
		logs = new LinkedBlockingQueue<>();
		waitingLists = new LinkedBlockingQueue<>();
		processingLists = new LinkedBlockingQueue<>();
		finishedLists = new LinkedBlockingQueue<>();

		documents = db.getDocuments();
		stepPerSecDiagram = 10;
	}

	/**
	 * Singleton instance
	 * 
	 * @return the instance of the waiting logger
	 */
	public static WaitingLogger getInstance() {
		lockSingleton.lock();
		if (instance == null) {
			instance = new WaitingLogger();
		}
		lockSingleton.unlock();

		return instance;
	}

	/**
	 * Assign the future task to be able to cancel it when all threads are done
	 * 
	 * @param consoleFuture Future task running the main thread
	 * @param persons       List of persons generated by the main thread
	 */
	public void assignConsoleFuture(FutureTask<String> consoleFuture, ArrayList<Person> persons) {
		this.consoleFuture = consoleFuture;
		this.persons = persons;
	}

	/**
	 * Add a thread to the waiting queue
	 * 
	 * @param p     Person waiting to access a document
	 * @param timer Time of the operation
	 */
	public void addWaiting(Person p, long timer) {
		logs.add(new Log(Log.Type.WAITING, p, timer));
	}

	/**
	 * Remove a thread from the waiting queue and add it to the processing queue
	 * 
	 * @param p     Person accessing the document
	 * @param timer Time of the operation
	 */
	public void removeWaiting(Person p, long timer) {
		logs.add(new Log(Log.Type.REMOVE, p, timer));
	}

	/**
	 * Remove a thread from the processing queue
	 * 
	 * @param p     Person finishing to access the document
	 * @param timer Time of the operation
	 */
	public void finished(Person p, long timer) {
		logs.add(new Log(Log.Type.FINISHED, p, timer));
	}

	/**
	 * Called by the user on typing 'NEXT', display the next operation logged
	 */
	public boolean popNextLog() {
		if (logs.size() == 0)
			return false;

		Log nextLog = logs.poll();

		Person p = nextLog.getPerson();

		// Treat log type
		switch (nextLog.getType()) {
			case WAITING:
				try {
					waitingLists.put(p);

					// Calculate the space gap based on the starting time
					int spaceGap = getSpaceGap(p.getStartingTime(), p.getDiagramLog());

					// Update the diagram with " " * spaceGap + "W"
					p.updateDiagram(" ".repeat(spaceGap) + "W");
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return false;
				}

				break;
			case REMOVE:
				try {
					// Remove the person from the waiting list
					waitingLists.remove(p);

					processingLists.put(p);

					// Calculate the space gap based on the starting time
					int spaceGap = getSpaceGap(nextLog.getElapsedTime(), p.getDiagramLog()) + 1;

					// Update the diagram with " " * spaceGap + "W"
					p.updateDiagram(" ".repeat(spaceGap) + "T");
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return false;
				}

				break;
			case FINISHED:
				try {
					processingLists.remove(p);

					finishedLists.put(p);

					// Calculate the space gap based on the starting time
					int spaceGap = getSpaceGap(p.getDurationTime(), p.getDiagramLog());

					if (spaceGap > 0)
						spaceGap--;

					// Update the diagram with "-" * spaceGap + "W"
					p.updateDiagram(DIAGRAM_SEPARATOR_CHAR.repeat(spaceGap) + "F");
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return false;
				}

				break;
			default:
				break;
		}

		System.out.println("-- Threads list -------------------------------------\n");

		// Display all the threads (persons)
		persons.stream().map(Person::toString).forEach(System.out::println);

		System.out.println("\n-- Queues state -------------------------------------");

		// Foreach document, display the waiting and processing persons
		documents.forEach(createDisplayDocumentQueueStateConsumer());

		System.out.println("\n-- Diagram ---------------------------------------------\n");

		System.out.println("W : Start waiting for doc access / T : Stop waiting + Start processing / F : Stop processing, finished\n");

		// Get the longest diagram length
		int longestDiagramLength = persons.stream().parallel().mapToInt(p_ -> p_.getDiagramLog().length()).max().getAsInt();

		// Display graduation
		System.out.println(this.getDiagramGraduatedAxis(15, longestDiagramLength / stepPerSecDiagram));

		persons.stream().map(Person::getDiagramLog).forEach(System.out::println);

		/*
		 * -----------------------------------------------------------------------------
		 * TODO : Controller si il s'agit du dernier log, arreter le programme si c'est
		 * le cas
		 * 
		 * Remarque : interrompre consoleFuture
		 * (2 conditions doivent etre reunies : logs == 0 et nombre de threads termines
		 * correspondant aux nombre total de personnes)
		 * -----------------------------------------------------------------------------
		 */
		if (logs.size() == 0 && finishedLists.size() == persons.size()) {
			consoleFuture.cancel(true);
		}

		return true;
	}

	/**
	 * Create a consumer to display the waiting and processing persons for a
	 * document
	 * 
	 * @return Consumer<Document> to display the waiting and processing persons for
	 *         a document
	 */
	private Consumer<Document> createDisplayDocumentQueueStateConsumer() {
		return document -> {
			// Get all the waiting persons for the document
			ArrayList<Person> waitingPersons2 = waitingLists//
					.stream()//
					.parallel()
					.filter(person -> person.getDocument().equals(document))
					.collect(Collectors.toCollection(ArrayList::new));

			// Get all the processing persons for the document
			ArrayList<Person> processingPersons2 = processingLists//
					.stream()//
					.parallel()
					.filter(person -> person.getDocument().equals(document))
					.collect(Collectors.toCollection(ArrayList::new));

			// Get all the finished persons for the document
			ArrayList<Person> finishedPersons2 = finishedLists//
					.stream()//
					.parallel()
					.filter(person -> person.getDocument().equals(document))
					.collect(Collectors.toCollection(ArrayList::new));

			// Display the waiting and processing persons for the document
			System.out.println("\n" + document.getColor().getColoredText(document.getName()) + " (WAITING): "
					+ waitingPersons2.stream().map(Person::getNameAndRole).collect(Collectors.joining(", ")));
			System.out.println(document.getColor().getColoredText(document.getName()) + " (PROCESSING): "
					+ processingPersons2.stream().map(Person::getNameAndRole).collect(Collectors.joining(", ")));
			System.out.println(document.getColor().getColoredText(document.getName()) + " (FINISHED): "
					+ finishedPersons2.stream().map(Person::getNameAndRole).collect(Collectors.joining(", ")));
		};
	}

	/**
	 * Calculate the space gap based on the time
	 * 
	 * @param time in milliseconds
	 * @return the space gap
	 */
	private int getSpaceGap(long time, String diagram) {
		double timeSec = time / 1000.0;

		double gap = timeSec * (double) stepPerSecDiagram;

		// Round the gap to the nearest stepPerSecDiagram
		// If the gap is 3.7 and stepPerSecDiagram is 2, the timeSecRounded will be 4
		double roundedGap = Math.round(gap / (double) stepPerSecDiagram) * stepPerSecDiagram;

		// Add the left gap based on the number of seconds display in the diagram
		double additionalGap = roundedGap / (double) stepPerSecDiagram;

		gap += additionalGap;

		return (int) Math.ceil(gap);
	}

	/**
	 * Get the diagram graduated axis
	 * 
	 * @param leftGap the left gap
	 * @param nbSec   the number of seconds
	 * @return the diagram graduated axis as a string
	 */
	private String getDiagramGraduatedAxis(int leftGap, int nbSec) {
		StringBuilder sb = new StringBuilder();

		// Add the left gap
		sb.append(" ".repeat(leftGap));

		LongStream.range(0, nbSec).forEach(i -> {
			sb.append(i);

			sb.append(" ".repeat(stepPerSecDiagram));
		});

		return sb.toString();
	}
}
