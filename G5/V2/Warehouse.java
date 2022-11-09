import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {
	private Map<String, Product> map = new HashMap<String, Product>();
	private ReentrantLock lock = new ReentrantLock();

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

	// Errado se faltar algum produto...
	public void consume(Set<String> items) {
		int hasAll = 0;
		int times_waited = 0;
		lock.lock();
		while hasAll<items.size() {
			for (String s : items)
				Product p=get(s);
				if p.quantity==0 {
					p.condition.await();
					hasAll=0;
					times_waited++;
					break;
				}

		}
	  	for (String s : items)
			Product p=get(s);
			p.quantity--;
	  	lock.unlock();
  	}
}
