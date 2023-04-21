
package ch.hearc.paraprog.labo.performances;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Buffer
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Buffer(int size)
		{
		this.size = size;

		this.buffer = new ArrayBlockingQueue<String>(size, true);
		}

	public Buffer()
		{
		this(DEFAULT_SIZE);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void push(String data) throws InterruptedException
		{
		this.buffer.put(data);
		}

	public String take() throws InterruptedException
		{
		return this.buffer.take();
		}

	/*------------------------------*\
	|*			   Get				*|
	\*------------------------------*/

	public int getSize()
		{
		return this.size;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	// Nothing

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private int size;

	// Tools
	private BlockingQueue<String> buffer;

	private static final int DEFAULT_SIZE = 100;
	}
