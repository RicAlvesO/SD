package g8;

public class FramedConnection implements AutoCloseable {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final ReentrantLock wlock = new ReentrantLock();
    private final ReentrantLock rlock = new ReentrantLock();

    public FramedConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void send(byte[] data) throws IOException {
        wlock.lock();
        out.writeInt(data.length);
        out.write(data);
        out.flush();
        wlock.unlock();
    }

    public byte[] receive() throws IOException {
        rlock.lock();
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readFully(data);
        rlock.unlock();
        return data;
    }

    public void close() throws IOException {
        socket.close();
    }
}