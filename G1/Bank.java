import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

class Bank implements Runnable {

  private static class Account {
    private int balance;
    Account(int balance) { this.balance = balance; }
    int balance() { return balance; }
    boolean deposit(int value) {
      balance += value;
      return true;
    }
  }

  // Our single account, for now
  private Account savings = new Account(0);
  ReentrantLock lock = new ReentrantLock();

  // Account balance
  public int balance() {
    return savings.balance();
  }

  // Deposit
  boolean deposit(int value) {
    lock.lock();
    boolean res = savings.deposit(value);
    lock.unlock();
    return res;
  }

  public static void main(String[] args) {
    final long N = 10;
    Bank b = new Bank();
    List<Thread> threads = new ArrayList<Thread>();
    for (int i = 0; i < N; i++) {
      Thread t = new Thread(b);
      t.start();
      threads.add(t);
    }
    for (Thread t : threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        System.err.println("Interrupted");
      }
    }
    System.out.println("Done, total: "+b.balance());
  }

  @Override
  public void run() {
    for (int i = 0; i < 1000; i++) {
      deposit(100);
    }
  }

}
