package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.VisualizzaProfiloControl;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiFile;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/** Tipologie ed elenco file pubblici della categoria; sblocco contenuti privati (UC 5.2-5.4, 5.3). */
public class SchermataCategoriaContenuti {

    @FXML private ListView<DatiFile> listaFile;

    private final VisualizzaProfiloControl control = new VisualizzaProfiloControl();

    @FXML
    private void initialize() {
        listaFile.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiFile f, boolean empty) {
                super.updateItem(f, empty);
                setText(empty || f == null ? null : f.titolo() + "  [" + f.tipologia() + "]");
            }
        });
    }

    @FXML private void cliccaImmagini() { mostraTipologia("Immagine"); }
    @FXML private void cliccaAudio() { mostraTipologia("Audio"); }
    @FXML private void cliccaVideo() { mostraTipologia("Video"); }
    @FXML private void cliccaDocumenti() { mostraTipologia("Documento"); }

    private void mostraTipologia(String tipologia) {
        try {
            listaFile.setItems(FXCollections.observableArrayList(control.selezionaTipologia(tipologia)));
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaVisualizza() {
        DatiFile sel = listaFile.getSelectionModel().getSelectedItem();
        if (sel == null) {
            MessaggioDiErrore.mostra("Seleziona un contenuto");
            return;
        }
        control.visualizzaContenuto(sel.id());
    }

    @FXML
    private void cliccaVisualizzaContenutiPrivati() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sblocco contenuti privati");
        dialog.setHeaderText(null);
        dialog.setContentText("Inserisci il codice di sblocco:");
        Optional<String> res = dialog.showAndWait();
        if (res.isEmpty()) {
            return;
        }
        try {
            if (control.verificaCodiceSblocco(res.get().trim())) {
                SceneManager.getIstanza().switchTo("SchermataContenutiCondivisi.fxml");
            } else {
                MessaggioDiErrore.mostra("Codice errato");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataContenutiPubblici.fxml");
    }
}
