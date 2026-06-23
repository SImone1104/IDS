package it.afam.gestioneStudente.interfaccia;

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
                setWrapText(true);
                setPrefWidth(0);
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
        return "Scuola/Universita:\n" + elencoPuntato(d.scuolaUniversita())
                + "\nCollaborazioni fatte:\n" + elencoPuntato(d.collaborazioniFatte())
                + "\nCollaborazioni con autori:\n" + elencoPuntato(d.collaborazioniAutori())
                + "\nPartecipazioni:\n" + elencoPuntato(d.partecipazioni());
    }

    private String sicuro(String s) {
        return s == null || s.isEmpty() ? "-" : s;
    }

    private String elencoPuntato(String valore) {
        String testo = sicuro(valore);
        if ("-".equals(testo)) {
            return "-";
        }
        StringBuilder elenco = new StringBuilder();
        for (String riga : testo.split("\\R")) {
            if (!riga.isBlank()) {
                if (elenco.length() > 0) {
                    elenco.append('\n');
                }
                elenco.append("- ").append(riga.trim());
            }
        }
        return elenco.length() == 0 ? "-" : elenco.toString();
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
        SceneManager.getIstanza().switchTo("SchermataGestioneProfilo.fxml");
    }
}
