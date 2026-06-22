package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.RegistrazioneControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/** Inserimento del codice di attivazione dell'account (UC 1.3). */
public class SchermataAttivazioneAccount {

    @FXML private TextField codice;

    @FXML
    private void cliccaVerifica() {
        try {
            if (RegistrazioneControl.getIstanza().verificaCodiceAttivazione(codice.getText())) {
                MessaggioDiConferma.mostra("Registrazione completata");
                SceneManager.getIstanza().switchTo("SchermataLogin.fxml");
            } else {
                MessaggioDiErrore.mostra("Codice non valido");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaRichiediNuovoCodice() {
        RegistrazioneControl.getIstanza().richiediNuovoCodice();
    }
}
