import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

class ContactManager {
    private HashMap<String, Contact> contacts;

    public ContactManager() {
        contacts = new HashMap<>();
        // example pre-population
        update(new Contact("John", 20, 253123321, null, asList("john@mail.com")));
        update(new Contact("Alice", 30, 253987654, "CompanyInc.", asList("alice.personal@mail.com", "alice.business@mail.com")));
        update(new Contact("Bob", 40, 253123456, "Comp.Ld", asList("bob@mail.com", "bob.work@mail.com")));
    }


    // @TODO
    public void update(Contact c) {
        contacts.put(c.name(), c);
    }

    // @TODO
    public ContactList getContacts() {
        return new ContactList (contacts.values().stream().collect(Collectors.toCollection(ArrayList::new)));
    }
}

class ServerWorker implements Runnable {
    private Socket socket;
    private ContactManager manager;

    public ServerWorker (Socket socket, ContactManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    // @TODO
    @Override
    public void run() { 
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            manager.getContacts().serialize(out);
            manager.update(Contact.deserialize(in));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


public class Server {

    public static void main (String[] args) throws IOException {
        try (
            ServerSocket serverSocket = new ServerSocket(12345)) {
            ContactManager manager = new ContactManager();

            while (true) {
                Socket socket = serverSocket.accept();
                Thread worker = new Thread(new ServerWorker(socket, manager));
                worker.start();
            }
        }
    }
}
