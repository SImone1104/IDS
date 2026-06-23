package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.LoginControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Login tramite credenziali con verifica OTP a due fattori (UC 1.1).
 * Il login SPID/eIDAS (UC 1.2) non è implementato (estensione futura, SDD).
 */
public class SchermataLogin {

    @FXML private TextField email;
    @FXML private PasswordField password;

    @FXML
    private void cliccaAccedi() {
        try {
            if (LoginControl.getIstanza().accedi(email.getText(), password.getText())) {
                SceneManager.getIstanza().switchTo("SchermataVerificaIdentita.fxml");
            } else {
                MessaggioDiErrore.mostra("Credenziali non corrette");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaAccediSpidEidas() {
        MessaggioDiErrore.mostra("Login tramite SPID/eIDAS non disponibile in questa demo.");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
    }
}
