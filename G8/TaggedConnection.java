package g8;

import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final ReentrantLock wlock = new ReentrantLock();
    private final ReentrantLock rlock = new ReentrantLock();

    public static class Frame {
        public final int tag;
        public final byte[] data;

        public Frame(int tag, byte[] data) {
            this.tag = tag;
            this.data = data;
        }
    }

    public TaggedConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void send(Frame frame) throws IOException {
        wlock.lock();
        out.writeInt(frame.tag);
        out.writeInt(frame.data.length);
        out.write(frame.data);
        out.flush();
        wlock.unlock();
    }

    public void send(int tag, byte[] data) throws IOException {
        send(new Frame(tag, data));
    }

    public Frame receive() throws IOException {
        rlock.lock();
        int tag = in.readInt();
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readFully(data);
        rlock.unlock();
        return new Frame(tag, data);
    }

    public void close() throws IOException {
        socket.close();
    }
}