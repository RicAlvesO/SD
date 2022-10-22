import java.util.Random;

class MoverCL implements Runnable {
  BankCL b;
  int s; // Number of accounts

  public MoverCL(BankCL b, int s) { this.b=b; this.s=s; }

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

class BankTestCL {
  public static void main(String[] args) throws InterruptedException {
    final int N=10;

    BankCL b = new BankCL(N);

    for (int i=0; i<N; i++) 
      b.deposit(i,1000);

    System.out.println(b.totalBalance());

    Thread[] t;
    int tn = 16;

    for (int i = 0; i < tn; i++) {
      t = new Thread[tn];
      for (int j = 0; j < tn; j++) {
        t[j] = new Thread(new MoverCL(b, 10));
      }
      for (int j = 0; j < tn; j++) {
        t[j].start();
      }
      for (int j = 0; j < tn; j++) {
        t[j].join();
      }
    }

    System.out.println(b.totalBalance());
  }
}
