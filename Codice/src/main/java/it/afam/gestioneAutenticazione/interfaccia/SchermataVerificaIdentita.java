package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.LoginControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/** Verifica del codice OTP per il login a due fattori (UC 1.1). */
public class SchermataVerificaIdentita {

    @FXML private TextField codice;

    @FXML
    private void cliccaVerifica() {
        try {
            if (LoginControl.getIstanza().verificaCodice(codice.getText())) {
                SceneManager.getIstanza().switchTo("HomeAFAM.fxml");
            } else {
                MessaggioDiErrore.mostra("Codice errato");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaRichiediNuovoCodice() {
        LoginControl.getIstanza().richiediNuovoCodice();
    }
}
