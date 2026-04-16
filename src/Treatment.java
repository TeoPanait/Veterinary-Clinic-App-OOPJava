public class Treatment {
    private String name;
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

    public void setName(String name){
        this.name = name;
    }

    public String getAdministrationMode(){
        return administrationMode;
    }

    public void setAdministrationMode(String administrationMode){
        this.administrationMode = administrationMode;
    }

    public double getCost(){
        return cost;
    }

    public void setCost(double cost){
        if(cost > 0){
            this.cost = cost;
        } else {
            System.out.println("Error: Cost must be positive");
        }
    }
}
