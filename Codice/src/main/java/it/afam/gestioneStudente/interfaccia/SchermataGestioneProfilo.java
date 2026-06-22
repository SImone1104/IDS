package it.afam.gestioneStudente.interfaccia;

import it.afam.utility.SceneManager;
import javafx.fxml.FXML;

/** Snodo della gestione profilo: porta ai dati personali. */
public class SchermataGestioneProfilo {

    @FXML
    private void cliccaDatiPersonali() {
        SceneManager.getIstanza().switchTo("SchermataDatiPersonali.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("HomeAFAM.fxml");
    }
}
