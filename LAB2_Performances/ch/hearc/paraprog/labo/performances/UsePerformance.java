
package ch.hearc.paraprog.labo.performances;

import java.util.stream.IntStream;

public class UsePerformance
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static void main(String[] args)
		{
		main();
		}

	public static void main()
		{
		int nbProducer = 20;
		int nbConsumer = 20;
		int bufferSize = 10;

		startSimulation(nbProducer, nbConsumer, bufferSize);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private static void startSimulation(int nbProducer, int nbConsumer, int bufferSize)
		{
		Buffer buffer = new Buffer(bufferSize);

		IntStream//
				.range(0, nbConsumer)//
				.mapToObj(i -> new Consumer("C" + i, buffer))//
				.map(Thread::new)//
				.forEach(Thread::start);

		IntStream//
				.range(0, nbProducer)//
				.mapToObj(i -> new Producer("P" + i, buffer))//
				.map(Thread::new)//
				.forEach(Thread::start);
		}
	}
