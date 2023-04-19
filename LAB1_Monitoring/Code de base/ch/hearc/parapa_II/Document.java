package ch.hearc.parapa_II;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Document 
{
	// Concurrent content
	private String content;
	
	// Other variables
	private String name;
	private Color color;

	private ReentrantReadWriteLock reentrantLock;
	
	/**
	 * Constructor
	 * @param name Name of the document
	 */
	public Document(String name, Color color)
	{
		this.name = name;

		this.color = color;

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
	 * Get the reading lock
	 * @return the reading lock
	 */
	public ReentrantReadWriteLock.ReadLock getReadLock()
	{
		return reentrantLock.readLock();
	}

	/**
	 * Get the writing lock
	 * @return the writing lock
	 */
	public ReentrantReadWriteLock.WriteLock getWriteLock()
	{
		return reentrantLock.writeLock();
	}
	
	/**
	 * Get a deep copy of document content, accessed by readers
	 * @return the content of the document
	 */
	public String readContent()
	{
		return new String(content);
	}
	
	/**
	 * Set the document's content, accessed by writers
	 * @param newContent New content of the document
	 */
	public void setContent(String newContent)
	{
		content = newContent;
	}

	/**
	 * Get the color of the document
	 * @return the color of the document
	 */
	public Color getColor()
	{
		return color;
	}
}
