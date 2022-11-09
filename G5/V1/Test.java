import java.util.ArrayList;

class Test {
    public static void main(String[] args) throws InterruptedException{
        final int TN = Runtime.getRuntime().availableProcessors();
        final int N = 1000000;
        Warehouse w = new Warehouse();
        ArrayList<String> items = new ArrayList<String>();
        items.add("apple");
        items.add("banana");
        items.add("orange");
        items.add("pear");
        items.add("grape");
        items.add("watermelon");
        items.add("pineapple");
        items.add("strawberry");
        items.add("peach");
        items.add("mango");
        items.add("kiwi");
        items.add("lemon");
        items.add("lime");
        items.add("coconut");
        items.add("cherry");
        supply(w, items, TN*0.5, N);
        consume(w, items, TN*1.5, N);
    }

    private static void supply(Warehouse w, ArrayList<String> items, int TN, int N) throws InterruptedException {
        Thread[] tf = new Thread[TN];

        for (int j = 0; j < TN; j++) {
            tf[j] = new Thread(new SupplyWorker(b, items, N));
        }
        for (int j = 0; j < TN; j++) {
            tf[j].start();
        }
        for (int j = 0; j < TN; j++) {
            tf[j].join();
        }
    }

    private static void consume(Warehouse w, ArrayList<String> items, int TN, int N) throws InterruptedException {
        Thread[] tf = new Thread[TN];

        for (int j = 0; j < TN; j++) {
            tf[j] = new Thread(new ConsumerWorker(b, items, N));
        }
        for (int j = 0; j < TN; j++) {
            tf[j].start();
        }
        for (int j = 0; j < TN; j++) {
            tf[j].join();
        }
    }
    
}
