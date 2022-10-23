import java.util.ArrayList;
import java.util.List;

class BankTest implements Runnable {
  public  static void main(String[] args) {
    final long N=10;
    List<Thread> threads = new ArrayList<Thread>();
    for (int i = 0; i < N; i++) {
      Thread t = new Thread(new BankTest());
      t.start();
      threads.add(t);
    }
    while (threads.stream().anyMatch(t -> t.isAlive())) {
      System.out.println("Still running");
    }
    System.out.println("Done");
  }
  
  public void run() {
    final long I=100;
    
    for (long i = 0; i < I; i++)
      System.out.println("["+Thread.currentThread().getId()+"] "+i);
  }
}
