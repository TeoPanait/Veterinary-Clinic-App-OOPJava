package service;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

// singleton pattern: audit service ensures single instance for consistent logging
// writes all system actions to CSV file with timestamp for compliance and traceability
public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE ="audit.csv";
    
    // private constructor: lazy initialization for singleton
    // initializes the audit file on first use
    private AuditService() {
        initFileIfNeeded();
    }
    
    // return the shared audit service instance
    // lazy initialization: creates instance only when first called
    public static synchronized AuditService getInstance() {
        if(instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    // create the audit file with header if it does not exist
    // header format: "nume_actiune,timestamp"
    private void initFileIfNeeded() {
        File file = new File(AUDIT_FILE);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("nume_actiune,timestamp");
                bw.newLine();
            } catch (IOException e) {
                System.err.println("Eroare la initializarea fisierului de audit: " + e.getMessage());
            }
        }
    }

    // log an action: write action name and current timestamp to CSV file
    // append mode (true parameter) adds to existing file without overwriting
    public void logAction(String actionName) {
        String line = actionName + "," + LocalDateTime.now();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Eroare la scriere audit CSV: " + e.getMessage());
        }
    }

}
