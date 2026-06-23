package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.RegistrazioneControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

/** Form di registrazione di un nuovo studente (UC 1.3). */
public class SchermataRegistrazione {

    @FXML private TextField nome;
    @FXML private TextField cognome;
    @FXML private TextField codiceFiscale;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private PasswordField confermaPassword;
    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private Button bottoneConferma;
    @FXML private Button bottoneAnnulla;

    @FXML
    private void confermaRegistrazione() {
        RegistrazioneControl control = RegistrazioneControl.getIstanza();
        if (!control.validaDati(nome.getText(), cognome.getText(), codiceFiscale.getText(),
                email.getText(), password.getText(), confermaPassword.getText())) {
            MessaggioDiErrore.mostra(control.getUltimoErrore());
            return;
        }

        impostaCaricamento(true);
        String emailInserita = email.getText();
        Task<Boolean> taskRegistrazione = new Task<>() {
            @Override
            protected Boolean call() {
                if (!control.emailDisponibile(emailInserita)) {
                    return false;
                }
                control.generaEInviaCodiceAttivazione();
                return true;
            }
        };

        taskRegistrazione.setOnSucceeded(event -> {
            impostaCaricamento(false);
            if (taskRegistrazione.getValue()) {
                SceneManager.getIstanza().switchTo("SchermataAttivazioneAccount.fxml");
            } else {
                MessaggioDiErrore.mostra("E-mail gia in uso");
            }
        });

        taskRegistrazione.setOnFailed(event -> {
            impostaCaricamento(false);
            Throwable errore = taskRegistrazione.getException();
            if (errore instanceof ConnessioneException) {
                MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
            } else {
                MessaggioDiErrore.mostra("Invio del codice non riuscito. Riprova tra qualche secondo.");
            }
        });

        Thread threadRegistrazione = new Thread(taskRegistrazione, "registrazione-afam");
        threadRegistrazione.setDaemon(true);
        threadRegistrazione.start();
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
    }

    private void impostaCaricamento(boolean caricamento) {
        indicatoreCaricamento.setVisible(caricamento);
        indicatoreCaricamento.setManaged(caricamento);
        labelCaricamento.setVisible(caricamento);
        labelCaricamento.setManaged(caricamento);

        nome.setDisable(caricamento);
        cognome.setDisable(caricamento);
        codiceFiscale.setDisable(caricamento);
        email.setDisable(caricamento);
        password.setDisable(caricamento);
        confermaPassword.setDisable(caricamento);
        bottoneConferma.setDisable(caricamento);
        bottoneAnnulla.setDisable(caricamento);
    }
}
