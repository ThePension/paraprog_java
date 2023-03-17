package ch.hearc.parapa_II;

public class Person implements Runnable
{
	public static enum Role { READER, WRITER }
	
	private String name;
	private Document doc;
	private Role role;
	private long startingTime;
	private long durationTime;
	
	private long startPause;
	private long timePaused;
	private boolean paused;
	
	private WaitingLogger waitingLogger;
	private Timer timer;
	
	/**
	 * Constructor
	 * @param name Name of the person
	 * @param doc Document treated by the person
	 * @param role Role defining if the person is a reader or a writer
	 * @param startingTime Time when the person tries to access his document
	 * @param durationTime Operation duration
	 */
	public Person(String name, Document doc, Role role, long startingTime, long durationTime)
	{
		// Variables
		this.name = name;
		this.doc = doc;
		this.role = role;
		this.startingTime = startingTime;
		this.durationTime = durationTime;
		
		// Helpers
		waitingLogger = WaitingLogger.getInstance();
		timer = Timer.getInstance();
	}

	/**
	 * Runnable content
	 */
	@Override
	public void run() 
	{
		//try    <- docummentez quand necessaire
		{
			/*
			 * -----------------------------------------------------------------------------------------------
			 * TODO : Faire patienter la personne tant que le temps écoule ne depasse pas son temps de depart.
			 *        Une fois lance, ajoutez la personne dans la file d'attente d'acces à son document
			 * 
			 * Remarque : addWaiting du WaitingLogger
			 * -----------------------------------------------------------------------------------------------
			 */
			
			if (role == Role.READER)
			{
				/*
				 * ------------------------------------------------------------------------------------------------------------------------------------
				 * TODO : Tentative de lecture du document.
				 * 
				 * Remarque : - Penser à faire dormir le thread quand il a acces au document (durationTime)
				 *            - Utiliser les locks du document
				 *            - Penser au fait que le programme doit pouvoir s'arreter a tout moment (ainsi que tous les threads lecteurs / redacteurs)
				 *            - Le contenu lu dans le document ne doit pas necessairement �tre traite, seul l'operation de lecture importe
				 * ------------------------------------------------------------------------------------------------------------------------------------
				 */
			}
			else
			{
				/*
				 * ------------------------------------------------------------------------------------------------------------------------------------
				 * TODO : Tentative d'ecriture dans le document.
				 * 
				 * Remarque : - Penser a faire dormir le thread quand il a acces au document (durationTime)
				 *            - Utiliser les locks du document
				 *            - Penser au fait que le programme doit pouvoir s'arreter a tout moment (ainsi que tous les threads lecteurs / redacteurs)
				 *            - Le nouveau contenu du document importe peu, seule l'acces a l'ecriture du document importe
				 * ------------------------------------------------------------------------------------------------------------------------------------
				 */
			}
			
		}
		//catch (InterruptedException e) {}     <- a docummenter quand necessaire (gestion de l'interruption du programme)
	}
	
	
	
	/**
	 * Compute time passed in this particular runnable
	 * @return the time passed in this runnable
	 */
	public long timePassed()
	{
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - timer.startTime;
		long timeInPause = currentTime - startPause;
		
		if (paused)
		{
			return timePassed - timePaused - timeInPause;
		}
		else
		{
			return timePassed - timePaused;
		}
	}
	

	// Getters
	
	public String getName()
	{
		return name;
	}
	
	public Role getRole()
	{
		return role;
	}
	
	public Document getDocument()
	{
		return doc;
	}
	
	public long getStartingTime()
	{
		return startingTime;
	}
	
	public long getDurationTime()
	{
		return durationTime;
	}
}
