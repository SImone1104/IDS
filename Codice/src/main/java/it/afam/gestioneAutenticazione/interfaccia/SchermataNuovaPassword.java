package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.RecuperaPasswordControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

/** Inserimento della nuova password al termine del recupero (UC 1.4). */
public class SchermataNuovaPassword {

    @FXML private PasswordField nuovaPassword;

    @FXML
    private void cliccaConferma() {
        if (!Utils.validaPassword(nuovaPassword.getText())) {
            MessaggioDiErrore.mostra("La password deve avere 8-16 caratteri, almeno una maiuscola e un numero");
            return;
        }
        try {
            RecuperaPasswordControl.getIstanza().aggiornaPassword(nuovaPassword.getText());
            MessaggioDiConferma.mostra("Password aggiornata correttamente");
            SceneManager.getIstanza().switchTo("SchermataLogin.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }
}
