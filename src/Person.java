public class Person {
    private String name;
    private String surname;
    private String phoneNumber;


    Person(String name, String surname, String phoneNumber){
        this.name=name;
        this.surname=surname;
        this.phoneNumber=phoneNumber;
    }

    public String getName(){
        return name;
    }
    public void setName(){
        this.name=name;
    }

    public String getSurname(){
        return surname;
    }
    public  void setSurname(){
        this.surname=surname;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(){
        this.phoneNumber=phoneNumber;
    }
}
