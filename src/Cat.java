public class Cat extends Pet{

    private final String CatBreed;
    private final String FurType;
    private boolean isIndoor;

    Cat(String namePet, int age, double weight, Client owner, String CatBreed, String FurType, boolean isIndoor){
        super(namePet, age, weight, owner);
        this.CatBreed=CatBreed;
        this.FurType=FurType;
        this.isIndoor=isIndoor;
    }

    public String getCatBreed(){
        return CatBreed;
    }
    public String getFurType(){
        return FurType;
    }

    public boolean isIndoor(){
        return isIndoor;
    }
    public void setIndoor(boolean indoor){
        this.isIndoor=indoor;
    }


    @Override
    public void makeSound(){
        System.out.println(getNamePet()+ " MIAU MIAU");
    }
}
