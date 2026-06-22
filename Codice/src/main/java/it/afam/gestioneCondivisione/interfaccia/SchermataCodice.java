package it.afam.gestioneCondivisione.interfaccia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** Dialogo "Codice generato con successo" con i pulsanti Copia Codice e OK (UC 7.2). */
public class SchermataCodice {

    public static void mostra(String codice) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Codice generato");

        Label titolo = new Label("Codice generato con successo");
        TextField campoCodice = new TextField(codice);
        campoCodice.setEditable(false);
        campoCodice.setPrefWidth(260);

        Button copia = new Button("Copia Codice");
        copia.setOnAction(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(codice);
            Clipboard.getSystemClipboard().setContent(content);
        });
        Button ok = new Button("OK");
        ok.setOnAction(e -> stage.close());

        HBox barra = new HBox(10, copia, ok);
        barra.setAlignment(Pos.CENTER);
        VBox box = new VBox(15, titolo, campoCodice, barra);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));

        stage.setScene(new Scene(box));
        stage.showAndWait();
    }
}
