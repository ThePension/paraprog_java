
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
			// IMPORTANT : The product (nbProducer*nbWriteAccess) == (nbConsumer*nbReadAccess)
			// It is also important to have a huge amount of access to the blocking queue to limit the random in the results.
			// RULE : (nbProducer*nbWriteAccess) == (nbConsumer*nbReadAccess) >= 500'000

			startSimulation(12, 800, 200, 1, 2500, 625);
			startSimulation(13, 800, 200, 2, 2500, 625);
			startSimulation(14, 800, 200, 5, 2500, 625);
			startSimulation(15, 800, 200, 10, 2500, 625);
			startSimulation(0, 800, 200, 20, 2500, 625);
			startSimulation(1, 800, 200, 50, 2500, 625);
			startSimulation(2, 800, 200, 100, 2500, 625);
			startSimulation(3, 800, 200, 200, 2500, 625);
			startSimulation(4, 800, 200, 300, 2500, 625);
			startSimulation(5, 800, 200, 400, 2500, 625);
			startSimulation(6, 800, 200, 500, 2500, 625);
			startSimulation(7, 800, 200, 600, 2500, 625);
			startSimulation(8, 800, 200, 700, 2500, 625);
			startSimulation(9, 800, 200, 800, 2500, 625);
			startSimulation(10, 800, 200, 900, 2500, 625);
			startSimulation(11, 800, 200, 1000, 2500, 625);
			
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

	private static void startSimulation(int index, int nbProducer, int nbConsumer, int bufferSize, int nbReadAccess, int nbWriteAccess) throws InterruptedException, ExecutionException
		{
		System.out.println("\n\nSimulation " + index + " : nbProducer=" + nbProducer + ", nbConsumer=" + nbConsumer + ", bufferSize=" + bufferSize + ", nbReadAccess=" + nbReadAccess + ", nbWriteAccess=" + nbWriteAccess);
		System.out.println("================================================================================================");

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
