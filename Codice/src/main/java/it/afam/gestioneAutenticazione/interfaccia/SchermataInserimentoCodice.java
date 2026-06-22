package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.RecuperaPasswordControl;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/** Inserimento del codice di recupero password (UC 1.4). */
public class SchermataInserimentoCodice {

    @FXML private TextField codice;

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
        RecuperaPasswordControl.getIstanza().richiediNuovoCodice();
    }
}
