package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.MostraContenutiLinkControl;
import it.afam.utility.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Schermata di accesso ai contenuti condivisi tramite link (UC 6.1).
 * Riceve il link dall'esterno (found message: l'OS apre l'app con il link
 * cliccato fuori, es. WhatsApp) e avvia la consultazione.
 */
public class SchermataContenutiTramiteLink {

    /** Link ricevuto dall'esterno (impostato dal Main all'avvio). */
    public static String linkRicevuto;

    @FXML private Label labelLink;

    @FXML
    private void initialize() {
        labelLink.setText(linkRicevuto == null ? "" : linkRicevuto);
        // avvio automatico della consultazione a partire dal link ricevuto (found message)
        Platform.runLater(() -> {
            if (linkRicevuto != null && !linkRicevuto.isBlank()) {
                new MostraContenutiLinkControl().mostraContenutiTramiteLink(linkRicevuto.trim());
            }
        });
    }

    @FXML
    private void cliccaHome() {
        SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
    }
}
