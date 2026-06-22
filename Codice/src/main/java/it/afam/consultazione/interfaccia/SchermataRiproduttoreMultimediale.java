package it.afam.consultazione.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.utility.ConnessioneException;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

/** Riproduttore per contenuti audio e video in streaming controllato (UC 5.4, 6.2). */
public class SchermataRiproduttoreMultimediale {

    @FXML private Label labelInfo;
    @FXML private MediaView riproduttore;

    private MediaPlayer player;

    @FXML
    private void initialize() {
        try {
            File f = DatabaseManager.getIstanza()
                    .recuperaFileFisico(SessioneCorrente.getIstanza().getIdFileCorrente());
            if (f != null && f.exists()) {
                labelInfo.setText(f.getName());
                Media media = new Media(f.toURI().toString());
                player = new MediaPlayer(media);
                riproduttore.setMediaPlayer(player);
                player.play();
            } else {
                labelInfo.setText("File non disponibile");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
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
