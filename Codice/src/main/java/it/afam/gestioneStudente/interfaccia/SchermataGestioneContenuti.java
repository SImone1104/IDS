package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneStudente.control.CategoriaControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiCategoria;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/** Elenco delle categorie artistiche (UC 3.1). */
public class SchermataGestioneContenuti {

    @FXML private ListView<DatiCategoria> listaCategorie;

    private final CategoriaControl control = new CategoriaControl();

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
            listaCategorie.setItems(FXCollections.observableArrayList(control.caricaCategorie()));
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
        SceneManager.getIstanza().switchTo("SchermataTipologiaFile.fxml");
    }

    @FXML
    private void cliccaCreaNuovaCategoria() {
        SceneManager.getIstanza().switchTo("SchermataNuovaCategoria.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("HomeAFAM.fxml");
    }
}
