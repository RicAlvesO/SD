class Test {
    public static void main(String[] args) throws InterruptedException {
        final int TN = 2 * Runtime.getRuntime().availableProcessors(); // RECOMENDED: 2*#CPUs | Threads
        Barrier b=new Barrier(TN);
        for(int i=0;i<2;i++){
            testBarrier(b, TN);
        }
    }

    private static void testBarrier(Barrier b, int TN) throws InterruptedException {
        Thread[] tf = new Thread[TN];

        for (int j = 0; j < TN; j++) {
            tf[j] = new Thread(new Worker(b));
        }
        for (int j = 0; j < TN; j++) {
            tf[j].start();
        }
        for (int j = 0; j < TN; j++) {
            tf[j].join();
        }
    }
}

