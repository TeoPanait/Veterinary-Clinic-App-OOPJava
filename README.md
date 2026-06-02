# Vet Clinic Management System - Project Overview

## Overview

The **Vet Clinic Management System** is a Java application developed to support the daily operations of a veterinary clinic. It provides a structured environment for managing clients, pets, veterinarians, appointments, medical records, treatments, medications, and prescriptions.

The project was designed as an object-oriented system and extended with:

- JDBC persistence using PostgreSQL
- a CSV-based audit mechanism
- a console interface
- a JavaFX graphical interface
- design patterns such as Singleton, Factory, and Builder

The application demonstrates how a layered software solution can be used to model a realistic clinic workflow while keeping business rules, persistence, and presentation concerns separated.

---

## 1. Business Context

The system simulates the daily activity of a veterinary clinic. Its purpose is to help clinic staff manage the complete flow of operations, from client registration and pet tracking to appointments, medical treatment, and medication prescriptions.

The main business goals are:

- organizing clinic activity in a consistent way
- maintaining traceability of important actions
- supporting medication inventory management
- ensuring that prescriptions are created only when stock is available
- offering both console-based and graphical interaction with the system

---

## 2. Application Flow

The typical workflow of the application follows these steps:

1. A client is registered in the system.
2. One or more pets are associated with that client.
3. A veterinarian is assigned to a consultation or appointment.
4. A medical record is created for the pet.
5. A treatment may be linked to the medical record.
6. Medications are added to the clinic inventory.
7. A prescription is created for a medical record.
8. The stock of the corresponding medication is decreased automatically.
9. All important operations are recorded in the audit log.

This flow reflects the way a real clinic can move from registration to treatment and prescription management.

---

## 3. Domain Model

The project includes several domain concepts that represent the clinic structure and its daily operations.

### Core entities
The main entities cover:

- clients and their contact information
- pets and pet specializations
- veterinarians and their professional data
- appointments and consultation details
- medical records with diagnosis and treatment data
- medications and their stock information
- prescriptions that link medical records to medications

### Supporting classes
The model is completed by support classes used for:

- inheritance and specialization
- object creation
- appointment construction
- audit logging
- business logic orchestration

The domain model was intentionally designed to show:

- encapsulation
- inheritance
- composition
- separation of responsibilities

---

## 4. Design Patterns Used

The project demonstrates several design patterns that are appropriate for its structure:

### Singleton
Used for shared services that should have a single instance:

- `AuditService`
- `ClinicDbService`

### Factory
Used for creating pet objects based on type:

- `PetFactory`

### Builder
Used for constructing appointment objects in a controlled way:

- `AppointmentBuilder`

These patterns improve readability, reduce duplication, and support maintainable design.

---

## 5. Persistence Layer

The application uses **PostgreSQL** and **JDBC** for data persistence.

### Main Tables

The database includes the following tables:

- `clients`
- `pets`
- `dogs`
- `cats`
- `veterinarians`
- `appointments`
- `medical_records`
- `treatments`
- `medications`
- `prescriptions`

### Prescription Model

Prescriptions are stored in a separate table to keep the medication inventory independent from the medical record history.

The main relationships are:

- `medical_records` 1 --- N `prescriptions`
- `medications` 1 --- N `prescriptions`

This structure makes the data model clearer and easier to maintain.

### ERD Diagram

<img width="700" height="300" alt="image" src="https://github.com/user-attachments/assets/467f23e9-517e-48e8-8d7b-e3eb424418fb" />

<img width="700" height="700" alt="image" src="https://github.com/user-attachments/assets/dba795c4-aa92-4b05-a6d0-237925e66034" />


---

## 6. Services

### ClinicDbService
`ClinicDbService` is the main application service that exposes the business operations supported by the database-backed version of the system.

It is responsible for:

- managing clients
- managing pets
- managing veterinarians
- creating and listing appointments
- creating medical records
- managing medications
- creating prescriptions
- retrieving prescriptions for a given record
- coordinating audit logging

### AuditService
`AuditService` writes important actions to a CSV file in order to preserve traceability of the system’s activity.

The audit file stores entries in the format:

```text
nume_actiune,timestamp
```

---

## 7. Graphical Interface

The project also includes a JavaFX interface that provides a more user-friendly way of interacting with the application.

The graphical interface includes:

- one main window
- a menu bar with multiple actions
- list views for displaying data
- a form/dialog for adding a client

The GUI reuses the same service and repository layer as the console application, which ensures consistency across both interfaces.

---

## 8. Console Interface

The console version provides direct access to the main system operations through a menu-driven workflow. It is useful for quick testing, debugging, and verification of business logic.

---

## 9. Database and Terminal Commands

### Open an interactive PostgreSQL session
```powershell
psql -h localhost -U postgres -d vet_clinic
```

### Useful commands inside `psql`
```sql
\dt
\d clients
\d pets
\d medical_records
\d medications
\d prescriptions
\q
```

### Run the migration script
```powershell
psql -h localhost -U postgres -d vet_clinic -f .\migrations\001_create_prescriptions.sql
```

### Show all tables from terminal
```powershell
psql -h localhost -U postgres -d vet_clinic -c "SELECT table_name FROM information_schema.tables WHERE table_schema='public' ORDER BY table_name;"
```

### Useful verification queries
```sql
SELECT * FROM clients;
SELECT * FROM pets;
SELECT * FROM veterinarians;
SELECT * FROM appointments;
SELECT * FROM medical_records;
SELECT * FROM medications;
SELECT * FROM prescriptions;
```

### Join query for prescriptions
```sql
SELECT p.prescription_id, p.record_id, p.quantity, m.med_name, m.stock
FROM prescriptions p
JOIN medications m ON m.medication_id = p.medication_id;
```

---

## 10. Example Workflow

A typical usage scenario of the system is:

1. register a client
2. register one or more pets
3. schedule an appointment
4. create a medical record
5. add medication to inventory
6. prescribe a medication for the medical record
7. verify the prescription in the database
8. check that the medication stock has been updated

Example checks:

```sql
SELECT * FROM prescriptions;
SELECT medication_id, med_name, stock FROM medications;
```

---

## 11. Notes for Development

- Database connection details are defined in `src/repository/DbConnection.java`
- `prescriptions` is the active table used by the current implementation
- JavaFX views are implemented in separate UI classes for easier maintenance
- the same repository and service layer is used by both the console and GUI entry points

---

## 12. Summary

The **Vet Clinic Management System** is a complete Java project that demonstrates:

- object-oriented design
- inheritance and composition
- collections
- custom exceptions
- design patterns
- JDBC persistence
- audit logging
- console and graphical interfaces

The project models a realistic clinic workflow and shows how a layered architecture can be used to build a maintainable business application.
