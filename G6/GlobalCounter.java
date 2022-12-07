import java.util.concurrent.locks.ReentrantLock;

public class GlobalCounter {

    private double count=0;
    private int amount=0;
    private ReentrantLock lock=new ReentrantLock();

    public GlobalCounter(){
    }

    public void increment(Double n) {
        this.lock.lock();
        this.count += n;
        this.amount++;
        this.lock.unlock();
    }

    public double getCount() {
        this.lock.lock();
        double d = this.count;
        this.lock.unlock();
        return d;
    }

    public double getMean() {
        this.lock.lock();
        double d = this.count / this.amount;
        this.lock.unlock();
        return d;
    }   
}
