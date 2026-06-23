package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.RecuperaPasswordControl;
import it.afam.utility.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

/** Inserimento del codice di recupero password (UC 1.4). */
public class SchermataInserimentoCodice {

    @FXML private TextField codice;
    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private Button bottoneConferma;
    @FXML private Button bottoneNuovoCodice;

    @FXML
    private void cliccaConferma() {
        if (RecuperaPasswordControl.getIstanza().verificaCodiceRecupero(codice.getText())) {
            SceneManager.getIstanza().switchTo("SchermataNuovaPassword.fxml");
        } else {
            MessaggioDiErrore.mostra("Codice non valido");
        }
    }

    @FXML
    private void cliccaRichiediNuovoCodice() {
        impostaCaricamento(true);
        Task<Void> taskCodice = new Task<>() {
            @Override
            protected Void call() {
                RecuperaPasswordControl.getIstanza().richiediNuovoCodice();
                return null;
            }
        };

        taskCodice.setOnSucceeded(event -> impostaCaricamento(false));
        taskCodice.setOnFailed(event -> {
            impostaCaricamento(false);
            MessaggioDiErrore.mostra("Invio del nuovo codice non riuscito. Riprova tra qualche secondo.");
        });

        Thread threadCodice = new Thread(taskCodice, "nuovo-codice-recupero-afam");
        threadCodice.setDaemon(true);
        threadCodice.start();
    }

    private void impostaCaricamento(boolean caricamento) {
        indicatoreCaricamento.setVisible(caricamento);
        indicatoreCaricamento.setManaged(caricamento);
        labelCaricamento.setVisible(caricamento);
        labelCaricamento.setManaged(caricamento);

        codice.setDisable(caricamento);
        bottoneConferma.setDisable(caricamento);
        bottoneNuovoCodice.setDisable(caricamento);
    }
}
