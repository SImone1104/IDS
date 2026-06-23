package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.GestisciLinkControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
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
        try {
            impostaCaricamento(true);
            GestisciLinkControl.getIstanza().generaLink();
            SceneManager.getIstanza().switchTo("SchermataGestioneCondLink.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        } finally {
            impostaCaricamento(false);
        }
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
