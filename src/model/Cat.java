package model;

public class Cat extends Pet {

    private final String catBreed;
    private final String furType;
    private boolean isIndoor;

    public Cat(String namePet, int age, double weight, Client owner, String CatBreed, String FurType, boolean isIndoor){
        super(namePet, age, weight, owner);
        this.catBreed=CatBreed;
        this.furType=FurType;
        this.isIndoor=isIndoor;
    }

    public String getCatBreed(){
        return catBreed;
    }
    public String getFurType(){
        return furType;
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
