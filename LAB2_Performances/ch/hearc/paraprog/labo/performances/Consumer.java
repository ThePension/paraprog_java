
package ch.hearc.paraprog.labo.performances;

import java.util.concurrent.Callable;

public class Consumer implements Callable<Integer>
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Consumer(Buffer buffer, int totalAccess)
		{
		this.buffer = buffer;
		this.totalAccess = totalAccess;
		this.nbAccess = 0;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public Integer call()
		{
		while(nbAccess < totalAccess)
			{
			try
				{
				processData(buffer.take());
				}
			catch (InterruptedException e)
				{
				System.err.println("ERROR during consumming data !");
				}

			nbAccess++;
			}

		return totalAccess;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void processData(String data)
		{
		// Nothing
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private int nbAccess;
	private int totalAccess;
	private Buffer buffer;
	}
