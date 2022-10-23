import java.util.concurrent.locks.ReentrantLock;

public class Bank {

  private static class Account {
    private int balance;
    Account(int balance) { this.balance = balance; }
    int balance() { return balance; }
    boolean deposit(int value) {
      balance += value;
      return true;
    }
    boolean withdraw(int value) {
      if (value > balance)
        return false;
      balance -= value;
      return true;
    }
  }

  // Bank slots and vector of accounts
  private int slots;
  private Account[] av; 
  ReentrantLock lock = new ReentrantLock();

  public Bank(int n)
  {
    slots=n;
    av=new Account[slots];
    for (int i=0; i<slots; i++) av[i]=new Account(0);
  }

  // Account balance
  public int balance(int id) {
    if (id < 0 || id >= slots)
    return 0;
    lock.lock();
    int balance = av[id].balance();
    lock.unlock();
    return balance;
  }

  // Deposit
  boolean deposit(int id, int value) {

    if (id < 0 || id >= slots)
      return false;
    lock.lock();
    boolean dep = av[id].deposit(value);
    lock.unlock();
    return dep;
  }

  // Withdraw; fails if no such account or insufficient balance
  public boolean withdraw(int id, int value) {
    if (id < 0 || id >= slots)
      return false;
    lock.lock();
    boolean wd = av[id].withdraw(value);
    lock.unlock();
    return wd;
  }

  boolean transfer(int from, int to, int value){
    if (from < 0 || from >= slots || to < 0 || to >= slots)
      return false;
    lock.lock();
    boolean wd = withdraw(from,value);
    if (!wd) {
      lock.unlock();
      return false;
    }
    boolean dep = deposit(to,value);
    if(!dep){
      deposit(from,value);
      lock.unlock();
      return false;
    }
    lock.unlock();
    return dep&&wd;
  }

  int totalBalance(){
    int total = 0;
    lock.lock();
    for (int i = 0; i < slots; i++) {
      total += balance(i);
    }
    lock.unlock();
    return total;
  }
  
}
