package Exercice.ex1;

import java.util.concurrent.RecursiveAction;

public class Sum extends RecursiveAction {
    public Sum(int[] data, int start, int end) {
        super();
        this.data = data;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if ((end - start) < LIMIT) {
            for (int i = start; i < end; i++) {
                result += data[i];
            }
        } else {
            int mid = (start + end) / 2;
            Sum left = new Sum(data, start, mid);
            Sum right = new Sum(data, mid, end);
            left.fork();
            right.fork();
            left.join();
            right.join();
        }
    }

    public static int getResult() {
        return result;
    }

    // Inputs
    private int[] data;
    private int start;
    private int end;
    private static final int LIMIT = 100;

    // Outputs
    private static int result;
}