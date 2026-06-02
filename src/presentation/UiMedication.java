package presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import repository.MedicationDao;

import java.util.List;

public class UiMedication {
    // dao for medications table
    private final MedicationDao medicationDao = new MedicationDao();
    // observable list for the list view
    private final ObservableList<String> items = FXCollections.observableArrayList();
    // list view showing medications
    private final ListView<String> listView = new ListView<>(items);

    public UiMedication() {
        listView.setPrefSize(800, 500);
    }

    public Node getView() {
        // build ui controls
        Button btnLoad = new Button("Show medications");
        Button btnRefresh = new Button("Refresh");
        Label status = new Label("Ready");

        btnLoad.setOnAction(e -> refresh(status, btnLoad, btnRefresh));
        btnRefresh.setOnAction(e -> refresh(status, btnLoad, btnRefresh));

        HBox topBar = new HBox(10, btnLoad, btnRefresh, status);
        topBar.setStyle("-fx-padding: 10;");

        BorderPane pane = new BorderPane();
        pane.setTop(topBar);
        pane.setCenter(listView);
        return pane;
    }

    public void refresh() { refresh(null, null, null); }

    private void refresh(Label status, Button bLoad, Button bRefresh) {
        if (bLoad != null) bLoad.setDisable(true);
        if (bRefresh != null) bRefresh.setDisable(true);
        if (status != null) status.setText("Loading...");

        // run dao call on background thread
        Task<List<String>> task = new Task<>() {
            @Override protected List<String> call() { return medicationDao.findAllBasic(); }
        };

        // update ui when task completes
        task.setOnSucceeded(ev -> {
            items.setAll(task.getValue());
            if (status != null) status.setText("Loaded: " + task.getValue().size());
            if (bLoad != null) bLoad.setDisable(false);
            if (bRefresh != null) bRefresh.setDisable(false);
        });

        task.setOnFailed(ev -> {
            if (status != null) status.setText("Error: " + task.getException().getMessage());
            if (bLoad != null) bLoad.setDisable(false);
            if (bRefresh != null) bRefresh.setDisable(false);
            task.getException().printStackTrace();
        });

        Thread t = new Thread(task, "meds-loader");
        t.setDaemon(true);
        t.start();
    }
}