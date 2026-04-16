import java.time.LocalDate;
import java.util.*;
public class Service implements ClinicOperations {
    private List<Client> clients;
    private Map<Integer, Pet> pets;
    private Set<Appointment> appointments;
    private Map<Integer, MedicalRecord> medicalRecords;
    private Map<String, Medication> inventory;

    public Service(){
        this.clients=new ArrayList<>();
        this.pets=new HashMap<>();
        this.appointments=new HashSet<>();
        this.medicalRecords= new HashMap<>();
        this.inventory= new HashMap<>();
    }

    @Override
    public void addClient(Client client){

        clients.add(client);
        System.out.println("Client " + client.getName()+ " " + client.getSurname()+ " was added");

    }

    @Override
    public void addPet(Pet patient){

        pets.put(patient.getId(), patient);
        System.out.println(" Pacient " + patient.getNamePet() + " was registered");

    }

    @Override
    public void scheduleAppointment(Appointment appointment){
        appointments.add(appointment);
        System.out.println(" Appointment registered for : " + appointment.getFormattedDateTime());
    }

    @Override
    public void cancelAppointment(int appointmentId){
        for(Appointment app : appointments){
            if(app.getAppointmentId() == appointmentId){
                app.setStatus("Canceled");
                System.out.println(" Appointment with id:" + appointmentId+ " was canceled");
                return;
            }
        }
        System.out.println("Error, appointment not found");
    }

    @Override
    public void addMedicalRecord(MedicalRecord record){
        medicalRecords.put(record.getIdRecord(), record);
        System.out.println("Medical record with id:" + record.getIdRecord() + " added");
    }

    @Override
    public void performTreatment(int petId, Treatment treatment){
        Pet pet=pets.get(petId);
        if(pet!= null){
            System.out.println("Vet applied treatment '"+ treatment.getName() + "' on pet "+ pet.getNamePet());
        }else {
            //System.out.println("This pet doesn't exist");
            throw new PetNotFoundException("Erorr: Pet with id "+ petId+ " does not exist");
        }
    }

    @Override
    public void updateDiagnosis(int recordId, String newDiagnosis){
        MedicalRecord record= medicalRecords.get(recordId);
        if(record!= null){
            record.setDiagnosis(newDiagnosis);
            System.out.println("Diagnosis updated for record: "+ record.getIdRecord());
        }else{
            System.out.println("Error: Medical record doesn't exist");
        }
    }

    @Override
    public void addMedicationToInventory(Medication medication){
        inventory.put(medication.getMedName(), medication);
        System.out.println("Medication " + medication.getMedName() + " was added to inventory");
    }

    @Override
    public void restockMedication(String medName, int quantity){
        Medication med=inventory.get(medName);
        if(med!=null){
            med.addStock(quantity);
        }else {
            System.out.println("Error: Medication '" + medName+ "' doesn't exist ");
        }
    }

    @Override
    public void prescribeMedication(int recordId, String medName, int quantity){
        MedicalRecord record=medicalRecords.get(recordId);
        Medication med= inventory.get(medName);

        if(record != null && med !=null){
            if(med.getStock() < quantity){
                throw new OutOfStockException("Insufficient stock for med: " + medName + ",  " +med.getStock() +" left");
            }
            med.decreaseStock(quantity);
            record.addMedication(med);
            System.out.println(quantity + " " + medName + " were prescribed");
        }else {
            System.out.println("Error: Medical record or medication doesn't exist");
        }
    }

    @Override
    public double calculateTotalBill(int recordId){
        MedicalRecord record= medicalRecords.get(recordId);
        if(record == null){
            System.out.println("medical record doesn't exist");
            return 0.0;
        }
        double total=0.0;
        if(record.getAppliedTreatment()!=null){
            total+=record.getAppliedTreatment().getCost();
        }
        for(Medication med : record.getPrescribedMeds()){
            total+= med.getPrice();
        }
        System.out.println("the total cost for the medical record: " + recordId + " is "+ total);
        return total;
    }

    @Override
    public void viewAllPatients(){
        System.out.println("Patients:");
        if(pets.isEmpty()){
            System.out.println(" no patients registered");
            return;
        }
        for(Pet pet: pets.values()){
            System.out.println("id: "+ pet.getId()+ " , name: "+ pet.getNamePet());
        }

    }

    @Override
    public void viewAvailableMeds(){
        if(inventory.isEmpty()){
            System.out.println("No meds available");
            return;
        }
        for(Medication med: inventory.values()){
            if(med.getStock() > 0){
                System.out.println("medication: "+ med.getMedName() + ", stock: " + med.getStock());
            }
        }

    }

    @Override
    public void viewAppForDate(LocalDate date) {
        System.out.println(" Appointments of the day " + date );
        boolean found=false;

        for(Appointment app : appointments){
            if(app.getDateTime().toLocalDate().equals(date)){
                System.out.println("time : " + app.getDateTime().toLocalTime() + ", patient : "+ app.getPatient().getNamePet() + ", reason: "+ app.getReason());
                found=true;
            }
        }
        if(!found){
            System.out.println(" free day");
        }
    }

    @Override
    public void getMedicalHistory(int petId){
        System.out.println(" medical history of pet " + petId);
        boolean found=false;

        for(MedicalRecord record: medicalRecords.values()){
            if(record.getPatient().getId() == petId){
                System.out.println("Date : " +record.getDateTime().toLocalDate() + ", diagnosis: "+ record.getDiagnosis());
                found=true;
            }
        }
        if(!found){
            System.out.println(" no medical record available for pet "+ petId);
        }
    }
}
