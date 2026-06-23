package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.CercaStudenteControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.Utils;
import it.afam.utility.dto.DatiStudente;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

/** Ricerca di uno studente per nome/cognome (UC 5.1). Accessibile anche all'utente esterno. */
public class SchermataRicerca {

    @FXML private TextField campoNome;
    @FXML private TextField campoCognome;

    private final CercaStudenteControl control = new CercaStudenteControl();

    @FXML
    private void cliccaCerca() {
        if (!Utils.validaNome(campoNome.getText()) || !Utils.validaCognome(campoCognome.getText())) {
            MessaggioDiErrore.mostra("Inserisci nome e cognome (solo caratteri alfabetici)");
            return;
        }
        try {
            List<DatiStudente> risultati = control.cercaStudente(campoNome.getText(), campoCognome.getText());
            if (risultati.isEmpty()) {
                MessaggioDiErrore.mostra("Nessun risultato trovato");
                return;
            }
            SchermataRisultatoRicerca.risultati = risultati;
            SceneManager.getIstanza().switchTo("SchermataRisultatoRicerca.fxml");
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        // utente autenticato -> Home; utente esterno (non loggato) -> Schermata Principale
        if (SessioneCorrente.getIstanza().getIdStudente() > 0) {
            SceneManager.getIstanza().switchTo("HomeAFAM.fxml");
        } else {
            SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
        }
    }
}
