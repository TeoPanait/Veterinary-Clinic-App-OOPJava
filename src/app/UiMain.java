package app;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Application;

public class UiMain extends Application {

    // ui view instances (keep as fields so we can refresh and reuse)
    private UiClients clientsPane;
    private UiPets petsPane;
    private UiMedication medsPane;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // instantiate the view objects (we keep instances to allow refresh)
        clientsPane = new UiClients();
        petsPane = new UiPets();
        medsPane = new UiMedication();

        // meniu
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem miClients = new MenuItem("Show Clients");
        MenuItem miPets = new MenuItem("Show Pets");
        MenuItem miMeds = new MenuItem("Show Meds");
        MenuItem miAddClient = new MenuItem("Add Client");

        menu.getItems().addAll(miClients, miPets, miMeds, miAddClient);
        menuBar.getMenus().add(menu);

        // handlers - obtain view nodes and reuse them to avoid rebuilding UI
        Node clientsView = clientsPane.getView();
        Node petsView = petsPane.getView();
        Node medsView = medsPane.getView();

        // menu item actions: set the center pane
        miClients.setOnAction(e -> root.setCenter(clientsView));
        miPets.setOnAction(e -> root.setCenter(petsView));
        miMeds.setOnAction(e -> root.setCenter(medsView));

        // add client dialog: on success refresh the clients view
        miAddClient.setOnAction(e -> {
            UiAddClient addDialog = new UiAddClient(insertedId -> {
                // on insert callback - refresh clients on ui thread
                Platform.runLater(() -> clientsPane.refresh());
            });
            addDialog.showDialog(stage);
        });

        root.setTop(menuBar);

        // show clients view by default at startup
        root.setCenter(clientsView);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Vet Clinic - Main UI");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}