package it.afam.consultazione.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.utility.ConnessioneException;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiFile;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

import java.io.File;

/** Visualizzatore inline per documenti PDF e immagini (UC 5.4, 6.2). Mostra titolo e descrizione. */
public class SchermataVisualizzatoreDocumenti {

    @FXML private Label labelTitolo;
    @FXML private Label labelDescrizione;
    @FXML private WebView visualizzatore;

    @FXML
    private void initialize() {
        try {
            DatiFile f = DatabaseManager.getIstanza()
                    .recuperaFilePerId(SessioneCorrente.getIstanza().getIdFileCorrente());
            if (f == null) {
                labelTitolo.setText("File non disponibile");
                return;
            }
            labelTitolo.setText(f.titolo());
            labelDescrizione.setText(f.descrizione() == null ? "" : f.descrizione());
            File file = new File(f.percorso());
            if (file.exists()) {
                visualizzatore.getEngine().load(file.toURI().toString());
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaChiudi() {
        SceneManager.getIstanza().switchTo(SessioneCorrente.getIstanza().getSchermataRitorno());
    }
}
