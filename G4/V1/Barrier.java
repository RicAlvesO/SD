import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
    private int count;
    private int limit;
    private int cleaner;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public Barrier (int TN) {
        this.count = 0;
        this.cleaner = TN;
        this.limit = TN;    
    }

    void nAwait() throws InterruptedException {
        lock.lock();
        while(count==limit){
            condition.await();
        }
        count++;
        while (count < limit) {
            condition.await();
        }
        if (count==limit && cleaner==limit) {
            condition.signalAll();
        }
        cleaner--;
        if (cleaner==0){
            count=0;
            cleaner=limit;
            condition.signalAll();
        }
        lock.unlock();
    }
}
