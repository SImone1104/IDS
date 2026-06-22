package it.afam.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
            Scene scene = stagePrimario.getScene();
            if (scene == null) {
                stagePrimario.setScene(new Scene(root));
            } else {
                scene.setRoot(root);
            }
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
            modale.setScene(new Scene(root));
            T controller = loader.getController();
            // mostra in modo bloccante; il controller chiude la finestra al click
            modale.showAndWait();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Impossibile aprire il dialogo: " + fxmlName, e);
        }
    }
}
