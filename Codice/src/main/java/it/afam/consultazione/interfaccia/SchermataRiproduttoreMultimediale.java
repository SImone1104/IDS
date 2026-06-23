package it.afam.consultazione.interfaccia;

import it.afam.utility.ConnessioneException;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiFile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;

/**
 * Riproduttore audio/video in streaming controllato (UC 5.4, 6.2) con
 * timeline (seek), pulsante Play/Pausa e indicatore di tempo. Anti-download:
 * nessuna funzione di salvataggio del file.
 */
public class SchermataRiproduttoreMultimediale {

    @FXML private Label labelTitolo;
    @FXML private Label labelDescrizione;
    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private MediaView riproduttore;
    @FXML private VBox pannelloAudio;
    @FXML private Button bottonePlayPause;
    @FXML private Slider sliderTempo;
    @FXML private Label labelTempo;

    private MediaPlayer player;

    @FXML
    private void initialize() {
        try {
            DatiFile dati = DatabaseManager.getIstanza()
                    .recuperaFilePerId(SessioneCorrente.getIstanza().getIdFileCorrente());
            if (dati == null) {
                labelTitolo.setText("File non disponibile");
                return;
            }
            labelTitolo.setText(dati.titolo());
            labelDescrizione.setText(dati.descrizione() == null ? "" : dati.descrizione());
            File f = new File(dati.percorso());
            if (!f.exists()) {
                labelTitolo.setText(dati.titolo() + " (file non trovato)");
                return;
            }
            configuraVista(dati.tipologia());
            Media media = new Media(f.toURI().toString());
            player = new MediaPlayer(media);
            riproduttore.setMediaPlayer(player);
            impostaCaricamento(true);
            configuraControlli();
            player.play();
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    private void configuraVista(String tipologia) {
        boolean audio = "Audio".equals(tipologia);
        pannelloAudio.setVisible(audio);
        pannelloAudio.setManaged(audio);
        riproduttore.setVisible(!audio);
        riproduttore.setManaged(!audio);
    }

    private void configuraControlli() {
        // imposta la durata massima della timeline quando è nota
        player.setOnReady(() -> {
            impostaCaricamento(false);
            sliderTempo.setMax(player.getTotalDuration().toSeconds());
            aggiornaTempo();
        });
        player.setOnError(() -> {
            impostaCaricamento(false);
            labelTitolo.setText(labelTitolo.getText() + " (contenuto non caricabile)");
        });

        // avanzamento automatico della timeline durante la riproduzione
        player.currentTimeProperty().addListener((obs, vecchio, corrente) -> {
            if (!sliderTempo.isValueChanging()) {
                sliderTempo.setValue(corrente.toSeconds());
            }
            aggiornaTempo();
        });

        // seek: trascinamento dello slider
        sliderTempo.valueChangingProperty().addListener((obs, prima, inCambiamento) -> {
            if (!inCambiamento) {
                player.seek(Duration.seconds(sliderTempo.getValue()));
            }
        });
        // seek: click sulla barra
        sliderTempo.setOnMouseReleased(e -> player.seek(Duration.seconds(sliderTempo.getValue())));

        // sincronizza il testo del pulsante con lo stato
        player.statusProperty().addListener((obs, vecchio, stato) -> {
            if (stato == MediaPlayer.Status.PLAYING) {
                bottonePlayPause.setText("Pausa");
            } else {
                bottonePlayPause.setText("Play");
            }
        });

        // a fine video: ferma e torna all'inizio
        player.setOnEndOfMedia(() -> {
            player.pause();
            player.seek(Duration.ZERO);
        });
    }

    @FXML
    private void cliccaPlayPause() {
        if (player == null) {
            return;
        }
        if (player.getStatus() == MediaPlayer.Status.PLAYING) {
            player.pause();
        } else {
            player.play();
        }
    }

    private void aggiornaTempo() {
        if (player == null) {
            return;
        }
        Duration corrente = player.getCurrentTime();
        Duration totale = player.getTotalDuration();
        labelTempo.setText(formato(corrente) + " / "
                + (totale == null || totale.isUnknown() ? "--:--" : formato(totale)));
    }

    private void impostaCaricamento(boolean caricamento) {
        indicatoreCaricamento.setVisible(caricamento);
        indicatoreCaricamento.setManaged(caricamento);
        labelCaricamento.setVisible(caricamento);
        labelCaricamento.setManaged(caricamento);

        bottonePlayPause.setDisable(caricamento);
        sliderTempo.setDisable(caricamento);
    }

    private String formato(Duration d) {
        int secondiTotali = (int) Math.floor(d.toSeconds());
        return String.format("%02d:%02d", secondiTotali / 60, secondiTotali % 60);
    }

    @FXML
    private void cliccaChiudi() {
        if (player != null) {
            player.stop();
            player.dispose();
        }
        SceneManager.getIstanza().switchTo(SessioneCorrente.getIstanza().getSchermataRitorno());
    }
}
