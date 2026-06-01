package model;

import java.time.LocalDateTime;

// builder pattern: fluent api for constructing appointment objects
// separates object construction from representation, allowing step-by-step creation
public class AppointmentBuilder {
    private LocalDateTime dateTime;
    private Pet patient;
    private Veterinarian vet;
    private String reason;
    private String status;

    // fluent methods: each setter returns 'this' to allow method chaining
    public AppointmentBuilder setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public AppointmentBuilder setPatient(Pet patient) {
        this.patient = patient;
        return this;
    }

    public AppointmentBuilder setVet(Veterinarian vet) {
        this.vet = vet;
        return this;
    }

    public AppointmentBuilder setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public AppointmentBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    // construct and return the final appointment object
    public Appointment build() {
        return new Appointment(dateTime, patient, vet, reason, status);
    }

}

