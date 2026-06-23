package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.LoginControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

/**
 * Login tramite credenziali con verifica OTP a due fattori (UC 1.1).
 * Il login SPID/eIDAS (UC 1.2) non è implementato (estensione futura, SDD).
 */
public class SchermataLogin {

    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private Button bottoneAccedi;
    @FXML private Button bottoneSpidEidas;
    @FXML private Button bottoneIndietro;

    @FXML
    private void cliccaAccedi() {
        impostaCaricamento(true);
        String emailInserita = email.getText();
        String passwordInserita = password.getText();

        Task<Boolean> taskLogin = new Task<>() {
            @Override
            protected Boolean call() {
                return LoginControl.getIstanza().accedi(emailInserita, passwordInserita);
            }
        };

        taskLogin.setOnSucceeded(event -> {
            impostaCaricamento(false);
            if (taskLogin.getValue()) {
                SceneManager.getIstanza().switchTo("SchermataVerificaIdentita.fxml");
            } else {
                MessaggioDiErrore.mostra("Credenziali non corrette");
            }
        });

        taskLogin.setOnFailed(event -> {
            impostaCaricamento(false);
            Throwable errore = taskLogin.getException();
            if (errore instanceof ConnessioneException) {
                MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
            } else {
                MessaggioDiErrore.mostra("Accesso non riuscito. Riprova tra qualche secondo.");
            }
        });

        Thread threadLogin = new Thread(taskLogin, "login-afam");
        threadLogin.setDaemon(true);
        threadLogin.start();
    }

    @FXML
    private void cliccaAccediSpidEidas() {
        MessaggioDiErrore.mostra("Login tramite SPID/eIDAS non disponibile in questa demo.");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
    }

    private void impostaCaricamento(boolean caricamento) {
        indicatoreCaricamento.setVisible(caricamento);
        indicatoreCaricamento.setManaged(caricamento);
        labelCaricamento.setVisible(caricamento);
        labelCaricamento.setManaged(caricamento);

        email.setDisable(caricamento);
        password.setDisable(caricamento);
        bottoneAccedi.setDisable(caricamento);
        bottoneSpidEidas.setDisable(caricamento);
        bottoneIndietro.setDisable(caricamento);
    }
}
