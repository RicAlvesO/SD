public class Worker implements Runnable {

    Barrier b;

    public Worker(Barrier b) {
        this.b = b;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Worker "+Thread.currentThread().getName()+" is busy!");
        }
        try {
            b.nAwait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Worker " + Thread.currentThread().getName() + " was released!");
    }
}