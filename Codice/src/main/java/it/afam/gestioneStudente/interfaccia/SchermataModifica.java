package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiConferma;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneStudente.control.GestioneFileControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiFile;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/** Modifica titolo, descrizione e file di un contenuto (UC 3.6). */
public class SchermataModifica {

    @FXML private TextField campoTitolo;
    @FXML private TextArea campoDescrizione;
    @FXML private Label labelFile;

    private String percorsoFile;

    @FXML
    private void initialize() {
        try {
            DatiFile f = GestioneFileControl.getIstanza().caricaModificaFile();
            if (f != null) {
                campoTitolo.setText(f.titolo());
                campoDescrizione.setText(f.descrizione());
                percorsoFile = f.percorso();
                labelFile.setText(percorsoFile);
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaScegliFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Scegli nuovo file");
        var file = chooser.showOpenDialog(campoTitolo.getScene().getWindow());
        if (file != null) {
            percorsoFile = file.getAbsolutePath();
            labelFile.setText(file.getName());
        }
    }

    @FXML
    private void cliccaConfermaModifica() {
        try {
            if (GestioneFileControl.getIstanza().modificaFile(
                    campoTitolo.getText(), campoDescrizione.getText(), percorsoFile)) {
                MessaggioDiConferma.mostra("Modifica effettuata correttamente");
                SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
            } else {
                MessaggioDiErrore.mostra("Il titolo è obbligatorio (max 200 caratteri)");
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
