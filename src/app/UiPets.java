package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import repository.PetDao;

import java.util.List;

public class UiPets {

    // pet dao used to read pet records from database
    private final PetDao petDao = new PetDao();
    // observable list backing the list view
    private final ObservableList<String> items = FXCollections.observableArrayList();

    public Node getView() {
        // build the list view and controls
        ListView<String> listView = new ListView<>(items);
        listView.setPrefSize(800, 500);

        Button btnLoad = new Button("Show pets");
        Button btnRefresh = new Button("Refresh");
        btnRefresh.setDisable(true);
        Label status = new Label("Ready");

        // load data when buttons are clicked
        btnLoad.setOnAction(e -> loadPets(btnLoad, btnRefresh, status));
        btnRefresh.setOnAction(e -> loadPets(btnLoad, btnRefresh, status));

        HBox topBar = new HBox(10, btnLoad, btnRefresh, status);
        topBar.setStyle("-fx-padding: 10;");

        BorderPane pane = new BorderPane();
        pane.setTop(topBar);
        pane.setCenter(listView);

        return pane;
    }
    // load pets from db on a background thread
    private void loadPets(Button triggerButton, Button refreshButton, Label status) {
        triggerButton.setDisable(true);
        refreshButton.setDisable(true);
        status.setText("Loading...");

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() {
                // call dao on background thread
                return petDao.findAllBasic();
            }
        };

        // on success update the observable list on the ui thread
        task.setOnSucceeded(ev -> {
            List<String> result = task.getValue();
            items.setAll(result);
            status.setText("Loaded: " + result.size() + " pets");
            triggerButton.setDisable(false);
            refreshButton.setDisable(false);
        });

        // handle failure and re-enable controls
        task.setOnFailed(ev -> {
            Throwable ex = task.getException();
            String msg = ex == null ? "Unknown error" : ex.getMessage();
            status.setText("Error: " + msg);
            triggerButton.setDisable(false);
            refreshButton.setDisable(false);
            ex.printStackTrace();
        });

        Thread t = new Thread(task, "pets-loader");
        t.setDaemon(true);
        t.start();
    }

}