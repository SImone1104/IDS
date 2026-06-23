package it.afam.gestioneStudente.interfaccia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialogo modale di avviso (boundary locale). Mostrato in caso di caduta di
 * connessione (UC 0.1): pulsanti Riprova e Annulla. Restituisce true se l'utente
 * sceglie Riprova.
 */
public class MessaggioDiAvviso {

    public static boolean mostra(String messaggio) {
        final boolean[] riprova = {false};
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Avviso");

        Label label = new Label(messaggio);
        label.setWrapText(true);
        Button bottoneRiprova = new Button("Riprova");
        Button bottoneAnnulla = new Button("Annulla");
        bottoneRiprova.setOnAction(e -> { riprova[0] = true; stage.close(); });
        bottoneAnnulla.setOnAction(e -> stage.close());

        HBox barra = new HBox(10, bottoneRiprova, bottoneAnnulla);
        barra.setAlignment(Pos.CENTER);
        VBox box = new VBox(15, label, barra);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setMinWidth(300);

        stage.setScene(new Scene(box));
        stage.showAndWait();
        return riprova[0];
    }
}
