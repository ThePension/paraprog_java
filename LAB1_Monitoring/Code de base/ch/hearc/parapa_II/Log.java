package ch.hearc.parapa_II;

public class Log 
{
	public enum Type { WAITING, REMOVE, FINISHED }
	
	private Type type;
	private Person person;
	
	/**
	 * Constructor
	 * @param type Type of the log
	 * @param person Person logged
	 */
	public Log(Type type, Person person)
	{
		this.type = type;
		this.person = person;
	}
	
	// Getters
	
	public Type getType()
	{
		return type;
	}
	
	public Person getPerson()
	{
		return person;
	}
}
