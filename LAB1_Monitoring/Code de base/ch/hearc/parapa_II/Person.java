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

	private long timeSpentInWaiting;
	private long timeSpendInProcessing;

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
		try
		{
			// Faire patienter la personne tant que le temps ecoule ne depasse pas son temps
			// de depart
			this.sleep(this.startingTime);

			// Une fois lance, ajoutez la personne dans la file d'attente d'acces a son
			// document
			waitingLogger.addWaiting(this, this.durationTime);

			long startWaitingTime = timer.timePassed();
			long startProcessingTime = 0;

			if (role == Role.READER) {
				// Tentative de lecture du document
				doc.getReadLock().lock();

				// Get the spent time in waiting
				timeSpentInWaiting = timer.timePassed() - startWaitingTime;

				waitingLogger.removeWaiting(this, timeSpentInWaiting);

				startProcessingTime = timer.timePassed();

				doc.readContent();

				this.sleep(this.durationTime);

				doc.getReadLock().unlock();
			} else {
				// Tentative d'ecriture dans le document
				doc.getWriteLock().lock();

				timeSpentInWaiting = timer.timePassed() - startWaitingTime;

				// Remove the person from the waiting queue
				waitingLogger.removeWaiting(this, timeSpentInWaiting);

				startProcessingTime = timer.timePassed();

				doc.setContent(name);

				this.sleep(this.durationTime);

				doc.getWriteLock().unlock();
			}

			// Get the spent time in processing
			timeSpendInProcessing = timer.timePassed() - startProcessingTime;

			// Remove the person from the process queue
			waitingLogger.finished(this, timeSpendInProcessing);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return;
		}
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

	/**
	 * Get the name of the person
	 * @return the name of the person
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the role of the person
	 * @return the role of the person
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Get the document treated by the person
	 * @return the document treated by the person
	 */
	public Document getDocument() {
		return doc;
	}

	/**
	 * Get the starting time of the person
	 * @return the starting time of the person
	 */
	public long getStartingTime() {
		return startingTime;
	}

	/**
	 * Get the duration time of the person
	 * @return the duration time of the person
	 */
	public long getDurationTime() {
		return durationTime;
	}

	/**
	 * Get the time spent in waiting
	 * @return the time spent in waiting
	 */
	public long getTimeSpentInWaiting()
	{
		return timeSpentInWaiting;
	}

	/**
	 * Get the time spent in processing
	 * @return the time spent in processing
	 */
	public long getTimeSpentInProcessing()
	{
		return timeSpendInProcessing;
	}

	@Override
	public String toString() {
		return " - " + name + " (" + role + ") start : " + startingTime + " / duration : "
				+ durationTime + " (" + doc.getColor().getColoredText(doc.getName()) + ")";
	}

	/**
	 * Get the name and the role of the person
	 * @return the name and the role of the person
	 */
	public String getNameAndRole() {
		return name + " (" + role.toString().charAt(0) + ")";
	}

	/**
	 * Get the diagram log
	 * @return the diagram log
	 */
	public void updateDiagram(String nextpart) {
		diagramLog += nextpart;
	}

	/**
	 * Get the diagram log
	 * @return the diagram log
	 */
	public String getDiagramLog() {
		return this.doc.getColor().getColoredText(diagramLog);
	}
}
