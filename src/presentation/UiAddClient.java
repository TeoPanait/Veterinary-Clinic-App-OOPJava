package presentation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.ClientDao;
import model.Client;

import java.util.function.Consumer;

public class UiAddClient {
    // dao used to insert new clients
    private final ClientDao clientDao = new ClientDao();
    // optional callback invoked with the inserted id after successful insert
    private final Consumer<Integer> onInserted; // may be null

    public UiAddClient(Consumer<Integer> onInserted) {
        this.onInserted = onInserted;
    }

    public void showDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Client");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(12));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField tfName = new TextField();
        TextField tfSurname = new TextField();
        TextField tfPhone = new TextField();
        TextField tfEmail = new TextField();
        TextField tfAddress = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(tfName, 1, 0);
        grid.add(new Label("Surname:"), 0, 1);
        grid.add(tfSurname, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(tfPhone, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(tfEmail, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(tfAddress, 1, 4);

        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");
        Label status = new Label();

        btnSave.setDefaultButton(true);
        btnCancel.setCancelButton(true);

        grid.add(btnSave, 0, 5);
        grid.add(btnCancel, 1, 5);
        grid.add(status, 0, 6, 2, 1);

        // cancel closes the dialog
        btnCancel.setOnAction(e -> dialog.close());

        // save button handler: validate and perform insert in background
        btnSave.setOnAction(e -> {
            // simple validation
            String name = tfName.getText().trim();
            String surname = tfSurname.getText().trim();
            String phone = tfPhone.getText().trim();
            String email = tfEmail.getText().trim();
            String address = tfAddress.getText().trim();

            if (name.isEmpty() || surname.isEmpty() || phone.isEmpty()) {
                status.setText("Name, surname and phone are required.");
                return;
            }

            // disable save while inserting and show status
            btnSave.setDisable(true);
            status.setText("Saving...");

            // do the insert on a background thread
            Task<Integer> task = new Task<>() {
                @Override
                protected Integer call() {
                    Client client = new Client(name, surname, phone, email, address);
                    return clientDao.insert(client); // returns inserted id or negative
                }
            };

            task.setOnSucceeded(ev -> {
                int id = task.getValue();
                if (id > 0) {
                    status.setText("Inserted id = " + id);
                    // notify caller about the new id (ui thread)
                    if (onInserted != null) {
                        Platform.runLater(() -> onInserted.accept(id));
                    }
                    dialog.close();
                } else {
                    status.setText("Insert failed (id <= 0).");
                    btnSave.setDisable(false);
                }
            });

            task.setOnFailed(ev -> {
                Throwable ex = task.getException();
                status.setText("Error: " + (ex == null ? "unknown" : ex.getMessage()));
                ex.printStackTrace();
                btnSave.setDisable(false);
            });

            Thread t = new Thread(task, "insert-client");
            t.setDaemon(true);
            t.start();
        });

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}