package ch.hearc.parapa_II;

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
	
	/**
	 * Constructor
	 * @param name Name of the document
	 */
	public Document(String name)
	{
		this.name = name;
		
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
		return content;
	}
	
	/**
	 * Set the document's content, accessed by writers
	 * @param newContent New content of the document
	 */
	public void setContent(String newContent)
	{
		content = newContent;
	}
}
