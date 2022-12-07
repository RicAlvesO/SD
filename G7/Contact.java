import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

class Contact {
    private String name;
    private int age;
    private long phoneNumber;
    private String company;     // Pode ser null
    private ArrayList<String> emails;

    public Contact (String name, int age, long phoneNumber, String company, List<String> emails) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.emails = new ArrayList<>(emails);
    }

    public String name() { return name; }
    public int age() { return age; }
    public long phoneNumber() { return phoneNumber; }
    public String company() { return company; }
    public List<String> emails() { return new ArrayList<>(emails); }

    // @TODO
    public void serialize (DataOutputStream out) throws IOException { 
        out.writeUTF(name);
        out.writeInt(age);
        out.writeLong(phoneNumber);
        if (company!=null){ 
            out.writeInt(1);
            out.writeUTF(company);
        }else{
            out.writeInt(0);
        }
        out.writeInt(emails.size());
        for (String email : emails) {
            out.writeUTF(email);
        }
    }

    // @TODO
    public static Contact deserialize (DataInputStream in) throws IOException { 
        String name = in.readUTF();
        int age = in.readInt();
        long phoneNumber = in.readLong();
        int companyCheck = in.readInt();
        String company = null;
        if (companyCheck==1) company = in.readUTF();
        int size = in.readInt();
        ArrayList<String> emails = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            emails.add(in.readUTF());
        }
        return new Contact(name, age, phoneNumber, company, emails);
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";");
        builder.append(this.age).append(";");
        builder.append(this.phoneNumber).append(";");
        builder.append(this.company).append(";");
        builder.append(this.emails.toString());
        builder.append("}");
        return builder.toString();
    }

}
