package ch.hearc.parapa_II;

public class Person implements Runnable {
	public static enum Role {
		READER, WRITER
	}

	private String name;
	private Document doc;
	private Role role;
	private long startingTime;
	private long durationTime;

	private WaitingLogger waitingLogger;
	private Timer timer;

	private String diagramLog;

	/**
	 * Constructor
	 * 
	 * @param name         Name of the person
	 * @param doc          Document treated by the person
	 * @param role         Role defining if the person is a reader or a writer
	 * @param startingTime Time when the person tries to access his document
	 * @param durationTime Operation duration
	 */
	public Person(String name, Document doc, Role role, long startingTime, long durationTime) {
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
	public void run() {
		// try <- docummentez quand necessaire
		{
			// Faire patienter la personne tant que le temps ecoule ne depasse pas son temps
			// de depart
			this.sleep(this.startingTime);


			// Une fois lance, ajoutez la personne dans la file d'attente d'acces a son
			// document
			waitingLogger.addWaiting(this, this.durationTime);

			if (role == Role.READER) {
				/*
				 * -----------------------------------------------------------------------------
				 * -------------------------------------------------------
				 * TODO : Tentative de lecture du document.
				 * 
				 * Remarque : - Penser à faire dormir le thread quand il a acces au document
				 * (durationTime)
				 * - Utiliser les locks du document
				 * - Penser au fait que le programme doit pouvoir s'arreter a tout moment (ainsi
				 * que tous les threads lecteurs / redacteurs)
				 * - Le contenu lu dans le document ne doit pas necessairement �tre traite, seul
				 * l'operation de lecture importe
				 * -----------------------------------------------------------------------------
				 * -------------------------------------------------------
				 */

				// Tentative de lecture du document
				doc.getReadLock().lock();

				waitingLogger.removeWaiting(this, this.timePassed());

				doc.readContent();

				this.sleep(this.durationTime);

				doc.getReadLock().unlock();
			} else {
				/*
				 * -----------------------------------------------------------------------------
				 * TODO : Tentative d'ecriture dans le document.
				 * 
				 * Remarque : - Penser a faire dormir le thread quand il a acces au document
				 * (durationTime)
				 * - Utiliser les locks du document
				 * - Penser au fait que le programme doit pouvoir s'arreter a tout moment (ainsi
				 * que tous les threads lecteurs / redacteurs)
				 * - Le nouveau contenu du document importe peu, seule l'acces a l'ecriture du
				 * document importe
				 * -----------------------------------------------------------------------------
				 */

				// Tentative d'ecriture dans le document
				doc.getWriteLock().lock();

				// Remove the person from the waiting queue
				waitingLogger.removeWaiting(this, this.timePassed());

				doc.setContent(name);

				this.sleep(this.durationTime);

				doc.getWriteLock().unlock();
			}

			// Remove the person from the process queue
			waitingLogger.finished(this, timePassed());

		}
		// catch (InterruptedException e) {} <- a documenter quand necessaire (gestion
		// de l'interruption du programme)
	}

	/**
	 * Compute time passed in this particular runnable
	 * 
	 * @return the time passed in this runnable
	 */
	public long timePassed() {
		return System.currentTimeMillis() - timer.startTime;
	}

	private void sleep(long timeMs) {
		// Pause
		try {
			Thread.sleep(timeMs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Getters

	public String getName() {
		return name;
	}

	public Role getRole() {
		return role;
	}

	public Document getDocument() {
		return doc;
	}

	public long getStartingTime() {
		return startingTime;
	}

	public long getDurationTime() {
		return durationTime;
	}

	@Override
	public String toString() {
		return " - " + name + " (" + role + ") start : " + startingTime + " / duration : "
				+ durationTime + " (" + doc.getName() + ")";
	}

	public String getNameAndRole() {
		return name + " (" + role.toString().charAt(0) + ")";
	}

	public void updateDiagram(String nextpart) {
		diagramLog += nextpart;
	}

	public String getDiagramLog() {
		return diagramLog;
	}
}
