public class Medication {
    private String medName;
    private String dosage;
    private double price;
    private int stock;
    private boolean reqPrescription;

    Medication(String medName, String dosage, double price,
               int stock, boolean reqPrescription){
        this.medName=medName;
        this.dosage=dosage;
        this.price=price;
        this.stock=stock;
        this.reqPrescription=reqPrescription;
    }

    public String getMedName(){
        return medName;
    }

    public String getDosage(){
        return dosage;
    }

    public double getPrice(){
        return price;
    }

    public int getStock(){
        return stock;
    }

    public void addStock(int amount){
        if(amount>0){
            this.stock+=amount;
            System.out.println("Stoc actualizat");
        }else{
            System.out.println("Nu poti adauga cantitate negativa");
        }
    }

    public void decreaseStock(int amount){
        if(this.stock>=amount){
            this.stock-=amount;
            System.out.println("Stoc actualizat");
        }else{
            System.out.println("Stoc insuficient");
        }
    }

    public boolean getReqPrescription(){
        return reqPrescription;
    }
}
