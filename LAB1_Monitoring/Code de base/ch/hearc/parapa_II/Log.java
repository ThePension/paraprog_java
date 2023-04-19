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
	
	/**
	 * Get the type of the log
	 * @return the type of the log
	 */
	public Type getType()
	{
		return type;
	}
	
	/**
	 * Get the person logged
	 * @return the person logged
	 */
	public Person getPerson()
	{
		return person;
	}

	/**
	 * Get the elapsed time of the operation
	 * @return the elapsed time of the operation
	 */
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
