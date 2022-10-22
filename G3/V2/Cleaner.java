class Cleaner implements Runnable {
    Bank b;
    int n; // Number of accounts to delete
    int of; // Offset
    Integer[] ids;
    boolean v;

    // Cunstructor with Verbose option
    public Cleaner(Bank b, int n, int of, Integer[] ids, boolean v) {
        this.b = b;
        this.n = n;
        this.of = of;
        this.ids = ids;
        this.v = v;
    }

    // Default Constructor
    public Cleaner(Bank b, int n, int of, Integer[] ids) {
        this.b = b;
        this.n = n;
        this.of = of;
        this.ids = ids;
        this.v = false;
    }

    // Deletes N Accounts from list of IDS starting with N*OF offset
    public void run() {
        int from = this.n * this.of;
        int to = this.n * (this.of + 1);
        for (int m = from; m < to; m++) {
            if (v)
                System.out.println("[" + Thread.currentThread().getName().toUpperCase() + "] Removing Account " + m);
            b.closeAccount(ids[m]);
        }
    }
}