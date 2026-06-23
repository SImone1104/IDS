package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.GestisciCodiceControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/** Generazione del codice di sblocco (UC 7.2). */
public class SchermataGenerazioneCodice {

    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private Button bottoneGenera;
    @FXML private Button bottoneIndietro;

    @FXML
    private void cliccaGeneraCodice() {
        impostaCaricamento(true);
        Task<Void> taskCodice = new Task<>() {
            @Override
            protected Void call() {
                GestisciCodiceControl.getIstanza().generaCodice();
                return null;
            }
        };

        taskCodice.setOnSucceeded(event -> {
            impostaCaricamento(false);
            SceneManager.getIstanza().switchTo("SchermataGestioneCondInterna.fxml");
        });
        taskCodice.setOnFailed(event -> {
            impostaCaricamento(false);
            Throwable errore = taskCodice.getException();
            if (errore instanceof ConnessioneException) {
                MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
            } else {
                MessaggioDiErrore.mostra("Generazione codice non riuscita. Riprova tra qualche secondo.");
            }
        });

        Thread threadCodice = new Thread(taskCodice, "genera-codice-afam");
        threadCodice.setDaemon(true);
        threadCodice.start();
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataSelezionaContenuti.fxml");
    }

    private void impostaCaricamento(boolean caricamento) {
        indicatoreCaricamento.setVisible(caricamento);
        indicatoreCaricamento.setManaged(caricamento);
        labelCaricamento.setVisible(caricamento);
        labelCaricamento.setManaged(caricamento);
        bottoneGenera.setDisable(caricamento);
        bottoneIndietro.setDisable(caricamento);
    }
}
