
package ch.hearc.paraprog.labo.performances;

import java.util.concurrent.Callable;

public class Producer implements Callable<Integer>
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Producer(Buffer buffer, int totalAccess)
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
				buffer.push(produceData());
				}
			catch (InterruptedException e)
				{
				System.err.println("ERROR during producing data !");
				}

			nbAccess++;
			}

		return totalAccess;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private String produceData()
		{
		return DATA;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private int nbAccess;
	private int totalAccess;
	private Buffer buffer;

	// Tools
	private static final String DATA = "data";
	}
