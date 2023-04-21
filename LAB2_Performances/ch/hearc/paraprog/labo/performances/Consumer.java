
package ch.hearc.paraprog.labo.performances;

public class Consumer implements Runnable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Consumer(String name, Buffer buffer)
		{
		this.name = name;
		this.buffer = buffer;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public void run()
		{
		while(true)
			{
			try
				{
				processData(buffer.take());
				}
			catch (InterruptedException e)
				{
				// TODO : improve
				System.err.println("ERROR !");
				}
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void processData(String data)
		{
		System.out.println("Consumer (" + name + ") : " + data);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private String name;
	private Buffer buffer;
	}
