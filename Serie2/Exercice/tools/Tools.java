package Exercice.tools;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class Tools {
    public static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * 100);
        }
        return array;
    }

    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }

        System.out.println();
    }

    public static int sumIntArray(int[] array) {
        return Arrays.stream(array).parallel().sum();
    }

    public static void monitoringPool(ForkJoinPool pool) {
        System.out.println("Pool size: " + pool.getPoolSize());
        System.out.println("Active threads: " + pool.getActiveThreadCount());
        System.out.println("Queued tasks: " + pool.getQueuedTaskCount());
        System.out.println("Steal count: " + pool.getStealCount());
        System.out.println("Parallelism: " + pool.getParallelism());
    }
}
