import java.util.Random;

class Action implements Runnable {
    Bank b;         // Bank
    Integer ids[];  // Array of account ids
    int am;         // Amount of operations
    int mode;       // -1 for random operations
    boolean v;      // Verbose

    // Constructor with Verbose option
    public Action(Bank b, Integer[] ids, int am, int mode, boolean v) {
        this.b = b;
        this.ids = ids;
        this.am = am;
        this.mode = mode;
        this.v = v;
    }

    // Default Constructor
    public Action(Bank b, Integer[] ids, int am, int mode) {
        this.b = b;
        this.ids = ids;
        this.am = am;
        this.mode = mode;
        this.v = false;
    }

    public void run() {
        int from, to, value, op=mode;
        Random rand = new Random();

        // Make 'am' number of random operations in random accounts
        for (int m = 0; m < am; m++) {
            if (mode==-1)
                op = rand.nextInt(3); // Random Operation
            value = rand.nextInt(10000); // Random Value
            from = rand.nextInt(ids.length); // Random Account
            to = rand.nextInt(ids.length); // Random Account

            // Operation Switch
            switch (op) {

                // Whitdraw & Deposit
                case 0:
                    if (m + 1 != am) {
                        m += wnd(from, to, value);
                    } else if (mode==0) {
                        wnd(from, to, 0);
                    } else 
                        m--;
                    break;

                // Balance
                case 1:
                    int bal = b.balance(ids[from]);
                    if (v)
                        System.out.println(
                                "[" + Thread.currentThread().getName().toUpperCase() + "] Balance of " + ids[from] + " is " + bal);
                    break;

                // Transfer
                case 2:
                    if (b.transfer(ids[from], ids[to], value) && v) {
                        System.out.println("[" + Thread.currentThread().getName().toUpperCase() + "] Transfer " + value + " from "
                                + ids[from] + " to " + ids[to]);
                    }
                    break;
            }
        }
    }

    // Whitdraw & Deposit
    public int wnd(int from, int to, int value) {
        if (b.withdraw(ids[from], value)) {
            if (v)
                System.out
                        .println("[" + Thread.currentThread().getName().toUpperCase() + "] Withdraw " + value + " from " + ids[from]);
            if (b.deposit(ids[to], value)) {
                if (v)
                    System.out
                            .println("[" + Thread.currentThread().getName().toUpperCase() + "] Deposit " + value + " to " + ids[to]);
            }
            return 1; // Tried to do 2 operations
        }
        return 0; // Only tried to do 1 operation
    }
}
