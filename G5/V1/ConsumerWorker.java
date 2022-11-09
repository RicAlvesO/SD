import java.util.Set;
import java.util.ArrayList;

class ConsumerWorker implements Runnable{
    Warehouse w;
    ArrayList<String> items;
    int amount;

    public SupplyWorker(Warehouse w, ArrayList<String> items, int amount){
        this.w = w;
        this.items = items;
        this.amount = amount;
    }

    public void run() {
        try{
            for (int i=0; i<this.amount; i++){
                Set<String> items_to_consume = new HashSet<String>();
                int n_supplies = (int)(Math.random()*20);
                for (j=0;j<n_supplies;j++){
                    items_to_consume.add(items.get((int)(Math.random()*items.size())));
                }
                w.consume(items_to_consume);
                System.out.println("ConsumerWorker " + Thread.currentThread().getName() + " got " + items_to_consume.toString() + "!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ConsumerWorker " + Thread.currentThread().getName() + " has finished!");
    }
}
