package ch.hearc.parapa_II;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
	/**
	 * Start a new cancellable future task to run the console reading
	 * 
	 * @param args Program parameters, not used
	 */
	public static void main(String[] args) {
		new Thread(consoleTask).start();
	}

	/**
	 * Task starting the threads and reading the console, cancelled on EXIT or when
	 * all threads are done
	 */
	private static FutureTask<String> consoleTask = new FutureTask<>(new Callable<String>() {
		@Override
		public String call() throws Exception {
			int nbDocuments = 1;
			int nbPersons = 4;

			/*
			 * -----------------------------------------------------------------------------
			 * TODO : Demander a l'utilisateur d'entrer un nombre de documents et un nombre
			 * de personne
			 * 
			 * Remarque : via console ou interface graphique
			 * -----------------------------------------------------------------------------
			 */

			// Database
			Database db = Database.getInstance();
			db.init(nbDocuments);

			// Waiting logger
			WaitingLogger waitingLogger = WaitingLogger.getInstance();

			// Create threads
			ArrayList<Person> persons = generatePopulation(db, nbPersons);

			// Start threads
			ArrayList<Thread> threads = new ArrayList<Thread>();

			// ArrayList<Thread> threads = persons//
			// .stream()//
			// .parallel()//
			// .map(Thread::new)//
			// .peek(Thread::start)//
			// .collect(Collectors.toCollection(ArrayList<Thread>::new));

			for (Person person : persons) {
				Thread thread = new Thread(person);
				thread.setName(person.getName());
				thread.start();

				threads.add(thread);
			}

			// Setup waiting controller
			waitingLogger.assignConsoleFuture(consoleTask, persons);

			while (!consoleTask.isCancelled()) {
				/*
				 * -----------------------------------------------------------------------------
				 * TODO : Lire en continu les inputs de l'utilisateur (prevoir une porte de
				 * sortie sur demande d'arret du programme).
				 * Permettre à l'utilisateur de visualiser la prochaine operation des logs
				 * 
				 * Remarque : Pour l'arret du programme, ne pas oublier d'interrompre tous les
				 * threads en plus de la tache principale.
				 * Pour afficher la prochaine operation du stack, voir la méthode popNextLog du
				 * WaitingLogger
				 * -----------------------------------------------------------------------------
				 */

				// waitingLogger.popNextLog();
				// Get user input
				String input = System.console().readLine();

				if (input.equals("EXIT")) {
					consoleTask.cancel(true);
				}

				if (input.equals("NEXT") || input.equals("N") || input.equals("")) {
					if (!waitingLogger.popNextLog()) {
						System.out.println("No logs available at the moment");
					}
				}
			}

			// Stop all threads
			threads//
					.stream()//
					.parallel()//
					.forEach(Thread::interrupt);

			return "";
		}
	});

	/**
	 * Generate a list of person and assign them a document from the database
	 * 
	 * @param db        Database containing all documents
	 * @param nbPersons Number of persons to generate
	 * @return a list of persons
	 */
	private static ArrayList<Person> generatePopulation(Database db, int nbPersons) {
		ArrayList<Person> persons = new ArrayList<Person>();

		long minStartingTime = 0;
		long maxStartingTime = 5000;
		long minDuration = 1000;
		long maxDuration = 5000;
		double probabilityReader = 0.5f;

		for (int i = 0; i < nbPersons; i++) {
			long startTime = (long) (minStartingTime + Math.random() * (maxStartingTime - minStartingTime));
			long duration = (long) (minDuration + Math.random() * (maxDuration - minDuration));
			Person.Role role = Math.random() < probabilityReader ? Person.Role.READER : Person.Role.WRITER;

			persons.add(new Person("Thread " + (i + 1), db.getRandomDocument(), role, roundTime(startTime),
					roundTime(duration)));
		}

		return persons;
	}

	/**
	 * Round milliseconds to 100
	 * 
	 * @param time Time to round
	 * @return rounded time
	 */
	private static long roundTime(long time) {
		return time - (time % 100);
	}

}
