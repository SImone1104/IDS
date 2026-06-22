package it.afam;

import it.afam.consultazione.control.MostraContenutiLinkControl;
import it.afam.consultazione.interfaccia.SchermataContenutiTramiteLink;
import it.afam.gestioneOperazioniAutomatiche.ControlValiditaLink;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Punto di ingresso dell'applicazione AFAM Connect.
 * Inizializza il database, avvia il controllo automatico dei link scaduti e
 * mostra la schermata principale.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inizializza il database (crea afam.db e lo schema al primo avvio)
        DatabaseManager.getIstanza();

        // Avvia il modulo batch di controllo validità link (UC 4.5)
        avviaControlloValiditaLink();

        primaryStage.setTitle("AFAM Connect");
        SceneManager.getIstanza().setStage(primaryStage);

        String link = primoArgomentoValido(getParameters().getRaw());
        if (link != null) {
            // Found message (UC 6.1): link ricevuto dall'esterno (es. WhatsApp) all'avvio dell'app
            if (new MostraContenutiLinkControl().linkValido(link)) {
                SchermataContenutiTramiteLink.linkRicevuto = link;
                SceneManager.getIstanza().switchTo("SchermataContenutiTramiteLink.fxml");
            } else {
                // link scaduto o non valido: non si apre niente
                Platform.exit();
            }
        } else {
            SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
        }
    }

    /** Primo argomento non vuoto passato all'avvio (il link di condivisione ricevuto). */
    private String primoArgomentoValido(java.util.List<String> args) {
        for (String a : args) {
            if (a != null && !a.isBlank()) {
                return a.trim();
            }
        }
        return null;
    }

    /**
     * Esegue il controllo dei link scaduti all'avvio e poi ogni giorno alle 00:00,
     * come previsto dal Requisito di Controllo Temporale del RAD/SDD.
     */
    private void avviaControlloValiditaLink() {
        ControlValiditaLink control = new ControlValiditaLink();
        control.verificaValiditaLink(); // controllo immediato all'avvio

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "controllo-validita-link");
            t.setDaemon(true);
            return t;
        });
        long ritardoIniziale = Duration.between(
                LocalDateTime.now(),
                LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay()).getSeconds();
        scheduler.scheduleAtFixedRate(control::verificaValiditaLink,
                ritardoIniziale, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
