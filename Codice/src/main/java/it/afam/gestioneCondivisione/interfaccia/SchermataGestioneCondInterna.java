package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.GestisciCodiceControl;
import it.afam.gestioneCondivisione.control.SelezionaContenutiPrivatiControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import javafx.fxml.FXML;

/** Snodo della condivisione interna tramite codice (UC 7.1, 7.3). */
public class SchermataGestioneCondInterna {

    @FXML
    private void cliccaSelezionaContenutiPrivati() {
        try {
            if (new SelezionaContenutiPrivatiControl().caricaContenutiPrivati().isEmpty()) {
                MessaggioDiErrore.mostra("Nessun contenuto disponibile");
                return;
            }
            SessioneCorrente.getIstanza().setModalitaSelezione("CODICE");
            SceneManager.getIstanza().switchTo("SchermataSelezionaContenuti.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaGestisciCodici() {
        try {
            if (GestisciCodiceControl.getIstanza().caricaElencoCodici().isEmpty()) {
                MessaggioDiErrore.mostra("Nessun codice generato");
                return;
            }
            SceneManager.getIstanza().switchTo("SchermataElencoCodici.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("HomeAFAM.fxml");
    }
}
