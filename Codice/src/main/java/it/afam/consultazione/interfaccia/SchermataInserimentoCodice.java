package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.VisualizzaProfiloControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Inserimento del codice di sblocco per accedere ai contenuti privati di uno
 * studente consultato (UC 5.3). Boundary locale al sotto-sistema consultazione.
 */
public class SchermataInserimentoCodice {

    @FXML private TextField codice;

    private final VisualizzaProfiloControl control = new VisualizzaProfiloControl();

    @FXML
    private void cliccaConferma() {
        try {
            if (control.verificaCodiceSblocco(codice.getText().trim())) {
                SceneManager.getIstanza().switchTo("SchermataContenutiCondivisi.fxml");
            } else {
                MessaggioDiErrore.mostra("Codice errato");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataProfiloStudente.fxml");
    }
}
