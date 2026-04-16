import java.time.LocalDateTime;
import java.time.LocalDate;
void main() {
    Service clinic= new Service();
    System.out.println("CLINIC OPEN");

    Client client1=new Client("Ion", "Popescu", "07233367617", "ion.popescu@gmail.com", "drumul pacii 7");
    Dog dog1= new Dog("Marius", 3, 45.3, client1, "CIOBANESC", "BIG");
    Veterinarian vet1= new Veterinarian("Maria", "Cojocariu", "0738285218", "Dogs");

    clinic.addClient(client1);
    clinic.addPet(dog1);

    Medication med1= new Medication("Bravetco", "10 mg", 100.0, 10, true);
    Treatment t1= new Treatment("Consultatie", "Control de rutina", 70.0);

    clinic.addMedicationToInventory(med1);

    System.out.println("\n Making an appointment");
    Appointment app1= new Appointment(LocalDateTime.now().plusDays(2), dog1,  vet1, "control");
    clinic.scheduleAppointment(app1);

    System.out.println("\n appointment");
    MedicalRecord record1 = new MedicalRecord(dog1, vet1 , "sanatos", t1, null, LocalDateTime.now());
    clinic.addMedicalRecord(record1);

    record1.setAppliedTreatment(t1);
    clinic.performTreatment(dog1.getId(), t1);

    int idRecord = record1.getIdRecord();

    clinic.updateDiagnosis(idRecord, "Infestatie cu capuse");

    System.out.println("\n Pharmacy ");
    try{
    clinic.prescribeMedication(idRecord, "Bravetco", 40);
    }catch(OutOfStockException e){
        System.out.println(e.getMessage());
    }

    System.out.println("\nPayment");
    clinic.calculateTotalBill(idRecord);

    System.out.println("\n About us ");
    clinic.viewAllPatients();
    clinic.viewAvailableMeds();
    System.out.println( "\nAppointments : ");
    clinic.viewAppForDate(LocalDate.now().plusDays(1));

    clinic.getMedicalHistory(dog1.getId());

    try{
        clinic.performTreatment(10, t1);
    }catch (PetNotFoundException e){
        System.out.println(e.getMessage());
    }

}
