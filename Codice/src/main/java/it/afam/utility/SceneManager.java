package it.afam.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Singleton per la navigazione tra le schermate principali: mantiene un unico
 * Stage primario e ne sostituisce la Scene. Gestisce inoltre l'apertura dei
 * dialoghi modali (MessaggioDiErrore/Conferma/Avviso).
 * Tutti gli FXML risiedono in /fxml/.
 */
public class SceneManager {

    private static SceneManager istanza;
    private Stage stagePrimario;

    private SceneManager() { }

    public static SceneManager getIstanza() {
        if (istanza == null) {
            istanza = new SceneManager();
        }
        return istanza;
    }

    public void setStage(Stage stage) {
        this.stagePrimario = stage;
    }

    /** Sostituisce la schermata principale a tutto schermo. */
    public void switchTo(String fxmlName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName));
            Parent schermata = creaSchermataConTema(root);
            Scene scene = stagePrimario.getScene();
            if (scene == null) {
                scene = new Scene(schermata, 900, 700);
                applicaTema(scene);
                stagePrimario.setScene(scene);
            } else {
                scene.setRoot(schermata);
                applicaTema(scene);
            }
            stagePrimario.setMinWidth(760);
            stagePrimario.setMinHeight(560);
            stagePrimario.show();
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare la schermata: " + fxmlName, e);
        }
    }

    /** Apre un dialogo modale e restituisce il controller, per configurarlo. */
    public <T> T openModal(String fxmlName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlName));
            Parent root = loader.load();
            Stage modale = new Stage();
            modale.initModality(Modality.APPLICATION_MODAL);
            modale.setTitle(title);
            Scene scene = new Scene(root);
            applicaTema(scene);
            modale.setScene(scene);
            T controller = loader.getController();
            // mostra in modo bloccante; il controller chiude la finestra al click
            modale.showAndWait();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Impossibile aprire il dialogo: " + fxmlName, e);
        }
    }

    private Parent creaSchermataConTema(Parent contenuto) {
        preparaContenuto(contenuto);

        BorderPane shell = new BorderPane();
        shell.getStyleClass().add("app-shell");

        StackPane areaCentrale = new StackPane(contenuto);
        areaCentrale.getStyleClass().add("content-area");

        contenuto.getStyleClass().add("view-card");
        shell.setCenter(areaCentrale);
        return shell;
    }

    private void preparaContenuto(Parent root) {
        root.lookupAll(".label").stream()
                .filter(Label.class::isInstance)
                .map(Label.class::cast)
                .filter(label -> label.getText() != null && !label.getText().isBlank())
                .findFirst()
                .ifPresent(label -> label.getStyleClass().add("screen-title"));

        for (Node node : root.lookupAll(".button")) {
            node.getStyleClass().add("action-button");
        }
    }

    private void applicaTema(Scene scene) {
        URL css = getClass().getResource("/css/afam-theme.css");
        if (css != null && !scene.getStylesheets().contains(css.toExternalForm())) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }
}
