public class Veterinarian extends Person{
    private String specialization;

    Veterinarian(String name, String surname, String phoneNumber, String specialization){
        super(name, surname, phoneNumber);
        this.specialization=specialization;
    }

    public String getSpecialization(){
        return specialization;
    }

    public void setSpecialization(String specialization){
        this.specialization=specialization;
    }
}
