package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneCondivisione.control.GestisciLinkControl;
import it.afam.gestioneCondivisione.control.SelezionaContenutiControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import javafx.fxml.FXML;

/** Snodo della condivisione tramite link (UC 4.1, 4.3). */
public class SchermataGestioneCondLink {

    @FXML
    private void cliccaSelezionaContenuti() {
        try {
            if (new SelezionaContenutiControl().caricaContenuti().isEmpty()) {
                MessaggioDiErrore.mostra("Nessun contenuto disponibile");
                return;
            }
            SessioneCorrente.getIstanza().setModalitaSelezione("LINK");
            SceneManager.getIstanza().switchTo("SchermataSelezionaContenuti.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaGestisciLink() {
        try {
            if (GestisciLinkControl.getIstanza().caricaElencoLink().isEmpty()) {
                MessaggioDiErrore.mostra("Nessun link generato");
                return;
            }
            SceneManager.getIstanza().switchTo("SchermataElencoLink.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("HomeAFAM.fxml");
    }
}
