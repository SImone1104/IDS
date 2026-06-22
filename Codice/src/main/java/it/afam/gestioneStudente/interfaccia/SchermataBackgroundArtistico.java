package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneStudente.control.GestioneProfiloControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiBackground;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/** Elenco delle voci di background artistico con aggiunta/modifica/eliminazione (UC 2.2-2.4). */
public class SchermataBackgroundArtistico {

    @FXML private ListView<DatiBackground> listaBackground;

    @FXML
    private void initialize() {
        listaBackground.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiBackground d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty || d == null ? null : riassunto(d));
            }
        });
        aggiornaLista();
    }

    private void aggiornaLista() {
        try {
            listaBackground.setItems(FXCollections.observableArrayList(
                    GestioneProfiloControl.getIstanza().caricaBackgroundArtistico()));
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    private String riassunto(DatiBackground d) {
        return "Scuola/Università: " + sicuro(d.scuolaUniversita())
                + "  |  Partecipazioni: " + sicuro(d.partecipazioni());
    }

    private String sicuro(String s) {
        return s == null || s.isEmpty() ? "-" : s;
    }

    @FXML
    private void cliccaAggiungiDati() {
        GestioneProfiloControl.getIstanza().preparaAggiunta();
        SceneManager.getIstanza().switchTo("FormModificaBackgroundArtistico.fxml");
    }

    @FXML
    private void cliccaModificaDati() {
        DatiBackground sel = listaBackground.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona una voce da modificare");
            return;
        }
        GestioneProfiloControl.getIstanza().preparaModifica(sel);
        SceneManager.getIstanza().switchTo("FormModificaBackgroundArtistico.fxml");
    }

    @FXML
    private void cliccaEliminaDati() {
        DatiBackground sel = listaBackground.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona una voce da eliminare");
            return;
        }
        try {
            GestioneProfiloControl.getIstanza().eliminaBackground(sel.id());
            aggiornaLista();
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataGestionePortfolio.fxml");
    }
}
