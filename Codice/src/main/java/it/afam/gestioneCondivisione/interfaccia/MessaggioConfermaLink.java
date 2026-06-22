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

/** Dialogo "Link generato con successo" con i pulsanti Copia Link e OK (UC 4.2). */
public class MessaggioConfermaLink {

    public static void mostra(String link) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Link generato");

        Label titolo = new Label("Link generato con successo");
        TextField campoLink = new TextField(link);
        campoLink.setEditable(false);
        campoLink.setPrefWidth(320);

        Button copia = new Button("Copia Link");
        copia.setOnAction(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(link);
            Clipboard.getSystemClipboard().setContent(content);
        });
        Button ok = new Button("OK");
        ok.setOnAction(e -> stage.close());

        HBox barra = new HBox(10, copia, ok);
        barra.setAlignment(Pos.CENTER);
        VBox box = new VBox(15, titolo, campoLink, barra);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));

        stage.setScene(new Scene(box));
        stage.showAndWait();
    }
}
