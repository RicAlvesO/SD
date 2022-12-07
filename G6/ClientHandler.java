import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private double total;
    private int amount;
    private GlobalCounter gc;

    public ClientHandler(Socket socket, GlobalCounter gc) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
        this.total = 0;
        this.amount = 0;
        this.gc=gc;
    }

    public void run(){
        String line;
        try {
            while ((line = in.readLine()) != null) {
                //check if received line is a number
                if (isNumeric(line)) {
                    double i=Double.parseDouble(line);
                    total += i;
                    amount++;
                    gc.increment(i);
                    out.println(total);
                    out.flush();
                } else {
                    out.println("NaN");
                    out.flush();
                }
            }
            double totalAvarege=gc.getMean();
            out.println("(Client Average: " + total / amount + ", Global Average: " + totalAvarege+")");
            out.flush();

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
