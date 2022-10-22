import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Bank {

    private static class Account {
        private int balance;
        private ReentrantReadWriteLock accLock = new ReentrantReadWriteLock();
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

    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;
    private ReentrantReadWriteLock bankLock = new ReentrantReadWriteLock();

    // create account and return account id
    public int createAccount(int balance) {
        Account c = new Account(balance);
        bankLock.writeLock().lock();
        int id = nextId;
        nextId += 1;
        map.put(id, c);
        bankLock.writeLock().unlock();
        return id;
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        bankLock.writeLock().lock();
        Account c = map.remove(id);
        if (c == null){
            return 0;
        }
        c.accLock.readLock().lock();
        bankLock.writeLock().unlock();
        int bal=c.balance();
        c.accLock.readLock().unlock();
        return bal;
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        bankLock.readLock().lock();
        Account c = map.get(id);
        if (c == null)
            return 0;
        c.accLock.readLock().lock();
        bankLock.readLock().unlock();
        int bal = c.balance();
        c.accLock.readLock().unlock();
        return bal;
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        bankLock.readLock().lock();
        Account c = map.get(id);
        if (c == null)
            return false;
        c.accLock.writeLock().lock();
        bankLock.readLock().unlock();
        boolean b = c.deposit(value);
        c.accLock.writeLock().unlock();
        return b;
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        bankLock.readLock().lock();
        Account c = map.get(id);
        if (c == null)
            return false;
        c.accLock.writeLock().lock();
        bankLock.readLock().unlock();
        boolean b = c.withdraw(value);
        c.accLock.writeLock().unlock();
        return b;
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        bankLock.readLock().lock();
        cfrom = map.get(from);
        cto = map.get(to);
        if (cfrom == null || cto ==  null)
            return false;
        bankLock.readLock().unlock();
        if (withdraw(from, value))
            if (deposit(to, value))
                return true;
            else {
                deposit(from, value);
                return false;
            }
        return false;
    }
    
    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance() {
        Integer ids[] = this.getIds();
        int total = 0;
        bankLock.readLock().lock();
        Account[] accs = new Account[ids.length];
        int j=0;
        for (int id : ids) {
            accs[j] = map.get(id);
            if (accs[j] == null)
                return 0;
            accs[j].accLock.readLock().lock();
            j++;
        }
        bankLock.readLock().unlock();
        for (Account ac : accs) {
            total += ac.balance();
            ac.accLock.readLock().unlock();
        }
        return total;
    }

    public Integer[] getIds() {
        bankLock.readLock().lock();
        Set<Integer> keys = map.keySet();
        Integer[] ids = keys.toArray(new Integer[keys.size()]);
        bankLock.readLock().unlock();
        return ids;
    }

}
