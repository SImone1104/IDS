package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneStudente.control.GestioneFileControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;

/** Menu delle opzioni su un file: modifica, privacy, eliminazione (UC 3.5-3.7). */
public class SchermataGestioneFile {

    @FXML
    private void cliccaModificaFile() {
        SceneManager.getIstanza().switchTo("SchermataModifica.fxml");
    }

    @FXML
    private void cliccaPrivacy() {
        SceneManager.getIstanza().switchTo("SchermataPrivacy.fxml");
    }

    @FXML
    private void cliccaEliminaFile() {
        try {
            GestioneFileControl.getIstanza().eliminaFile();
            SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
    }
}
