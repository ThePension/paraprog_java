package Exercice.ex1;

import java.util.concurrent.ForkJoinPool;

import Exercice.tools.Tools;

public class UseSum {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();

        int[] data = Tools.generateRandomArray(100000000);

        // Tools.printArray(data);

        // int expected = Tools.sumIntArray(data);

        // System.out.println("Expected: " + expected);

        Sum app = new Sum(data, 0, data.length);

        // Monitore pool every 1 second
        monitoringPool(app, pool, 10);

        pool.invoke(app);

        System.out.println("Result: " + Sum.getResult());
    }

    public static void monitoringPool(Sum app, ForkJoinPool pool, int delay)
    {
        new Thread(() -> {
            while (!app.isDone()) {
                Tools.monitoringPool(pool);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
