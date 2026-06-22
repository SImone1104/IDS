package it.afam.consultazione.interfaccia;

import it.afam.entity.EntityStudente;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.utility.ConnessioneException;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/** Profilo pubblico di uno studente consultato (UC 5.1, 5.2). */
public class SchermataProfiloStudente {

    @FXML private Label labelNome;
    @FXML private Label labelCognome;

    @FXML
    private void initialize() {
        try {
            EntityStudente s = DatabaseManager.getIstanza()
                    .recuperaProfilo(SessioneCorrente.getIstanza().getIdProfiloConsultato());
            if (s != null) {
                labelNome.setText("Nome: " + s.getNome());
                labelCognome.setText("Cognome: " + s.getCognome());
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaMostraContenutiPubblici() {
        SceneManager.getIstanza().switchTo("SchermataContenutiPubblici.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataRisultatoRicerca.fxml");
    }
}
