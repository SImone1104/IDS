package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.VisualizzaProfiloControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiCategoria;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/** Elenco delle categorie del profilo consultato (UC 5.2). Riusata per pubblici/privati. */
public class SchermataContenutiPubblici {

    @FXML private ListView<DatiCategoria> listaCategorie;

    private final VisualizzaProfiloControl control = new VisualizzaProfiloControl();

    @FXML
    private void initialize() {
        listaCategorie.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiCategoria c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.nome());
            }
        });
        try {
            listaCategorie.setItems(FXCollections.observableArrayList(control.mostraContenutiPubblici()));
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaApriCategoria() {
        DatiCategoria sel = listaCategorie.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona una categoria");
            return;
        }
        control.selezionaCategoria(sel.id());
        SceneManager.getIstanza().switchTo("SchermataCategoriaContenuti.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataProfiloStudente.fxml");
    }
}
