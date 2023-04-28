
package ch.hearc.paraprog.labo.performances;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
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
		try
			{
			// IMPORTANT : Le produit (nbProducer*nbWriteAccess) == (nbConsumer*nbReadAccess)

			System.out.println("\n\nSimulation 1 : nbProducer=500, nbConsumer=500, bufferSize=10, nbReadAccess=1000, nbWriteAccess=1000");
			System.out.println("================================================================================================");
			startSimulation(500, 500, 10, 1000, 1000);

			System.out.println("\n\nSimulation 2 : nbProducer=500, nbConsumer=500, bufferSize=50, nbReadAccess=1000, nbWriteAccess=1000");
			System.out.println("================================================================================================");
			startSimulation(500, 500, 50, 1000, 1000);

			System.out.println("\n\nSimulation 3 : nbProducer=500, nbConsumer=500, bufferSize=100, nbReadAccess=1000, nbWriteAccess=1000");
			System.out.println("================================================================================================");
			startSimulation(500, 500, 100, 1000, 1000);

			System.out.println("\n\nSimulation 4 : nbProducer=500, nbConsumer=500, bufferSize=1000, nbReadAccess=1000, nbWriteAccess=1000");
			System.out.println("================================================================================================");
			startSimulation(500, 500, 1000, 1000, 1000);
			} 
		catch (InterruptedException e)
			{
			e.printStackTrace();
			}
		catch (ExecutionException e)
			{
			e.printStackTrace();
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private static void startSimulation(int nbProducer, int nbConsumer, int bufferSize, int nbReadAccess, int nbWriteAccess) throws InterruptedException, ExecutionException
		{
		Buffer buffer = new Buffer(bufferSize);

		ExecutorService executor = Executors.newFixedThreadPool(nbConsumer + nbProducer);
		
		List<Callable<Integer>> listCallable = IntStream//
			.range(0, nbConsumer)//
			.mapToObj(i -> new Consumer(buffer, nbReadAccess))//
			.collect(Collectors.toList());

		IntStream.range(0, nbProducer)//
			.mapToObj(i -> new Producer( buffer, nbWriteAccess))//
			.forEach(p -> listCallable.add(p));

		long startTimestamp = System.currentTimeMillis();
		
		List<Future<Integer>> listFutures = executor.invokeAll(listCallable);

		int nbTotalAccess = 0;

		for (Future<Integer> future : listFutures) {
			nbTotalAccess += future.get();
		}

		long elapsedTime = System.currentTimeMillis() - startTimestamp;
		float debit = (nbTotalAccess) / (float)elapsedTime;
		float latence = elapsedTime / (float)(nbTotalAccess);

		executor.shutdown();

		System.out.println("\nRESULTS :");
		System.out.println("------------");
		System.out.println("Total duration : " + elapsedTime + " ms");
		System.out.println("Total access : " + nbTotalAccess);
		System.out.println("Debit : " + debit + " access/ms");
		System.out.println("Latence : " + latence + " ms/access");
		}
	}
