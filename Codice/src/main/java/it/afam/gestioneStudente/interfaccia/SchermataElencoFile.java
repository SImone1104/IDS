package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneStudente.control.GestioneFileControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiFile;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/** Elenco dei file della tipologia/categoria correnti (UC 3.3); accesso a upload e opzioni. */
public class SchermataElencoFile {

    @FXML private ListView<DatiFile> listaFile;

    @FXML
    private void initialize() {
        listaFile.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiFile f, boolean empty) {
                super.updateItem(f, empty);
                setText(empty || f == null ? null : f.titolo() + "  [" + f.privacy() + "]");
            }
        });
        try {
            SessioneCorrente s = SessioneCorrente.getIstanza();
            listaFile.setItems(FXCollections.observableArrayList(
                    DatabaseManager.getIstanza().recuperaFile(s.getIdCategoriaCorrente(), s.getTipologiaCorrente())));
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaUpload() {
        SceneManager.getIstanza().switchTo("SchermataUpload.fxml");
    }

    @FXML
    private void cliccaOpzioniFile() {
        DatiFile sel = listaFile.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona un file");
            return;
        }
        GestioneFileControl.getIstanza().selezionaFile(sel.id());
        SceneManager.getIstanza().switchTo("SchermataGestioneFile.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataTipologiaFile.fxml");
    }
}
