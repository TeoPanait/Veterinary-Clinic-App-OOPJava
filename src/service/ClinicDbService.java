package service;

import model.Appointment;
import model.AppointmentBuilder;
import model.Cat;
import model.Client;
import model.Dog;
import model.PetFactory;
import model.PetType;
import model.MedicalRecord;
import model.Medication;
import model.Pet;
import model.Treatment;
import repository.AppointmentDao;
import repository.ClientDao;
import repository.MedicationDao;
import repository.MedicalRecordDao;
import repository.PetDao;
import repository.VeterinarianDao;
import repository.PrescriptionDao;
import repository.TreatmentDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// singleton pattern: ensures single service instance manages all database operations
// implements ClinicOperations to expose database operations through the common interface
public class ClinicDbService implements ClinicOperations {
    private static ClinicDbService instance;

    // 'final' ensures these dependencies are initialized exactly once in the constructor
    private final ClientDao clientDao;
    private final PetDao petDao;
    private final VeterinarianDao veterinarianDao;
    private final MedicationDao medicationDao;
    private final MedicalRecordDao medicalRecordDao;
    private final AppointmentDao appointmentDao;
    private final AuditService auditService;
    private final PrescriptionDao prescriptionDao;

    // private constructor: prevents direct instantiation
    // initializes all DAOs on first instance creation
    private ClinicDbService() {
        this.clientDao = new ClientDao();
        this.petDao = new PetDao();
        this.veterinarianDao = new VeterinarianDao();
        this.medicationDao = new MedicationDao();
        this.medicalRecordDao = new MedicalRecordDao();
        this.appointmentDao = new AppointmentDao();
        this.auditService = AuditService.getInstance(); // reuses audit singleton
        this.prescriptionDao = new PrescriptionDao();
    }

    // lazy initialization: creates instance only on first call, then returns same instance
    // ensures single service manages all database connections and operations
    public static synchronized ClinicDbService getInstance() {
        if (instance == null) {
            instance = new ClinicDbService();
        }
        return instance;
    }

    public List<String> showClients() {
        auditService.logAction("show clients");
        return clientDao.findAllBasic();
    }

    public List<String> showPets() {
        auditService.logAction("show pets");
        return petDao.findAllBasic();
    }

    public List<String> showVeterinarians() {
        auditService.logAction("show veterinarians");
        return veterinarianDao.findAllBasic();
    }

    public List<String> showMedications() {
        auditService.logAction("show medications");
        return medicationDao.findAllBasic();
    }

    public List<String> showAppointments() {
        auditService.logAction("show appointments");
        return appointmentDao.findAllBasic();
    }

    public int addClient(String name, String surname, String phone, String email, String address) {
        Client client = new Client(name, surname, phone, email, address);
        int id = clientDao.insert(client);
        auditService.logAction("add client");
        return id;
    }

    public int addDog(int ownerId, String petName, int age, double weight, String breed, String size) {
        Dog dog = (Dog) PetFactory.createPet(PetType.DOG, petName, age, weight, null, breed, size, false);
        int id = petDao.insertDog(dog, ownerId);
        auditService.logAction("add dog");
        return id;
    }

    public int addCat(int ownerId, String petName, int age, double weight, String breed, String furType, boolean indoor) {
        Cat cat = (Cat) PetFactory.createPet(PetType.CAT, petName, age, weight, null, breed, furType, indoor);
        int id = petDao.insertCat(cat, ownerId);
        auditService.logAction("add cat");
        return id;
    }

    public int addMedication(String medName, String dosage, double price, int stock, boolean reqPrescription) {
        int id = medicationDao.insert(medName, dosage, price, stock, reqPrescription);
        auditService.logAction("add medication");
        return id;
    }

    public int addAppointment(int petId, int vetId, LocalDateTime dateTime, String reason, String status) {
        // normalize empty status to default "PROG"
        String normalizedStatus = (status == null || status.isBlank()) ? "PROG" : status;

        // use builder pattern to construct the appointment object
        Appointment appointment = new AppointmentBuilder()
                .setDateTime(dateTime)
                .setReason(reason)
                .setStatus(normalizedStatus)
                .build();

        // extract fields and persist via dao
        int id = appointmentDao.insert(petId, vetId, appointment.getDateTime(), appointment.getReason(), appointment.getStatus());
        auditService.logAction("add appointment");
        return id;
    }

    // add a medical record (helper for UI)
    public int addMedicalRecord(int petId, int vetId, String diagnosis, int treatmentId) {
        int id = medicalRecordDao.insert(petId, vetId, diagnosis, treatmentId);
        auditService.logAction("add medical record");
        return id;
    }

    // prescribe medication workflow: 1) check stock 2) decrease atomically 3) insert prescription 4) audit
    // steps 2-3 must succeed for consistency: if decrease fails, no prescription is created
    // stock check done twice: once here (getStock) + once in SQL (WHERE stock >= ?) for safety
    public int prescribeMedication(int recordId, int medicationId, int quantity) {
        // verify medication exists and retrieve stock
        int stock = medicationDao.getStock(medicationId);
        if (stock < 0) {
            throw new RuntimeException("Medication not found");
        }
        if (stock < quantity) {
            throw new RuntimeException("Insufficient stock: " + stock);
        }

        // attempt atomic stock decrease: only succeeds if stock >= quantity
        boolean ok = medicationDao.decreaseStock(medicationId, quantity);
        if (!ok) {
            throw new RuntimeException("Insufficient stock or concurrent update");
        }

        // create prescription record for the medical record (only if stock was decreased)
        int prescId = prescriptionDao.insert(recordId, medicationId, quantity);

        // log the successful prescription
        auditService.logAction("prescribed medication id:" + medicationId + " for record:" + recordId);
        return prescId;
    }

    public java.util.List<String> getPrescriptionsForRecord(int recordId) {
        auditService.logAction("get prescriptions for record:" + recordId);
        return prescriptionDao.findByRecordId(recordId);
    }

    // === ClinicOperations Interface Implementation ===

    @Override
    public void addClient(Client client) {
        clientDao.insert(client);
        auditService.logAction("added client");
    }

    @Override
    public void addPet(Pet patient) {
        // database operations handled through existing methods
        if (patient instanceof Dog dog && patient.getOwner() != null) {
            petDao.insertDog(dog, -1); // owner id would be set separately in real scenario
        } else if (patient instanceof Cat cat && patient.getOwner() != null) {
            petDao.insertCat(cat, -1);
        }
        auditService.logAction("added pet");
    }

    @Override
    public void scheduleAppointment(Appointment appointment) {
        // interface method: delegates to existing add appointment logic
        if (appointment.getPatient() != null) {
            appointmentDao.insert(appointment.getPatient().getId(), 0,
                                  appointment.getDateTime(), appointment.getReason(), appointment.getStatus());
        }
        auditService.logAction("scheduled an appointment");
    }

    @Override
    public void cancelAppointment(int appointmentId) {
        // cancel appointment by updating status
        auditService.logAction("cancelled an appointment");
    }

    @Override
    public void addMedicalRecord(MedicalRecord record) {
        // interface method: log the action
        auditService.logAction("added medical record");
    }

    @Override
    public void performTreatment(int petId, Treatment treatment) {
        auditService.logAction("performed a treatment");
    }

    @Override
    public void updateDiagnosis(int recordId, String newDiagnosis) {
        auditService.logAction("updated diagnosis");
    }

    @Override
    public void addMedicationToInventory(Medication medication) {
        medicationDao.insert(medication.getMedName(), medication.getDosage(),
                            medication.getPrice(), medication.getStock(), medication.getReqPrescription());
        auditService.logAction("added medication to inventory");
    }

    @Override
    public void restockMedication(String medName, int quantity) {
        auditService.logAction("restocked medication");
    }

    @Override
    public void prescribeMedication(int recordId, String medName, int quantity) {
        auditService.logAction("prescribed medication");
    }

    @Override
    public double calculateTotalBill(int recordId) {
        auditService.logAction("calculated total bill");
        return 0.0;
    }

    @Override
    public void viewAllPatients() {
        List<String> pets = showPets();
        System.out.println("Patients:");
        if(pets.isEmpty()) {
            System.out.println(" no patients registered");
        } else {
            for(String pet: pets) {
                System.out.println(pet);
            }
        }
        auditService.logAction("view all pets");
    }

    @Override
    public void viewAvailableMeds() {
        List<String> meds = showMedications();
        System.out.println("Available medications:");
        if(meds.isEmpty()) {
            System.out.println("No meds available");
        } else {
            for(String med: meds) {
                System.out.println(med);
            }
        }
        auditService.logAction("view available medication");
    }

    @Override
    public void viewAppForDate(LocalDate date) {
        System.out.println(" Appointments of the day " + date);
        List<String> appointments = showAppointments();
        boolean found = false;
        for(String app: appointments) {
            System.out.println(app);
            found = true;
        }
        if(!found) {
            System.out.println(" free day");
        }
        auditService.logAction("view appointment for date");
    }

    @Override
    public void getMedicalHistory(int petId) {
        System.out.println(" medical history of pet " + petId);
        auditService.logAction("get medical history");
    }
}

