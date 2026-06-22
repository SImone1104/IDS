package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneCondivisione.control.GestisciLinkControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiLink;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/** Elenco dei link generati, con revoca (UC 4.3, 4.4). */
public class SchermataElencoLink {

    @FXML private ListView<DatiLink> listaLink;

    @FXML
    private void initialize() {
        listaLink.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiLink d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty || d == null ? null
                        : d.url() + "  | scad. " + d.scadenza() + "  | " + d.stato()
                        + (d.visualizzato() ? "  | letto ✔" : ""));
            }
        });
        aggiornaLista();
    }

    private void aggiornaLista() {
        try {
            listaLink.setItems(FXCollections.observableArrayList(
                    GestisciLinkControl.getIstanza().caricaElencoLink()));
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaRevoca() {
        DatiLink sel = listaLink.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona un link da revocare");
            return;
        }
        try {
            GestisciLinkControl.getIstanza().revocaLink(sel.id());
            aggiornaLista();
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaCopiaLink() {
        DatiLink sel = listaLink.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona un link da copiare");
            return;
        }
        ClipboardContent content = new ClipboardContent();
        content.putString(sel.url());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataGestioneCondLink.fxml");
    }
}
