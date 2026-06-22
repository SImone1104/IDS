package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.LogoutControl;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;

/**
 * Schermata principale dello studente autenticato.
 * I pulsanti verso gli altri sotto-sistemi verranno collegati alle rispettive
 * schermate man mano che le fette successive vengono implementate.
 */
public class HomeAFAM {

    private final LogoutControl logoutControl = new LogoutControl();

    @FXML
    private void cliccaLogout() {
        logoutControl.richiediConfermaLogout();
    }

    @FXML
    private void cliccaGestioneProfilo() {
        SceneManager.getIstanza().switchTo("SchermataGestioneProfilo.fxml");
    }

    @FXML
    private void cliccaGestioneContenuti() {
        SceneManager.getIstanza().switchTo("SchermataGestioneContenuti.fxml");
    }

    @FXML
    private void cliccaGestioneCondLink() {
        SceneManager.getIstanza().switchTo("SchermataGestioneCondLink.fxml");
    }

    @FXML
    private void cliccaGestCondInterna() {
        SceneManager.getIstanza().switchTo("SchermataGestioneCondInterna.fxml");
    }

    @FXML
    private void cliccaRicerca() {
        SceneManager.getIstanza().switchTo("SchermataRicerca.fxml");
    }
}
