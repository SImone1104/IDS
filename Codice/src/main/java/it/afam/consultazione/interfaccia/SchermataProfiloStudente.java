package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.VisualizzaProfiloControl;
import it.afam.entity.EntityStudente;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiBackground;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/** Profilo dello studente consultato: dati personali + background artistico (UC 5.1, Tabella 27). */
public class SchermataProfiloStudente {

    @FXML private ImageView immagineFoto;
    @FXML private Label labelNome;
    @FXML private Label labelCognome;
    @FXML private Label labelEmail;
    @FXML private Label labelTelefono;
    @FXML private ListView<DatiBackground> listaBackground;

    private final VisualizzaProfiloControl control = new VisualizzaProfiloControl();

    @FXML
    private void initialize() {
        listaBackground.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DatiBackground d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty || d == null ? null : riassunto(d));
            }
        });
        try {
            EntityStudente s = control.caricaProfilo();
            if (s != null) {
                labelNome.setText("Nome: " + s.getNome());
                labelCognome.setText("Cognome: " + s.getCognome());
                labelEmail.setText("E-mail: " + sicuro(s.getEmail()));
                labelTelefono.setText("Telefono: " + sicuro(s.getTelefono()));
                caricaFoto(s.getPercorsoFoto());
            }
            listaBackground.setItems(FXCollections.observableArrayList(control.caricaBackground()));
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    private void caricaFoto(String percorso) {
        if (percorso == null || percorso.isEmpty()) {
            return;
        }
        File f = new File(percorso);
        if (f.exists()) {
            immagineFoto.setImage(new Image(f.toURI().toString()));
        }
    }

    private String riassunto(DatiBackground d) {
        return "Scuola/Università: " + sicuro(d.scuolaUniversita())
                + " | Collaborazioni: " + sicuro(d.collaborazioniFatte())
                + " | Autori: " + sicuro(d.collaborazioniAutori())
                + " | Partecipazioni: " + sicuro(d.partecipazioni());
    }

    private String sicuro(String s) {
        return s == null || s.isEmpty() ? "-" : s;
    }

    @FXML
    private void cliccaMostraContenutiPubblici() {
        SceneManager.getIstanza().switchTo("SchermataContenutiPubblici.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataRisultatoRicerca.fxml");
    }
}
