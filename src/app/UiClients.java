package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import repository.ClientDao;

import java.util.List;

public class UiClients {
    // data access object used to read clients from the database
    private final ClientDao clientDao = new ClientDao();
    // observable list backing the listview
    private final ObservableList<String> items = FXCollections.observableArrayList();
    // list view that shows clients (simple text rows)
    private final ListView<String> listView = new ListView<>(items);

    public UiClients() {
        listView.setPrefSize(800, 500);
    }

    public Node getView() {
        // build the view node (top controls + center list)
        Button btnLoad = new Button("Show clients");
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

    // public method to refresh data from outside (e.g. after insert)
    public void refresh() {
        refresh(null, null, null);
    }

    private void refresh(Label status, Button bLoad, Button bRefresh) {
        if (bLoad != null) bLoad.setDisable(true);
        if (bRefresh != null) bRefresh.setDisable(true);
        if (status != null) status.setText("Loading...");

        // perform DB read on background thread so ui stays responsive
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() {
                return clientDao.findAllBasic();
            }
        };

        // when background task finishes successfully, update ui
        task.setOnSucceeded(ev -> {
            items.setAll(task.getValue());
            if (status != null) status.setText("Loaded: " + task.getValue().size());
            if (bLoad != null) bLoad.setDisable(false);
            if (bRefresh != null) bRefresh.setDisable(false);
        });

        // handle failure and re-enable buttons
        task.setOnFailed(ev -> {
            if (status != null) status.setText("Error: " + task.getException().getMessage());
            if (bLoad != null) bLoad.setDisable(false);
            if (bRefresh != null) bRefresh.setDisable(false);
            task.getException().printStackTrace();
        });

        Thread t = new Thread(task, "clients-loader");
        t.setDaemon(true);
        t.start();
    }
}