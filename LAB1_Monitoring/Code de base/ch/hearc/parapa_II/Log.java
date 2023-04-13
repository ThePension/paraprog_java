package ch.hearc.parapa_II;

public class Log 
{
	public enum Type { WAITING, REMOVE, FINISHED }
	
	private Type type;
	private Person person;
	private long elapsedTime;
	
	/**
	 * Constructor
	 * @param type Type of the log
	 * @param person Person logged
	 */
	public Log(Type type, Person person, long elapsedTime)
	{
		this.type = type;
		this.person = person;
		this.elapsedTime = elapsedTime;
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

	public long getElapsedTime()
	{
		return elapsedTime;
	}

	@Override
	public String toString() 
	{
		return "Log [type=" + type + ", personName=" + person.getName() + ", personRole=" + person.getRole() + "]";
	}
}
