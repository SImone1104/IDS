package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.GestisciCodiceControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;

/** Generazione del codice di sblocco (UC 7.2). */
public class SchermataGenerazioneCodice {

    @FXML
    private void cliccaGeneraCodice() {
        try {
            GestisciCodiceControl.getIstanza().generaCodice();
            SceneManager.getIstanza().switchTo("SchermataGestioneCondInterna.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataSelezionaContenuti.fxml");
    }
}
