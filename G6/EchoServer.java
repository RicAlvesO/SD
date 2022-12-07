import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class EchoServer {

    
    public static void main(String[] args) {
        
        try {
            GlobalCounter globalCounter=new GlobalCounter();
            ServerSocket ss = new ServerSocket(12345);
            ExecutorService pool = Executors.newFixedThreadPool(16);
            while (true) {
                Socket socket = ss.accept();
                Runnable ch=new ClientHandler(socket, globalCounter);
                pool.execute(ch);
            }
            //pool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
