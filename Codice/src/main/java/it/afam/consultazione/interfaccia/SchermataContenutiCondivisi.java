package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.MostraContenutiLinkControl;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiFile;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.List;

/** Elenco dei file condivisi tramite link o codice (UC 6.1, 5.3). */
public class SchermataContenutiCondivisi {

    /** Contenuti impostati dal control prima della navigazione (link o codice). */
    public static List<DatiFile> contenuti;

    /** true se l'accesso avviene tramite link esterno: l'utente esterno può solo vedere questa schermata. */
    public static boolean accessoTramiteLink;

    @FXML private ListView<DatiFile> listaFile;
    @FXML private Button bottoneIndietro;

    private final MostraContenutiLinkControl control = new MostraContenutiLinkControl();

    @FXML
    private void initialize() {
        listaFile.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiFile f, boolean empty) {
                super.updateItem(f, empty);
                setText(empty || f == null ? null : f.titolo() + "  [" + f.tipologia() + "]");
            }
        });
        if (contenuti != null) {
            listaFile.setItems(FXCollections.observableArrayList(contenuti));
        }
        // accesso esterno tramite link: l'utente può solo consultare, niente navigazione indietro
        if (accessoTramiteLink) {
            bottoneIndietro.setVisible(false);
            bottoneIndietro.setManaged(false);
        }
    }

    @FXML
    private void cliccaVisualizza() {
        DatiFile sel = listaFile.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona un contenuto");
            return;
        }
        control.visualizzaContenutoTramiteLink(sel.id());
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataRicerca.fxml");
    }
}
