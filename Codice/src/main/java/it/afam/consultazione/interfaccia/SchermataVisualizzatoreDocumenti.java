package it.afam.consultazione.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.utility.ConnessioneException;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

import java.io.File;

/** Visualizzatore inline per documenti PDF e immagini (UC 5.4, 6.2). */
public class SchermataVisualizzatoreDocumenti {

    @FXML private Label labelInfo;
    @FXML private WebView visualizzatore;

    @FXML
    private void initialize() {
        try {
            File f = DatabaseManager.getIstanza()
                    .recuperaFileFisico(SessioneCorrente.getIstanza().getIdFileCorrente());
            if (f != null && f.exists()) {
                labelInfo.setText(f.getName());
                visualizzatore.getEngine().load(f.toURI().toString());
            } else {
                labelInfo.setText("File non disponibile");
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
