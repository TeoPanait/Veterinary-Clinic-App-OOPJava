package app;

import model.*;
import service.ClinicDbService;
import service.ClinicDbOperations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClinicDbOperations clinicService = ClinicDbService.getInstance();

        while (true) {
            System.out.println("\n=== VET CLINIC MENU ===");
            System.out.println("1. Show clients");
            System.out.println("2. Show pets");
            System.out.println("3. Show veterinarians");
            System.out.println("4. Show medications");
            System.out.println("5. Add client");
            System.out.println("6. Add pet");
            System.out.println("7. Add medication");
            System.out.println("8. Add appointment");
            System.out.println("9. Show appointments");
            //System.out.println("10. Add medical record");
            //System.out.println("11. Prescribe medication");
            //System.out.println("12. Show prescriptions for record");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
                continue;
            }

            switch (option) {
                case 1 -> {
                    List<String> clients = clinicService.showClients();
                    if (clients.isEmpty()) {
                        System.out.println("No clients found.");
                    } else {
                        System.out.println("=== CLIENTS ===");
                        for (String row : clients) {
                            System.out.println(row);
                        }
                    }
                }
                case 2 -> {
                    List<String> pets = clinicService.showPets();
                    if (pets.isEmpty()) {
                        System.out.println("No pets found.");
                    } else {
                        System.out.println("=== PETS ===");
                        for (String row : pets) {
                            System.out.println(row);
                        }
                    }
                }
                case 3 -> {
                    List<String> vets = clinicService.showVeterinarians();
                    if (vets.isEmpty()) {
                        System.out.println("No veterinarians found.");
                    } else {
                        System.out.println("=== VETERINARIANS ===");
                        for (String row : vets) {
                            System.out.println(row);
                        }
                    }
                }
                case 4 -> {
                    List<String> meds = clinicService.showMedications();
                    if (meds.isEmpty()) {
                        System.out.println("No medications found.");
                    } else {
                        System.out.println("=== MEDICATIONS ===");
                        for (String row : meds) {
                            System.out.println(row);
                        }
                    }
                }
                case 5 -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Surname: ");
                    String surname = scanner.nextLine();

                    System.out.print("Phone number: ");
                    String phone = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Address: ");
                    String address = scanner.nextLine();

                    int id = clinicService.addClient(name, surname, phone, email, address);
                    System.out.println("Client inserted with id = " + id);
                }
                case 6 -> {
                    System.out.print("Owner client id: ");
                    int ownerId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Pet type (DOG/CAT): ");
                    PetType type = PetType.valueOf(scanner.nextLine().toUpperCase());

                    System.out.print("Pet name: ");
                    String petName = scanner.nextLine();

                    System.out.print("Age: ");
                    int age = Integer.parseInt(scanner.nextLine());

                    System.out.print("Weight: ");
                    double weight = Double.parseDouble(scanner.nextLine());

                    if (type == PetType.DOG) {
                        System.out.print("Dog breed: ");
                        String breed = scanner.nextLine();

                        System.out.print("Size: ");
                        String size = scanner.nextLine();

                        int petId = clinicService.addDog(ownerId, petName, age, weight, breed, size);
                        System.out.println("Dog inserted with id = " + petId);
                    } else {
                        System.out.print("Cat breed: ");
                        String breed = scanner.nextLine();

                        System.out.print("Fur type: ");
                        String furType = scanner.nextLine();

                        System.out.print("Indoor (true/false): ");
                        boolean indoor = Boolean.parseBoolean(scanner.nextLine());

                        int petId = clinicService.addCat(ownerId, petName, age, weight, breed, furType, indoor);
                        System.out.println("Cat inserted with id = " + petId);
                    }
                }
                case 7 -> {
                    System.out.print("Medication name: ");
                    String medName = scanner.nextLine();

                    System.out.print("Dosage: ");
                    String dosage = scanner.nextLine();

                    System.out.print("Price: ");
                    double price = Double.parseDouble(scanner.nextLine());

                    System.out.print("Stock: ");
                    int stock = Integer.parseInt(scanner.nextLine());

                    System.out.print("Requires prescription (true/false): ");
                    boolean req = Boolean.parseBoolean(scanner.nextLine());

                    int id = clinicService.addMedication(medName, dosage, price, stock, req);
                    System.out.println("Medication inserted with id = " + id);
                }
                case 8 -> {
                    System.out.print("Pet database id: ");
                    int petId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Vet database id: ");
                    int vetId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Appointment date-time (yyyy-MM-ddTHH:mm) or date only (yyyy-MM-dd): ");
                    LocalDateTime dateTime = parseDateTimeInput(scanner.nextLine());

                    System.out.print("Reason: ");
                    String reason = scanner.nextLine();

                    System.out.print("Status: ");
                    String status = scanner.nextLine();

                    int appointmentDbId = clinicService.addAppointment(petId, vetId, dateTime, reason, status);
                    System.out.println("Appointment inserted with id = " + appointmentDbId);
                }
                case 9 -> {
                    List<String> appointments = clinicService.showAppointments();
                    if (appointments.isEmpty()) {
                        System.out.println("No appointments found.");
                    } else {
                        System.out.println("=== APPOINTMENTS ===");
                        for (String row : appointments) {
                            System.out.println(row);
                        }
                    }
                }
                case 10 -> {
                    System.out.print("Pet id: ");
                    int petId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Vet id (or 1): ");
                    int vetId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Diagnosis: ");
                    String diagnosis = scanner.nextLine();

                    System.out.print("Treatment id (or 4): ");
                    int treatmentId = Integer.parseInt(scanner.nextLine());

                    int recId = clinicService.addMedicalRecord(petId, vetId == 0 ? 0 : vetId, diagnosis, treatmentId == 0 ? -1 : treatmentId);
                    System.out.println("Medical record inserted with id = " + recId);
                }
                case 11 -> {
                    System.out.print("Medical record id: ");
                    int recordId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Medication id: ");
                    int medicationId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Quantity: ");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    try {
                        int prescId = clinicService.prescribeMedication(recordId, medicationId, quantity);
                        System.out.println("Prescription inserted with id = " + prescId);
                    } catch (RuntimeException ex) {
                        System.out.println("Error prescribing medication: " + ex.getMessage());
                    }
                }
                case 12 -> {
                    System.out.print("Medical record id: ");
                    int recordId = Integer.parseInt(scanner.nextLine());
                    var rows = clinicService.getPrescriptionsForRecord(recordId);
                    if (rows == null || rows.isEmpty()) {
                        System.out.println("No prescriptions found for record " + recordId);
                    } else {
                        System.out.println("=== PRESCRIPTIONS for record " + recordId + " ===");
                        rows.forEach(System.out::println);
                    }
                }
                case 0 -> {
                    System.out.println("Bye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static LocalDateTime parseDateTimeInput(String input) {
        String value = input == null ? "" : input.trim();
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ex) {
            return LocalDate.parse(value).atTime(9, 0);
        }
    }
}