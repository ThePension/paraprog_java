package ch.hearc.parapa_II;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList; // changer en list 
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Database {
	// Singleton lock
	private final static ReentrantLock lockSingleton = new ReentrantLock();
	private static Database instance;

	// https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CopyOnWriteArrayList.html
	private CopyOnWriteArrayList<String> names;
	private CopyOnWriteArrayList<Document> documents;

	/**
	 * Constructor
	 */
	private Database() {
		documents = new CopyOnWriteArrayList<Document>();
		names = new CopyOnWriteArrayList<String>();
	}

	/**
	 * Instance of the database
	 * 
	 * @return the instance of the database
	 */
	public static Database getInstance() {
		lockSingleton.lock();
		if (instance == null) {
			instance = new Database();
		}
		lockSingleton.unlock();

		return instance;
	}

	/**
	 * Initialize the database with the number of documents to generate
	 * 
	 * @param size
	 */
	public void init(int size) {
		// Color manager
		ColorManager colorManager = ColorManager.getInstance();

		for (int i = 0; i < size; i++) {
			String name = "Document " + (i + 1);

			documents.add(new Document(name, colorManager.getRandomColor()));
			names.add(name);
		}
	}

	/**
	 * Return all documents names
	 * 
	 * @return all names
	 */
	public CopyOnWriteArrayList<String> getNames() {
		return names;
	}

	/**
	 * Select a random document in the database
	 * 
	 * @return a random document contained in the database
	 */
	public Document getRandomDocument() {
		if (documents.size() > 0) {
			return documents.get((int) (Math.floor(Math.random() * documents.size())));
		}

		return null;
	}

	/**
	 * Get all documents
	 * 
	 * @return a Set containing all documents
	 */
	public Set<Document> getDocuments() {
		return documents.stream().collect(Collectors.toSet());
	}
}
