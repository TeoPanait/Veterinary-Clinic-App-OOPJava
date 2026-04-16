import java.time.LocalDate;

public interface ClinicOperations {

    void addClient(Client client);
    void addPet(Pet patient);

    void scheduleAppointment(Appointment appointment);
    void cancelAppointment(int appointmentId);

    void addMedicalRecord(MedicalRecord record);
    void performTreatment(int petId, Treatment treatment);
    void updateDiagnosis(int recordId, String newDiagnosis);

    void addMedicationToInventory(Medication medication);
    void restockMedication(String medName, int quantity);
    void  prescribeMedication(int recordId, String medName, int quantity);

    double calculateTotalBill(int recordId);

    void viewAllPatients();
    void viewAvailableMeds();
    void viewAppForDate(LocalDate date);
    void getMedicalHistory(int petId);
}
