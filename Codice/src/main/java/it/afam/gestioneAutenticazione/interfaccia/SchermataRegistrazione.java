package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.RegistrazioneControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/** Form di registrazione di un nuovo studente (UC 1.3). */
public class SchermataRegistrazione {

    @FXML private TextField nome;
    @FXML private TextField cognome;
    @FXML private TextField codiceFiscale;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private PasswordField confermaPassword;

    @FXML
    private void confermaRegistrazione() {
        RegistrazioneControl control = RegistrazioneControl.getIstanza();
        try {
            if (!control.validaDati(nome.getText(), cognome.getText(), codiceFiscale.getText(),
                    email.getText(), password.getText(), confermaPassword.getText())) {
                MessaggioDiErrore.mostra(control.getUltimoErrore());
                return;
            }
            if (!control.emailDisponibile(email.getText())) {
                MessaggioDiErrore.mostra("E-mail già in uso");
                return;
            }
            control.generaEInviaCodiceAttivazione();
            SceneManager.getIstanza().switchTo("SchermataAttivazioneAccount.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
    }
}
