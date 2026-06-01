package model;

// factory pattern: creates pet objects based on type enum
// encapsulates pet instantiation logic and ensures correct subclass creation
public class PetFactory {

    // create pet instance based on type: returns Dog or Cat
    // parameters: type (DOG/CAT), namePet, age, weight, owner, breed, secondAtr (size for dog / furType for cat), isIndoor
    public static Pet createPet(PetType type, String namePet, int age, double weight, Client owner,
                                String breed, String secondAtr, boolean isIndoor) {
        if (type == null){
            throw new IllegalArgumentException("Pet type is null");
        }
        switch(type){
            case DOG:
                // create dog with breed and size
                return new Dog(namePet, age, weight, owner, breed, secondAtr);
            case CAT:
                // create cat with breed, fur type, and indoor status
                return new Cat(namePet, age, weight, owner, breed,secondAtr, isIndoor);
            default:
                throw new IllegalArgumentException("Unknown pet type: " + type);
        }
    }

}
