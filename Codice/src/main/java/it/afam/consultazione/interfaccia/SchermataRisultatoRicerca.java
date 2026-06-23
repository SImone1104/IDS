package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.CercaStudenteControl;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiStudente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.List;

/** Elenco degli studenti trovati (UC 5.1). */
public class SchermataRisultatoRicerca {

    /** Risultati impostati dalla SchermataRicerca prima della navigazione. */
    public static List<DatiStudente> risultati;

    @FXML private ListView<DatiStudente> listaStudenti;

    private final CercaStudenteControl control = new CercaStudenteControl();

    @FXML
    private void initialize() {
        listaStudenti.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiStudente s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? null : s.nome() + " " + s.cognome());
            }
        });
        if (risultati != null) {
            listaStudenti.setItems(FXCollections.observableArrayList(risultati));
        }
    }

    @FXML
    private void cliccaApriProfilo() {
        DatiStudente sel = listaStudenti.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona uno studente");
            return;
        }
        control.selezionaStudente(sel.id());
        SceneManager.getIstanza().switchTo("SchermataProfiloStudente.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataRicerca.fxml");
    }
}
