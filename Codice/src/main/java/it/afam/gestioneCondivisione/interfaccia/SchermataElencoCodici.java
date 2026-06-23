package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.GestisciCodiceControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiCodice;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/** Elenco dei codici di sblocco generati, con revoca (UC 7.3, 7.4). */
public class SchermataElencoCodici {

    @FXML private ListView<DatiCodice> listaCodici;

    @FXML
    private void initialize() {
        listaCodici.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiCodice d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty || d == null ? null
                        : d.codice() + "  | " + (d.attivo() ? "Attivo" : "Revocato"));
            }
        });
        aggiornaLista();
    }

    private void aggiornaLista() {
        try {
            listaCodici.setItems(FXCollections.observableArrayList(
                    GestisciCodiceControl.getIstanza().caricaElencoCodici()));
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaRevoca() {
        DatiCodice sel = listaCodici.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona un codice da revocare");
            return;
        }
        try {
            GestisciCodiceControl.getIstanza().revocaCodice(sel.id());
            aggiornaLista();
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataGestioneCondInterna.fxml");
    }
}
