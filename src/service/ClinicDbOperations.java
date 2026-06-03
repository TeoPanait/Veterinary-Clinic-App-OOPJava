package service;

import java.time.LocalDateTime;
import java.util.List;

public interface ClinicDbOperations {

    List<String> showClients();
    List<String> showPets();
    List<String> showVeterinarians();
    List<String> showMedications();
    List<String> showAppointments();
    List<String> getPrescriptionsForRecord(int recordId);

    int addClient(String name, String surname, String phone, String email, String address);
    int addDog(int ownerId, String petName, int age, double weight, String breed, String size);
    int addCat(int ownerId, String petName, int age, double weight, String breed, String furType, boolean indoor);
    int addMedication(String medName, String dosage, double price, int stock, boolean reqPrescription);
    int addAppointment(int petId, int vetId, LocalDateTime dateTime, String reason, String status);
    int addMedicalRecord(int petId, int vetId, String diagnosis, int treatmentId);

    int prescribeMedication(int recordId, int medicationId, int quantity);
}