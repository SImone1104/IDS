package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.GestisciLinkControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;

/** Generazione del link di condivisione (UC 4.2). */
public class SchermataGenerazioneLink {

    @FXML
    private void cliccaGeneraLink() {
        try {
            GestisciLinkControl.getIstanza().generaLink();
            SceneManager.getIstanza().switchTo("SchermataGestioneCondLink.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataSelezionaContenuti.fxml");
    }
}
