import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private static int idCounter=1;
    private int appointmentId;
    private LocalDateTime dateTime;
    private Pet patient;
    private Veterinarian vet;
    private String reason;

    private String status;

    public Appointment(LocalDateTime dateTime, Pet patient, Veterinarian vet,
                       String reason){
        this.appointmentId=idCounter++;
        this.dateTime=dateTime;
        this.patient=patient;
        this.reason=reason;
        this.status="PROG";
    }
    public int getAppointmentId() { return appointmentId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Pet getPatient() { return patient; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }

    public void setDateTime(LocalDateTime newDateTime){
        this.dateTime=newDateTime;
        System.out.println("The appointment was moved to: "+ newDateTime);

    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getFormattedDateTime(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return this.dateTime.format(formatter);
    }
//    @Override
//    public int compareTo(Appointment other){
//        return this.dateTime.compareTo(other.dateTime);
//    }
}
