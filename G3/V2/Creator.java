class Creator implements Runnable {
    Bank b;
    int n; // Number of accounts to create
    int of; // Offset
    int am; // Amount to deposit
    boolean v;

    // Cunstructor with Verbose option
    public Creator(Bank b, int n, int am, boolean v) {
        this.b = b;
        this.n = n;
        this.am = am;
        this.v = v;
    }

    // Default Constructor
    public Creator(Bank b, int n, int am) {
        this.b = b;
        this.n = n;
        this.am = am;
        this.v = false;
    }

    // Creates N Accounts with AM amount of money
    public void run() {
        for (int m = 0; m < n; m++) {
            if (v)
                System.out.println("[" + Thread.currentThread().getName().toUpperCase() + "] Creating Account " + m);
            b.createAccount(am);
        }
    }
}
