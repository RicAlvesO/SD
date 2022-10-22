import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {

    private static class Account {
        private int balance;
        private ReentrantLock accLock = new ReentrantLock();
        Account(int balance) { this.balance = balance; }
        int balance() { return balance; }
        boolean deposit(int value) {
            accLock.lock();
            balance += value;
            accLock.unlock();
            return true;
        }
        boolean withdraw(int value) {
            if (value > balance)
                return false;
            accLock.lock();
            balance -= value;
            accLock.unlock();
            return true;
        }
    }

    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;
    private ReentrantLock bankLock = new ReentrantLock();

    // create account and return account id
    public int createAccount(int balance) {
        Account c = new Account(balance);
        bankLock.lock();
        int id = nextId;
        nextId += 1;
        map.put(id, c);
        bankLock.unlock();
        return id;
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        bankLock.lock();
        Account c = map.remove(id);
        if (c == null){
            return 0;
        }
        c.accLock.lock();
        bankLock.unlock();
        int bal=c.balance();
        c.accLock.unlock();
        return bal;
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        bankLock.lock();
        Account c = map.get(id);
        if (c == null)
            return 0;
        c.accLock.lock();
        bankLock.unlock();
        int bal = c.balance();
        c.accLock.unlock();
        return bal;
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        bankLock.lock();
        Account c = map.get(id);
        if (c == null)
            return false;
        c.accLock.lock();
        bankLock.unlock();
        boolean b = c.deposit(value);
        c.accLock.unlock();
        return b;
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        bankLock.lock();
        Account c = map.get(id);
        if (c == null)
            return false;
        c.accLock.lock();
        bankLock.unlock();
        boolean b = c.withdraw(value);
        c.accLock.unlock();
        return b;
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        bankLock.lock();
        cfrom = map.get(from);
        cto = map.get(to);
        if (cfrom == null || cto ==  null)
            return false;
        cfrom.accLock.lock();
        cto.accLock.lock();
        bankLock.unlock();
        boolean b = cfrom.withdraw(value) && cto.deposit(value);
        cfrom.accLock.unlock();
        cto.accLock.unlock();
        return b;
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance() {
        Integer ids[] = this.getIds();
        int total = 0;
        bankLock.lock();
        Account[] accs = new Account[ids.length];
        int j=0;
        for (int id : ids) {
            accs[j] = map.get(id);
            if (accs[j] == null)
                return 0;
            accs[j].accLock.lock();
            j++;
        }
        bankLock.unlock();
        for (Account ac : accs) {
            total += ac.balance();
            ac.accLock.unlock();
        }
        return total;
    }

    public Integer[] getIds() {
        bankLock.lock();
        Set<Integer> keys = map.keySet();
        Integer[] ids = keys.toArray(new Integer[keys.size()]);
        bankLock.unlock();
        return ids;
    }

}
