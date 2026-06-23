package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.GestisciCodiceControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
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
        try {
            impostaCaricamento(true);
            GestisciCodiceControl.getIstanza().generaCodice();
            SceneManager.getIstanza().switchTo("SchermataGestioneCondInterna.fxml");
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
