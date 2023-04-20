package ch.hearc.parapa_II;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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
			int nbDocuments = GetUserIntegerInput("Insert number of concurrent documents (max 9) : ", 1, 9);
			int nbPersons = GetUserIntegerInput("Insert number of readers / writers (max 9) : ", 1, 9);

			boolean random = GetUserIntegerInput("Would you like to use random parameters (the type of person) ? (1 - Yes, 2 - No) : ", 1, 2) == 1;

			// Database
			Database db = Database.getInstance();
			db.init(nbDocuments);

			// Waiting logger
			WaitingLogger waitingLogger = WaitingLogger.getInstance();

			// Create threads
			ArrayList<Person> persons = generatePopulation(db, nbPersons, random);

			// Start threads
			ArrayList<Thread> threads = new ArrayList<Thread>();

			for (Person person : persons) {
				Thread thread = new Thread(person);
				thread.setName(person.getName());
				thread.start();

				threads.add(thread);
			}

			// Setup waiting controller
			waitingLogger.assignConsoleFuture(consoleTask, persons);

			while (!consoleTask.isCancelled()) {

				System.out.println("\n> Type EXIT to stop the program, NEXT or N to see the next log <\n");

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
	 * @param random    If true, the starting time and duration will be random, otherwise parameters will be asked to the user
	 * @return a list of persons
	 */
	private static ArrayList<Person> generatePopulation(Database db, int nbPersons, boolean random) {
		ArrayList<Person> persons = new ArrayList<Person>();

		long minStartingTime = 0;
		long maxStartingTime = 2000;
		long minDuration = 1000;
		long maxDuration = 3000;
		double probabilityReader = 0.5f;

		for (int i = 0; i < nbPersons; i++) {
			long startTime = (long) (minStartingTime + Math.random() * (maxStartingTime - minStartingTime));
			long duration = (long) (minDuration + Math.random() * (maxDuration - minDuration));
			
			Person.Role role = null;

			if (random) role = Math.random() < probabilityReader ? Person.Role.READER : Person.Role.WRITER;
			else 
			{
				System.out.println("Person " + (i + 1) + " :");
				System.out.println("1 - Reader");
				System.out.println("2 - Writer");
				int roleInput = GetUserIntegerInput("Choose role : ", 1, 2);
				role = roleInput == 1 ? Person.Role.READER : Person.Role.WRITER;
			}

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

	private static int GetUserIntegerInput(String message, int min, int max) {
		int input = 0;
		boolean validInput = false;

		while (!validInput) {
			System.out.print(message);
			try {
				input = Integer.parseInt(System.console().readLine());
				if (input >= min && input <= max) {
					validInput = true;
				} else {
					System.out.println("Invalid input, please enter a number between " + min + " and " + max);
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input, please enter a number between " + min + " and " + max);
			}
		}

		return input;
	}
}
