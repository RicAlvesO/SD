import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ContactList extends ArrayList<Contact> {

    public ContactList (List<Contact> contacts) {
        super(contacts);
    }

    // @TODO
    public void serialize (DataOutputStream out) throws IOException { 
        out.writeInt(size());
        for (Contact contact : this) {
            contact.serialize(out);
        }
    }

    // @TODO
    public static ContactList deserialize (DataInputStream in) throws IOException { 
        int size = in.readInt();
        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            contacts.add(Contact.deserialize(in));
        }
        return new ContactList(contacts);
    }

}
