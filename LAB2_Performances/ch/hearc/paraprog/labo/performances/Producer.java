
package ch.hearc.paraprog.labo.performances;

public class Producer implements Runnable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Producer(String name, Buffer buffer)
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
				buffer.push(produceData());
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

	private String produceData()
		{
		System.out.println("Producer (" + name + ") : " + DATA);
		return DATA;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private String name;
	private Buffer buffer;

	// Tools
	private static final String DATA = "data";
	}
