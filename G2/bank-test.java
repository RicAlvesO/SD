import java.util.Random;

class Mover implements Runnable {
  Bank b;
  int s; // Number of accounts

  public Mover(Bank b, int s) { this.b=b; this.s=s; }

  public void run() {
    final int moves=100000;
    int from, to;
    Random rand = new Random();

    for (int m=0; m<moves; m++)
    {
      from=rand.nextInt(s); // Get one
      while ((to=rand.nextInt(s))==from); // Slow way to get distinct
      b.transfer(from,to,1);
    }
  }
}

class BankTest {
  public static void main(String[] args) throws InterruptedException {
    final int N=10;

    Bank b = new Bank(N);

    for (int i=0; i<N; i++) 
      b.deposit(i,1000);

    System.out.println(b.totalBalance());

    Thread[] t;

    for (int i = 0; i < 10; i++) {
      t = new Thread[10];
      for (int j = 0; j < 10; j++) {
        t[j] = new Thread(new Mover(b, 10));
        t[j].start();
      }
      for (int j = 0; j < 10; j++) {
        t[j].join();
      }
    }

    System.out.println(b.totalBalance());
  }
}
