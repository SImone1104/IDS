package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.LoginControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

/** Verifica del codice OTP per il login a due fattori (UC 1.1). */
public class SchermataVerificaIdentita {

    @FXML private TextField codice;
    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private Button bottoneVerifica;
    @FXML private Button bottoneNuovoCodice;

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
        impostaCaricamento(true);
        Task<Void> taskCodice = new Task<>() {
            @Override
            protected Void call() {
                LoginControl.getIstanza().richiediNuovoCodice();
                return null;
            }
        };

        taskCodice.setOnSucceeded(event -> impostaCaricamento(false));
        taskCodice.setOnFailed(event -> {
            impostaCaricamento(false);
            MessaggioDiErrore.mostra("Invio del nuovo codice non riuscito. Riprova tra qualche secondo.");
        });

        Thread threadCodice = new Thread(taskCodice, "nuovo-codice-login-afam");
        threadCodice.setDaemon(true);
        threadCodice.start();
    }

    private void impostaCaricamento(boolean caricamento) {
        indicatoreCaricamento.setVisible(caricamento);
        indicatoreCaricamento.setManaged(caricamento);
        labelCaricamento.setVisible(caricamento);
        labelCaricamento.setManaged(caricamento);

        codice.setDisable(caricamento);
        bottoneVerifica.setDisable(caricamento);
        bottoneNuovoCodice.setDisable(caricamento);
    }
}
