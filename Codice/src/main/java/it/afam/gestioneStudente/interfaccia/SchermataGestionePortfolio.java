package it.afam.gestioneStudente.interfaccia;

import it.afam.utility.SceneManager;
import javafx.fxml.FXML;

/** Snodo della gestione portfolio: porta al background artistico. */
public class SchermataGestionePortfolio {

    @FXML
    private void cliccaBackgroundArtistico() {
        SceneManager.getIstanza().switchTo("SchermataBackgroundArtistico.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("HomeAFAM.fxml");
    }
}
