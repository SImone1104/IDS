package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.CercaStudenteControl;
import it.afam.consultazione.control.MostraContenutiLinkControl;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.Utils;
import it.afam.utility.dto.DatiStudente;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

/** Ricerca di uno studente per nome/cognome e accesso tramite link (UC 5.1, 6.1). */
public class SchermataRicerca {

    @FXML private TextField campoNome;
    @FXML private TextField campoCognome;
    @FXML private TextField campoLink;

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
    private void cliccaApriLink() {
        String link = campoLink.getText();
        if (link == null || link.isBlank()) {
            MessaggioDiErrore.mostra("Inserisci un link");
            return;
        }
        try {
            new MostraContenutiLinkControl().mostraContenutiTramiteLink(link.trim());
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("HomeAFAM.fxml");
    }
}
