package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.GestisciLinkControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/** Generazione del link di condivisione (UC 4.2). */
public class SchermataGenerazioneLink {

    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private Button bottoneGenera;
    @FXML private Button bottoneIndietro;

    @FXML
    private void cliccaGeneraLink() {
        impostaCaricamento(true);
        Task<Void> taskLink = new Task<>() {
            @Override
            protected Void call() {
                GestisciLinkControl.getIstanza().generaLink();
                return null;
            }
        };

        taskLink.setOnSucceeded(event -> {
            impostaCaricamento(false);
            SceneManager.getIstanza().switchTo("SchermataGestioneCondLink.fxml");
        });
        taskLink.setOnFailed(event -> {
            impostaCaricamento(false);
            Throwable errore = taskLink.getException();
            if (errore instanceof ConnessioneException) {
                MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
            } else {
                MessaggioDiErrore.mostra("Generazione link non riuscita. Riprova tra qualche secondo.");
            }
        });

        Thread threadLink = new Thread(taskLink, "genera-link-afam");
        threadLink.setDaemon(true);
        threadLink.start();
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
