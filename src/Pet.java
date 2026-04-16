public class Pet {
    private static int idCounter=1;
    private int id;
    private String namePet;
    private int age;
    private double weight;
    private Client owner;

    Pet(String namePet, int age, double weight, Client owner){
        this.id=idCounter++;
        this.namePet=namePet;
        this.age=age;
        this.weight=weight;
        this.owner=owner;
    }

    public int getId(){
        return id;
    }

    public String getNamePet(){
        return namePet;
    }
    public void setNamePet(){
        this.namePet=namePet;
    }

    public int getAge(){
        return age;
    }
    public void setAge(int newAge){
        if(newAge>0) {
            this.age=newAge;
        }else {
            System.out.println("Eroare!! Varsta trebuie mai mare de 0");
        }


    }

    public double getWeight(){
        return weight;
    }
    public void setWeight(double newWeight){
        if(newWeight>0.0){
            this.weight=weight;
        } else {
            System.out.println("Eroare!! Greutatea nu poate fi negativa");
        }
    }

    public Client getOwner(){
        return owner;
    }
    public void setOwner(Client newOwner){
        if(newOwner!=null){
            this.owner=newOwner;
        }else {
            System.out.println("Eroare! Animalul trb sa aiba un stapan valid");
        }
    }

    public void makeSound(){
        System.out.println("Sunet nedefinit");
    }
}
