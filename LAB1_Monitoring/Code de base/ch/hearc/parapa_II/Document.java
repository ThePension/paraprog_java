package ch.hearc.parapa_II;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Document 
{
	/*
	 * -----------------------------------------------------------------------------------
	 * TODO : Ajouter les locks au documents afin d'assurer un acces concurrent à celui-ci
	 * 
	 * Remarque : java.util.concurrent contient tout ce qu'il faut
	 * -----------------------------------------------------------------------------------
	 */
	
	// Concurrent content
	private String content;
	
	// Other variables
	private String name;

	private ReentrantReadWriteLock reentrantLock;
	
	/**
	 * Constructor
	 * @param name Name of the document
	 */
	public Document(String name)
	{
		this.name = name;

		this.reentrantLock = new ReentrantReadWriteLock();
		
		content = "No data";
	}
	
	/**
	 * Get document name
	 * @return the name of the document
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get document content, accessed by readers
	 * @return the content of the document
	 */
	public String readContent()
	{
		// Protect the content
		reentrantLock.readLock().lock();

		// Deep copy the content
		String content = new String(this.content);

		// Release the lock
		reentrantLock.readLock().unlock();

		return content;
	}
	
	/**
	 * Set the document's content, accessed by writers
	 * @param newContent New content of the document
	 */
	public void setContent(String newContent)
	{
		// Protect the content
		reentrantLock.writeLock().lock();

		content = newContent;

		// Release the lock
		reentrantLock.writeLock().unlock();
	}
}
