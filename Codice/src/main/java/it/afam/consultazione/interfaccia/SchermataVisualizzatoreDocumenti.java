package it.afam.consultazione.interfaccia;

import it.afam.utility.ConnessioneException;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiFile;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.IOException;

/** Visualizzatore inline per documenti PDF e immagini (UC 5.4, 6.2). Mostra titolo e descrizione. */
public class SchermataVisualizzatoreDocumenti {

    private static final float DPI_PDF = 120f;
    private static final double LARGHEZZA_CONTENUTO = 690d;

    @FXML private Label labelTitolo;
    @FXML private Label labelDescrizione;
    @FXML private VBox contenitorePagine;

    @FXML
    private void initialize() {
        try {
            DatiFile f = DatabaseManager.getIstanza()
                    .recuperaFilePerId(SessioneCorrente.getIstanza().getIdFileCorrente());
            if (f == null) {
                labelTitolo.setText("File non disponibile");
                return;
            }
            labelTitolo.setText(f.titolo());
            labelDescrizione.setText(f.descrizione() == null ? "" : f.descrizione());

            File file = new File(f.percorso());
            if (!file.exists()) {
                mostraMessaggio("File non trovato.");
                return;
            }
            if ("pdf".equalsIgnoreCase(estensione(file))) {
                mostraPdfInline(file);
            } else {
                mostraImmagine(file);
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    private void mostraPdfInline(File file) {
        try (PDDocument documento = PDDocument.load(file)) {
            PDFRenderer renderer = new PDFRenderer(documento);
            for (int i = 0; i < documento.getNumberOfPages(); i++) {
                Image pagina = SwingFXUtils.toFXImage(
                        renderer.renderImageWithDPI(i, DPI_PDF, ImageType.RGB), null);
                contenitorePagine.getChildren().add(creaImageView(pagina));
            }
            if (documento.getNumberOfPages() == 0) {
                mostraMessaggio("PDF senza pagine visualizzabili.");
            }
        } catch (IOException e) {
            mostraMessaggio("Impossibile visualizzare il PDF.");
        }
    }

    private void mostraImmagine(File file) {
        contenitorePagine.getChildren().add(creaImageView(new Image(file.toURI().toString())));
    }

    private ImageView creaImageView(Image immagine) {
        ImageView vista = new ImageView(immagine);
        vista.setPreserveRatio(true);
        vista.setFitWidth(LARGHEZZA_CONTENUTO);
        return vista;
    }

    private void mostraMessaggio(String testo) {
        contenitorePagine.getChildren().setAll(new Label(testo));
    }

    private String estensione(File file) {
        String nome = file.getName();
        int punto = nome.lastIndexOf('.');
        return punto >= 0 ? nome.substring(punto + 1) : "";
    }

    @FXML
    private void cliccaChiudi() {
        SceneManager.getIstanza().switchTo(SessioneCorrente.getIstanza().getSchermataRitorno());
    }
}
