class BankTest {
  	public static void main(String[] args) throws InterruptedException {
		// Variable Declarations 
		final int     AM = 10000;                                         // RECOMENDED: 10000   | Cash per Account
    	final int     TN = 2*Runtime.getRuntime().availableProcessors();  // RECOMENDED: 2*#CPUs | Threads 
    	final int     N  = 10000;                                         // RECOMENDED: 10000   | Accounts per Thread
		final int 	  OP = 10000;									      // RECOMENDED: 10000   | Operations per Thread 
    	final boolean V  = true;                                         // RECOMENDED: true    | Verbose
		final long startTime = System.nanoTime(); // Global Clock: runtime start to finish

		//------------------------------------------------------------

		// Create Empty Bank
    	Bank b = new Bank();
		int at_bal = b.totalBalance();
		if(at_bal!=0){
			System.out.println("Bank is not empty at creation"); 
			return; // Bank creation error
    	}
		System.out.println("[" + Thread.currentThread().getName().toUpperCase() + "] Total Balance - Start : " + at_bal);
  		
		//------------------------------------------------------------
		
		// Add Accounts to Bank
		long startTimeF = System.nanoTime();
		add_accounts(b, TN, N, AM, V);
		long timeF = System.nanoTime();
		at_bal=b.totalBalance();
		
		// Account creation error
		if(at_bal!=TN*N*AM){
			System.out.println("Bank total balance is not correct after account creation"); 
			return; 
    	}

		//Print Results
		System.out.println("[CLOCK] "+TN*N+" Accounts were created in "+(timeF-startTimeF)/1000000+" ms");
    	System.out.println("["+Thread.currentThread().getName().toUpperCase()+"] Total Balance - Accounts Created : "+at_bal);
		
		//------------------------------------------------------------
		
		// Get all Account IDs
		// This is not needed atm
		// It's used sync the bank could use a non linear id generation
		// This would make the bank more robust 
    	Integer[] accs=b.getIds();

		//------------------------------------------------------------
		
		// Make random balance requests on the bank accounts
    	startTimeF = System.nanoTime();
		make_operations(b, accs, TN, OP, 1, V);
    	timeF = System.nanoTime();
		at_bal=b.totalBalance();

		// Random Operations error
		if(at_bal!=TN*N*AM){
			System.out.println("Bank total balance is not correct after random balance requests"); 
			return; 
    	}

		//Print Results 
		System.out.println("[CLOCK] " + TN * OP + " Balances were checked in " + (timeF - startTimeF) / 1000000 + " ms");
    	System.out.println("["+Thread.currentThread().getName().toUpperCase()+"] Total Balance - Random Operations : "+at_bal);
		

		//------------------------------------------------------------
		
		// Make random deposits and withdraws on the bank accounts
    	startTimeF = System.nanoTime();
		make_operations(b, accs, TN, OP, 0, V);
    	timeF = System.nanoTime();
		at_bal=b.totalBalance();

		// Random Operations error
		if(at_bal!=TN*N*AM){
			System.out.println("Bank total balance is not correct after random deposits and withdraws"); 
			return; 
    	}

		//Print Results 
		System.out.println("[CLOCK] " + TN * OP + " Deposits+Withdraws were made in " + (timeF - startTimeF) / 1000000 + " ms");
    	System.out.println("["+Thread.currentThread().getName().toUpperCase()+"] Total Balance - Random Operations : "+at_bal);
		

		//------------------------------------------------------------
		
		// Make random trasnferences on the bank accounts
    	startTimeF = System.nanoTime();
		make_operations(b, accs, TN, OP, 2, V);
    	timeF = System.nanoTime();
		at_bal=b.totalBalance();

		// Random Operations error
		if(at_bal!=TN*N*AM){
			System.out.println("Bank total balance is not correct after random transferences"); 
			return; 
    	}

		//Print Results 
		System.out.println("[CLOCK] " + TN * OP + " Transferences were made in " + (timeF - startTimeF) / 1000000 + " ms");
    	System.out.println("["+Thread.currentThread().getName().toUpperCase()+"] Total Balance - Random Operations : "+at_bal);
		

		//------------------------------------------------------------
		
		// Make random operations on the bank accounts
    	startTimeF = System.nanoTime();
		make_operations(b, accs, TN, OP, -1, V);
    	timeF = System.nanoTime();
		at_bal=b.totalBalance();

		// Random Operations error
		if(at_bal!=TN*N*AM){
			System.out.println("Bank total balance is not correct after random operations"); 
			return; 
    	}

		//Print Results 
		System.out.println("[CLOCK] " + TN * OP + " Random Operations were made in " + (timeF - startTimeF) / 1000000 + " ms");
    	System.out.println("["+Thread.currentThread().getName().toUpperCase()+"] Total Balance - Random Operations : "+at_bal);
		
		//------------------------------------------------------------
		
		// Remove all accounts from the bank
    	startTimeF = System.nanoTime();
		remove_accounts(b, accs, TN, V);
    	timeF = System.nanoTime();
		at_bal=b.totalBalance();
		
		// Account removing error
		if(at_bal!=0){
			System.out.println("Bank total balance is not 0 after removing accounts"); 
			return; 
    	}

		//Print Results
		System.out.println("[CLOCK] "+TN*N+" Accounts were removed in "+(timeF-startTimeF)/1000000+" ms");
    	System.out.println("["+Thread.currentThread().getName().toUpperCase()+"] Total Balance - End : "+at_bal);
		
		//------------------------------------------------------------

		// Show total execution time
		System.out.println("[CLOCK] Total time: "+(System.nanoTime()-startTime)/1000000+" ms");
	}

	// Multi threaded account creation
  	public static void add_accounts(Bank b, int TN,int N, int AM, boolean V) throws InterruptedException {
    	Thread[] ti = new Thread[TN];
    	for (int j = 0; j < TN; j++) {
    		ti[j] = new Thread(new Creator(b, N, AM, V));
    	}
    	for (int j = 0; j < TN; j++) {
    	  	ti[j].start();
    	}
    	for (int j = 0; j < TN; j++) {
    	  	ti[j].join();
    	}
  	}

	// Multi threaded operations on random accounts
	public static void make_operations(Bank b, Integer[] accs, int TN ,int OP, int mode, boolean V) throws InterruptedException{
 		Thread[] tm = new Thread[TN];
		for (int j = 0; j < TN; j++) {
			tm[j] = new Thread(new Action(b, accs, OP,mode, V));
		}
		for (int j = 0; j < TN; j++) {
			tm[j].start();
		}
		for (int j = 0; j < TN; j++) {
			tm[j].join();
		}
	}

	// Multi threaded account removal
	public static void remove_accounts(Bank b, Integer[] accs, int TN, boolean V) throws InterruptedException{
    	Thread[] tf = new Thread[TN];
    	for (int j = 0; j < TN; j++) {
    	  	tf[j] = new Thread(new Cleaner(b,accs.length/TN,j, accs,V));
    	}
    	for (int j = 0; j < TN; j++) {
    	  	tf[j].start();
    	}
    	for (int j = 0; j < TN; j++) {
    	  	tf[j].join();
    	}
	}
}
