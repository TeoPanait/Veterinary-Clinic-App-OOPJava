import java.util.*;
import java.time.LocalDateTime;

public class MedicalRecord {
    private static int idCounter=1;

    private int idRecord;
    private Pet patient;
    private Veterinarian vet;

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    private String diagnosis;
    private Treatment appliedTreatment;
    private List<Medication> prescribedMeds;
    private LocalDateTime dateTime;

    public void setAppliedTreatment(Treatment appliedTreatment) {
        this.appliedTreatment = appliedTreatment;
    }

    MedicalRecord(Pet patient, Veterinarian vet, String diagnosis,
                  Treatment appliedTreatment, List<Medication> prescribedMeds, LocalDateTime dateTime){
        this.idRecord=idCounter++;
        this.patient=patient;
        this.vet=vet;
        this.diagnosis=diagnosis;
        this.appliedTreatment=appliedTreatment;
        this.prescribedMeds=new ArrayList<>();
        this.dateTime=dateTime;
    }

    public Pet getPatient(){
        return patient;
    }

    public int getIdRecord(){
        return idRecord;
    }

    public Veterinarian getVet(){
        return vet;
    }

    public String getDiagnosis(){
        return diagnosis;
    }

    public Treatment getAppliedTreatment(){
        return appliedTreatment;
    }

    public void addMedication(Medication med){
        this.prescribedMeds.add(med);
    }

    public List<Medication> getPrescribedMeds(){
        return prescribedMeds;
    }

    public LocalDateTime getDateTime() { return dateTime; }
}
