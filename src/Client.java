import java.util.Locale;

public class Client extends Person{
    private String email;
    private String address;

    Client(String name, String surname, String phoneNumber,
           String email, String address){
        super(name, surname, phoneNumber);
        this.email=email;
        this.address=address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
