package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiConferma;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneStudente.control.UploadFileControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/** Caricamento di un nuovo file (UC 3.4). */
public class SchermataUpload {

    @FXML private TextField campoTitolo;
    @FXML private TextArea campoDescrizione;
    @FXML private Label labelFile;

    private final UploadFileControl control = new UploadFileControl();
    private String percorsoFile;

    @FXML
    private void cliccaScegliFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Scegli file da caricare");
        var file = chooser.showOpenDialog(campoTitolo.getScene().getWindow());
        if (file != null) {
            percorsoFile = file.getAbsolutePath();
            labelFile.setText(file.getName());
        }
    }

    @FXML
    private void cliccaConferma() {
        try {
            if (control.caricaFile(campoTitolo.getText(), campoDescrizione.getText(), percorsoFile)) {
                MessaggioDiConferma.mostra("File caricato con successo");
                SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
            } else {
                MessaggioDiErrore.mostra("Caricamento non riuscito: verifica titolo (max 200 caratteri), "
                        + "dimensione (max 1 GB) e formato idoneo alla tipologia");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
    }
}
