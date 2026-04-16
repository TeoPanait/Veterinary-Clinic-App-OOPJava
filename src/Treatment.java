public class Treatment {
    public String name;
    private String administrationMode;
    private double cost;

    Treatment(String name, String administrationMode, double cost){
        this.name=name;
        this.administrationMode=administrationMode;
        this.cost=cost;
    }
    public String getName(){
        return name;
    }

    public String getAdministrationMode(){
        return administrationMode;
    }
    public double getCost(){
        return cost;
    }


}
