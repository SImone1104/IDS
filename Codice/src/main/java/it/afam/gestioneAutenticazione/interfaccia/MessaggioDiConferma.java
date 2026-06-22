package it.afam.gestioneAutenticazione.interfaccia;

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
 * Dialogo modale di conferma (boundary locale). Due usi previsti dall'ODD:
 * - notifica di successo (solo OK)        -> mostra(messaggio)
 * - conferma prima di un'azione (Conferma/Annulla) -> conferma(messaggio)
 */
public class MessaggioDiConferma {

    /** Notifica di successo: solo pulsante OK. */
    public static void mostra(String messaggio) {
        Stage stage = creaStage(messaggio, "Conferma");
        Button ok = new Button("OK");
        ok.setOnAction(e -> stage.close());
        aggiungiPulsanti(stage, ok);
        stage.showAndWait();
    }

    /** Richiesta di conferma: restituisce true se l'utente clicca Conferma. */
    public static boolean conferma(String messaggio) {
        final boolean[] esito = {false};
        Stage stage = creaStage(messaggio, "Conferma");
        Button conferma = new Button("Conferma");
        Button annulla = new Button("Annulla");
        conferma.setOnAction(e -> { esito[0] = true; stage.close(); });
        annulla.setOnAction(e -> stage.close());
        aggiungiPulsanti(stage, conferma, annulla);
        stage.showAndWait();
        return esito[0];
    }

    // --- helper interni ---

    private static Stage creaStage(String messaggio, String titolo) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(titolo);
        Label label = new Label(messaggio);
        label.setWrapText(true);
        VBox box = new VBox(15, label);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setMinWidth(300);
        stage.setScene(new Scene(box));
        return stage;
    }

    private static void aggiungiPulsanti(Stage stage, Button... pulsanti) {
        VBox box = (VBox) stage.getScene().getRoot();
        HBox barra = new HBox(10, pulsanti);
        barra.setAlignment(Pos.CENTER);
        box.getChildren().add(barra);
    }
}
