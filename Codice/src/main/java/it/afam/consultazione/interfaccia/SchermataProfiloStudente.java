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
                setWrapText(true);
                setPrefWidth(0);
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
    private void cliccaMostraContenutiPubblici() {
        SceneManager.getIstanza().switchTo("SchermataContenutiPubblici.fxml");
    }

    @FXML
    private void cliccaMostraContenutiPrivati() {
        SceneManager.getIstanza().switchTo("SchermataInserimentoCodiceSblocco.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataRisultatoRicerca.fxml");
    }
}
