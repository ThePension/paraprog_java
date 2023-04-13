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

	private String diagramLog;
	
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

		// Diagram
		diagramLog = this.getNameAndRole() + " : ";
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

			// Faire patienter la personne tant que le temps ecoule ne depasse pas son temps de depart
			while (timePassed() < startingTime)
			{
				// Pause
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// Une fois lance, ajoutez la personne dans la file d'attente d'acces a son document
			waitingLogger.addWaiting(this, this.durationTime);
			
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
				// Tentative de lecture du document
				
				while(timePassed() < durationTime)
				{
					// Pause
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Add the person to the waiting queue
				// waitingLogger.addWaiting(this, this.durationTime);

				doc.getReadLock().lock();

				waitingLogger.removeWaiting(this, this.durationTime);

				doc.readContent();

				doc.getReadLock().unlock();				
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

				// Tentative d'ecriture dans le document
				while(timePassed() < durationTime)
				{
					// Pause
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				doc.getWriteLock().lock();

				// Remove the person from the waiting queue
				waitingLogger.removeWaiting(this, this.durationTime);

				doc.setContent(name);

				doc.getWriteLock().unlock();
			}
			
			// Remove the person from the process queue
			waitingLogger.finished(this, durationTime);

		}
		//catch (InterruptedException e) {}     <- a documenter quand necessaire (gestion de l'interruption du programme)
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

	@Override
	public String toString()
	{
		return " - " + name + " (" + role + ") start : " + startingTime / 1000l + " / duration : " + durationTime / 1000l + " (" + doc.getName() + ")" ;
	}

	public void display()
	{
		System.out.println(this);
	}

	public String getNameAndRole()
	{
		return name + " (" + role.toString().charAt(0) + ")";
	}

	public void updateDiagram(String nextpart){
		diagramLog += nextpart;
	}

	public String getDiagramLog()
	{
		return diagramLog;
	}
}
