package ch.hearc.parapa_II;

import java.util.concurrent.locks.ReentrantLock;

public class Timer 
{
	public final long startTime = System.currentTimeMillis();
	
	// Singleton lock
	private final static ReentrantLock lockSingleton = new ReentrantLock();
	private static Timer instance;
	
	/**
	 * Instance of the timer
	 * @return
	 */
	public static Timer getInstance()
	{
		lockSingleton.lock();
		if (instance == null)
		{
			instance = new Timer();
		}
		lockSingleton.unlock();
		
		return instance;
	}
	
	/**
	 * Compute the time passed in milliseconds
	 * @return time passed
	 */
	public long timePassed()
	{
		return System.currentTimeMillis() - startTime;
	}
}
