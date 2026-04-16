public class Veterinarian extends Person{
    private String specialization;

    Veterinarian(String name, String surname, String phoneNumber, String specialization){
        super(name, surname, phoneNumber);
        this.specialization=specialization;
    }
}
