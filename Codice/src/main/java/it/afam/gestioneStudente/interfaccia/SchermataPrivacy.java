package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiConferma;
import it.afam.gestioneStudente.control.GestioneFileControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

/** Gestione della privacy di un file: Pubblico / Privato (UC 3.7). */
public class SchermataPrivacy {

    @FXML private RadioButton opzionePubblico;
    @FXML private RadioButton opzionePrivato;

    @FXML
    private void initialize() {
        try {
            String privacy = GestioneFileControl.getIstanza().caricaPrivacy();
            if ("Pubblico".equals(privacy)) {
                opzionePubblico.setSelected(true);
            } else {
                opzionePrivato.setSelected(true);
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaConfermaModifica() {
        String scelta = opzionePubblico.isSelected() ? "Pubblico" : "Privato";
        try {
            GestioneFileControl.getIstanza().modificaPrivacy(scelta);
            MessaggioDiConferma.mostra("Impostazioni di privacy aggiornate correttamente");
            SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
    }
}
