package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.RecuperaPasswordControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

/** Avvio del recupero password tramite e-mail (UC 1.4). */
public class SchermataRecuperoPassword {

    @FXML private TextField email;
    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private Button bottoneConferma;
    @FXML private Button bottoneIndietro;

    @FXML
    private void cliccaConferma() {
        impostaCaricamento(true);
        String emailInserita = email.getText();
        Task<Boolean> taskRecupero = new Task<>() {
            @Override
            protected Boolean call() {
                return RecuperaPasswordControl.getIstanza().verificaEmail(emailInserita);
            }
        };

        taskRecupero.setOnSucceeded(event -> {
            impostaCaricamento(false);
            if (taskRecupero.getValue()) {
                SceneManager.getIstanza().switchTo("SchermataInserimentoCodice.fxml");
            } else {
                MessaggioDiErrore.mostra("Studente non trovato");
            }
        });

        taskRecupero.setOnFailed(event -> {
            impostaCaricamento(false);
            Throwable errore = taskRecupero.getException();
            if (errore instanceof ConnessioneException) {
                MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
            } else {
                MessaggioDiErrore.mostra("Invio del codice non riuscito. Riprova tra qualche secondo.");
            }
        });

        Thread threadRecupero = new Thread(taskRecupero, "recupero-password-afam");
        threadRecupero.setDaemon(true);
        threadRecupero.start();
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
        bottoneConferma.setDisable(caricamento);
        bottoneIndietro.setDisable(caricamento);
    }
}
