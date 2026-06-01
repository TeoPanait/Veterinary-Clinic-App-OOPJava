package app;

import model.*;
import repository.*;

import java.time.LocalDateTime;

public class DaoDemoMain {
    public static void main(String[] args) {
        ClientDao clientDao = new ClientDao();
        VeterinarianDao vetDao = new VeterinarianDao();
        TreatmentDao treatmentDao = new TreatmentDao();
        MedicationDao medicationDao = new MedicationDao();
        PetDao petDao = new PetDao();
        AppointmentDao appointmentDao = new AppointmentDao();
        MedicalRecordDao medicalRecordDao = new MedicalRecordDao();

        String uniqueEmail = "ana_" + System.currentTimeMillis() + "@mail.com";

        // 1) INSERT CLIENT
        Client client = new Client("Ana", "Ionescu", "0711111111", uniqueEmail, "Strada A");
        int clientId = clientDao.insert(client);
        System.out.println("Client inserted id = " + clientId);
        System.out.println(clientDao.findByIdBasic(clientId));

        // 2) INSERT VET
        Veterinarian vet = new Veterinarian("Maria", "Pop", "0722222222", "Dogs");
        int vetId = vetDao.insert(vet);
        System.out.println("Vet inserted id = " + vetId);
        System.out.println(vetDao.findByIdBasic(vetId));

        // 3) INSERT TREATMENT
        Treatment treatment = new Treatment("Consultatie", "Control de rutina", 70.0);
        int treatmentId = treatmentDao.insert(treatment);
        System.out.println("Treatment inserted id = " + treatmentId);
        System.out.println(treatmentDao.findByIdBasic(treatmentId));

        // 4) INSERT MEDICATION
        int medicationId = medicationDao.insert("Bravetco", "10 mg", 100.0, 20, true);
        System.out.println("Medication inserted id = " + medicationId);
        System.out.println(medicationDao.findByIdBasic(medicationId));

        // 5) INSERT PETS
        Dog dog = new Dog("Rex", 4, 21.3, client, "Labrador", "MEDIUM");
        Cat cat = new Cat("Miti", 2, 4.8, client, "European", "Short", true);

        int dogId = petDao.insertDog(dog, clientId);
        int catId = petDao.insertCat(cat, clientId);

        System.out.println("Dog inserted id = " + dogId);
        System.out.println("Cat inserted id = " + catId);
        System.out.println(petDao.findByIdBasic(dogId));
        System.out.println(petDao.findByIdBasic(catId));

        // 6) INSERT APPOINTMENT
        int appointmentId = appointmentDao.insert(
                dogId,
                vetId,
                LocalDateTime.now().plusDays(1),
                "Control",
                "PROG"
        );
        System.out.println("Appointment inserted id = " + appointmentId);
        System.out.println(appointmentDao.findByIdBasic(appointmentId));

        // 7) INSERT MEDICAL RECORD
        int recordId = medicalRecordDao.insert(
                dogId,
                vetId,
                "Sanatos",
                treatmentId
        );
        System.out.println("Medical record inserted id = " + recordId);
        System.out.println(medicalRecordDao.findByIdBasic(recordId));

        // 8) FIND ALL
        System.out.println("\n=== ALL CLIENTS ===");
        for (String row : clientDao.findAllBasic()) {
            System.out.println(row);
        }

        System.out.println("\n=== ALL VETS ===");
        for (String row : vetDao.findAllBasic()) {
            System.out.println(row);
        }

        System.out.println("\n=== ALL TREATMENTS ===");
        for (String row : treatmentDao.findAllBasic()) {
            System.out.println(row);
        }

        System.out.println("\n=== ALL MEDICATIONS ===");
        for (String row : medicationDao.findAllBasic()) {
            System.out.println(row);
        }

        System.out.println("\n=== ALL PETS ===");
        for (String row : petDao.findAllBasic()) {
            System.out.println(row);
        }

        System.out.println("\n=== ALL APPOINTMENTS ===");
        for (String row : appointmentDao.findAllBasic()) {
            System.out.println(row);
        }

        System.out.println("\n=== ALL MEDICAL RECORDS ===");
        for (String row : medicalRecordDao.findAllBasic()) {
            System.out.println(row);
        }

        // 9) UPDATE
        Client updatedClient = new Client("Matei", "Gugiu", "0799999988", uniqueEmail, "Strada Noua");
        System.out.println("Client update: " + clientDao.update(clientId, updatedClient));

        Veterinarian updatedVet = new Veterinarian("Carina", "Pele", "0733333344", "Dogs");
        System.out.println("Vet update: " + vetDao.update(vetId, updatedVet));

        Treatment updatedTreatment = new Treatment("Consultatie extinsa", "Control complet", 90.0);
        System.out.println("Treatment update: " + treatmentDao.update(treatmentId, updatedTreatment));

        System.out.println("Medication update: " + medicationDao.update(medicationId, 120.0, 25));

        System.out.println("Pet update: " + petDao.updatePet(dogId, "Rex Updated", 5, 22.5));
        System.out.println("Dog update: " + petDao.updateDog(dogId, "Golden Retriever", "LARGE"));
        System.out.println("Cat update: " + petDao.updateCat(catId, "European Updated", "Long", false));

        System.out.println("Appointment update: " + appointmentDao.update(
                appointmentId,
                LocalDateTime.now().plusDays(2),
                "Consultatie schimbata",
                "RESCH"
        ));

        System.out.println("Medical record update: " + medicalRecordDao.update(
                recordId,
                "Tratament finalizat",
                treatmentId
        ));

        // 10) RE-READ AFTER UPDATE
        System.out.println("\n=== AFTER UPDATE ===");
        System.out.println(clientDao.findByIdBasic(clientId));
        System.out.println(vetDao.findByIdBasic(vetId));
        System.out.println(treatmentDao.findByIdBasic(treatmentId));
        System.out.println(medicationDao.findByIdBasic(medicationId));
        System.out.println(petDao.findByIdBasic(dogId));
        System.out.println(petDao.findByIdBasic(catId));
        System.out.println(appointmentDao.findByIdBasic(appointmentId));
        System.out.println(medicalRecordDao.findByIdBasic(recordId));

        // 11) DELETE in ordine corecta
        System.out.println("\n=== DELETES ===");
        System.out.println("Delete medical record: " + medicalRecordDao.delete(recordId));
        System.out.println("Delete appointment: " + appointmentDao.delete(appointmentId));

        // pet-ul trebuie sters dupa ce ai sters record/appointment
        System.out.println("Delete pet dog: " + petDao.deletePet(dogId));
        System.out.println("Delete pet cat: " + petDao.deletePet(catId));

        System.out.println("Delete medication: " + medicationDao.delete(medicationId));
        System.out.println("Delete treatment: " + treatmentDao.delete(treatmentId));
        System.out.println("Delete vet: " + vetDao.delete(vetId));
        System.out.println("Delete client: " + clientDao.delete(clientId));
    }
}
