import java.util.ArrayList;

class SupplyWorker implements Runnable{
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
                int item_id = (int)(Math.random()*items.size());
                int n_supplies = (int)(Math.random()*10);
                w.supply(items.get(item_id), n_supplies);
                System.out.println("SupplyWorker " + Thread.currentThread().getName() + " supplied " + n_supplies + " " + items.get(item_id) + "!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("SupplyWorker " + Thread.currentThread().getName() + " has finished!");
    }
}
