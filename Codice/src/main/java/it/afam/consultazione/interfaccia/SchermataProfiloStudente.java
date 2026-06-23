package it.afam.consultazione.interfaccia;

import it.afam.consultazione.control.VisualizzaProfiloControl;
import it.afam.entity.EntityStudente;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiBackground;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Profilo dello studente consultato: dati personali + background artistico (UC 5.1, Tabella 27). */
public class SchermataProfiloStudente {

    private static final String TESTO_VUOTO = "Nessuna informazione inserita";

    @FXML private ImageView immagineFoto;
    @FXML private Label labelNome;
    @FXML private Label labelCognome;
    @FXML private Label labelEmail;
    @FXML private Label labelTelefono;
    @FXML private VBox contenitoreBackground;

    private final VisualizzaProfiloControl control = new VisualizzaProfiloControl();

    @FXML
    private void initialize() {
        try {
            EntityStudente s = control.caricaProfilo();
            if (s != null) {
                labelNome.setText("Nome: " + s.getNome());
                labelCognome.setText("Cognome: " + s.getCognome());
                labelEmail.setText("E-mail: " + sicuro(s.getEmail()));
                labelTelefono.setText("Telefono: " + sicuro(s.getTelefono()));
                caricaFoto(s.getPercorsoFoto());
            }
            contenitoreBackground.getChildren().setAll(
                    creaSchedaBackground(creaRiepilogoBackground(control.caricaBackground())));
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

    private DatiBackground creaRiepilogoBackground(List<DatiBackground> background) {
        return new DatiBackground(0,
                accorpaValori(background, CampoBackground.SCUOLA_UNIVERSITA),
                accorpaValori(background, CampoBackground.COLLABORAZIONI_FATTE),
                accorpaValori(background, CampoBackground.COLLABORAZIONI_AUTORI),
                accorpaValori(background, CampoBackground.PARTECIPAZIONI));
    }

    private String accorpaValori(List<DatiBackground> background, CampoBackground campo) {
        Set<String> valori = new LinkedHashSet<>();
        for (DatiBackground dato : background) {
            String valore = campo.leggi(dato);
            if (valore == null || valore.isBlank()) {
                continue;
            }
            for (String riga : valore.split("\\R")) {
                String testo = riga.trim();
                if (!testo.isBlank() && !"-".equals(testo) && !TESTO_VUOTO.equals(testo)) {
                    valori.add(testo);
                }
            }
        }
        return valori.isEmpty() ? null : String.join("\n", valori);
    }

    private VBox creaSchedaBackground(DatiBackground d) {
        VBox scheda = new VBox(8,
                creaRigaBackground("Scuola/Universita", d.scuolaUniversita()),
                creaRigaBackground("Collaborazioni fatte", d.collaborazioniFatte()),
                creaRigaBackground("Collaborazioni con autori", d.collaborazioniAutori()),
                creaRigaBackground("Partecipazioni", d.partecipazioni()));
        scheda.getStyleClass().add("background-card");
        return scheda;
    }

    private VBox creaRigaBackground(String titolo, String valore) {
        Label labelTitolo = new Label(titolo);
        labelTitolo.getStyleClass().add("background-card-title");

        Label labelValore = new Label(elencoPuntato(valore));
        labelValore.setWrapText(true);
        labelValore.getStyleClass().add("background-card-value");

        VBox riga = new VBox(3, labelTitolo, labelValore);
        riga.getStyleClass().add("background-card-row");
        return riga;
    }

    private String sicuro(String s) {
        return s == null || s.isBlank() ? TESTO_VUOTO : s;
    }

    private String elencoPuntato(String valore) {
        String testo = sicuro(valore);
        if (TESTO_VUOTO.equals(testo)) {
            return TESTO_VUOTO;
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
        return elenco.length() == 0 ? TESTO_VUOTO : elenco.toString();
    }

    private enum CampoBackground {
        SCUOLA_UNIVERSITA {
            @Override
            String leggi(DatiBackground dato) {
                return dato.scuolaUniversita();
            }
        },
        COLLABORAZIONI_FATTE {
            @Override
            String leggi(DatiBackground dato) {
                return dato.collaborazioniFatte();
            }
        },
        COLLABORAZIONI_AUTORI {
            @Override
            String leggi(DatiBackground dato) {
                return dato.collaborazioniAutori();
            }
        },
        PARTECIPAZIONI {
            @Override
            String leggi(DatiBackground dato) {
                return dato.partecipazioni();
            }
        };

        abstract String leggi(DatiBackground dato);
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
