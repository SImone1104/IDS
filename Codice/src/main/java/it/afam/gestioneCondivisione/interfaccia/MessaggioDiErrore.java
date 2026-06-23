package it.afam.gestioneCondivisione.interfaccia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialogo modale di errore (boundary locale). Realizzazione semplice del
 * MessaggioDiErrore dell'ODD: una label e il pulsante OK.
 */
public class MessaggioDiErrore {

    public static void mostra(String messaggio) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Errore");

        Label label = new Label(messaggio);
        label.setWrapText(true);
        Button bottoneOk = new Button("OK");
        bottoneOk.setOnAction(e -> stage.close());

        VBox box = new VBox(15, label, bottoneOk);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setMinWidth(280);

        stage.setScene(new Scene(box));
        stage.showAndWait();
    }
}
