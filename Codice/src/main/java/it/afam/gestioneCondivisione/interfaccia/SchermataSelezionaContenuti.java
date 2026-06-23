package it.afam.gestioneCondivisione.interfaccia;

import it.afam.gestioneCondivisione.control.SelezionaContenutiControl;
import it.afam.gestioneCondivisione.control.SelezionaContenutiPrivatiControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiFile;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.List;

/** Selezione (multipla) dei contenuti da condividere; riusata per link e codice (UC 4.1 / 7.1). */
public class SchermataSelezionaContenuti {

    @FXML private ListView<DatiFile> listaContenuti;

    private boolean modalitaCodice;

    @FXML
    private void initialize() {
        modalitaCodice = "CODICE".equals(SessioneCorrente.getIstanza().getModalitaSelezione());
        listaContenuti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listaContenuti.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiFile f, boolean empty) {
                super.updateItem(f, empty);
                setText(empty || f == null ? null
                        : f.titolo() + "  [" + f.categoria() + " - " + f.tipologia() + ", " + f.privacy() + "]");
            }
        });
        try {
            List<DatiFile> contenuti = modalitaCodice
                    ? new SelezionaContenutiPrivatiControl().caricaContenutiPrivati()
                    : new SelezionaContenutiControl().caricaContenuti();
            listaContenuti.setItems(FXCollections.observableArrayList(contenuti));
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaConfermaSelezione() {
        List<DatiFile> selezionati = listaContenuti.getSelectionModel().getSelectedItems();
        if (selezionati.isEmpty()) {
            MessaggioDiErrore.mostra("Seleziona almeno un contenuto");
            return;
        }
        int[] ids = selezionati.stream().mapToInt(DatiFile::id).toArray();
        if (modalitaCodice) {
            new SelezionaContenutiPrivatiControl().confermaSelezione(ids);
            SceneManager.getIstanza().switchTo("SchermataGenerazioneCodice.fxml");
        } else {
            new SelezionaContenutiControl().confermaSelezione(ids);
            SceneManager.getIstanza().switchTo("SchermataGenerazioneLink.fxml");
        }
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo(
                modalitaCodice ? "SchermataGestioneCondInterna.fxml" : "SchermataGestioneCondLink.fxml");
    }
}
