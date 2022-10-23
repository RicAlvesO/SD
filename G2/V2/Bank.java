import java.util.concurrent.locks.ReentrantLock;

public class Bank {

  private static class AccountCL {
    private int balance;
    public ReentrantLock lock=new ReentrantLock();
    AccountCL(int balance) { this.balance = balance; }
    int balance() { lock.lock();try{return balance;}finally{lock.unlock();} }
    boolean deposit(int value) {
      lock.lock();
      try{balance += value;return true;}finally{lock.unlock();}
    }
    boolean withdraw(int value) {
      lock.lock();
      try{
        if (value > balance){
          return false;
        }
        balance -= value;
        return true;
      }finally{
        lock.unlock();
      }
    }
  }

  // Bank slots and vector of accounts
  private int slots;
  private AccountCL[] av; 

  public Bank(int n)
  {
    slots=n;
    av=new AccountCL[slots];
    for (int i=0; i<slots; i++) av[i]=new AccountCL(0);
  }

  // Account balance
  public int balance(int id) {
    if (id < 0 || id >= slots)
    return 0;
    int balance = av[id].balance();
    return balance;
  }

  // Deposit
  boolean deposit(int id, int value) {

    if (id < 0 || id >= slots)
      return false;
    boolean dep = av[id].deposit(value);
    return dep;
  }

  // Withdraw; fails if no such account or insufficient balance
  public boolean withdraw(int id, int value) {
    if (id < 0 || id >= slots)
      return false;
    boolean wd = av[id].withdraw(value);
    return wd;
  }

  boolean transfer(int from, int to, int value){
    if (from < 0 || from >= slots || to < 0 || to >= slots)
      return false;
    if (from < to){
      av[from].lock.lock();
      av[to].lock.lock();
    }else{
      av[to].lock.lock();
      av[from].lock.lock();
    }
    try{
      boolean wd = withdraw(from,value);
      if(wd){
        boolean dep = deposit(to,value);
        return dep;
      }
      return false;
    }finally{
      av[from].lock.unlock();
      av[to].lock.unlock();
    }
  }

  int totalBalance(){
    int total = 0;
    for (int i = 0; i < slots; i++) {
      av[i].lock.lock();
    }
    for (int i = 0; i < slots; i++) {
      total += balance(i);
      av[i].lock.unlock();
    }
    return total;
  }
}