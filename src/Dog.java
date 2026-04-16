public class Dog extends Pet{
    private final String dogBreed;
    private final String size;

    Dog(String namePet, int age, double weight, Client owner, String dogBreed, String size){
        super(namePet, age, weight, owner);
        this.dogBreed=dogBreed;
        this.size=size;
    }
    public String getDogBreed() {
        return dogBreed;
    }

    public String getSize() {
        return size;
    }

    @Override
    public void makeSound(){
        System.out.println(getNamePet()+ "HAM HAM");
    }
}
