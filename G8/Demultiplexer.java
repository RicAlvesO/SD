package g8;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class Demultiplexer implements AutoCloseable {

    private final TaggedConnection conn;
    private final ReentrantLock lock = new ReentrantLock();
    private Map <Integer, Despertador> despertadores = new HashMap<>();

    public static class Despertador(int tag){
        private final ReentrantLock lock;
        private final Condition desp;
        private final Queue<Frame> queue;

        public Despertador(){
            this.lock = new ReentrantLock();
            this.desp = this.lock.newCondition();
            this.queue = new LinkedList<>();
        }
    }

    public Demultiplexer(TaggedConnection conn) {
        this.conn = conn;
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    TaggedConnection.Frame frame = conn.receive();
                    Despertador d=this.despertadores.get(frame.tag);
                    d.lock.lock();
                    d.queue.add(frame);
                    d.desp.signal();
                    d.lock.unlock();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void send(Frame frame) throws IOException {
        this.lock.lock();
        if (this.despertadores.containsKey(frame.tag)==false) {
            this.despertadores.put(frame.tag, new Despertador());
        }
        this.lock.unlock();
        conn.send(frame);
    }

    public void send(int tag, byte[] data) throws IOException {
        this.lock.lock();
        if (this.despertadores.containsKey(frame.tag)==false) {
            this.despertadores.put(frame.tag, new Despertador());
        }
        this.lock.unlock();
        conn.send(tag, data);
    }

    public byte[] receive(int tag) throws IOException, InterruptedException {
        Despertador d = this.despertadores.get(tag);
        d.lock.lock();
        d.desp.await();
        Frame frame = d.queue.poll();
        d.lock.unlock();
        return frame.data;
    }

    public void close() throws IOException {
        conn.close();
    }
}