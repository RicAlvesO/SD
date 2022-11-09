import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {
	private Map<String, Product> map = new HashMap<String, Product>();
	private ReentrantLock lock = new ReentrantLock();

	public Warehouse() {
	}

	private class Product {
		int quantity = 0;
		private Condition condition = this.lock.newCondition();
	}

	private Product get(String item) {
		Product p = map.get(item);
		if (p != null)
			return p;
		p = new Product();
		map.put(item, p);
		return p;
	}

	public void supply(String item, int quantity) {
		lock.lock();
		Product p = get(item);
		p.quantity += quantity;
		p.condition.signalAll();
		lock.unlock();
	}

	public void consume(Set<String> items) {
	  	lock.lock();
	  	for (String s : items)
		  while (p.quantity==0) p.condition.await();
		  Product p=get(s);
		p.quantity--;
		lock.unlock();
    }
}
