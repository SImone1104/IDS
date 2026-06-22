package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneStudente.control.TipologiaFileControl;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;

/** Selezione della tipologia di file (UC 3.3). */
public class SchermataTipologiaFile {

    private final TipologiaFileControl control = new TipologiaFileControl();

    @FXML private void cliccaImmagini() { seleziona("Immagine"); }
    @FXML private void cliccaAudio() { seleziona("Audio"); }
    @FXML private void cliccaVideo() { seleziona("Video"); }
    @FXML private void cliccaDocumenti() { seleziona("Documento"); }

    private void seleziona(String tipologia) {
        control.selezionaTipologia(tipologia);
        SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataGestioneContenuti.fxml");
    }
}
