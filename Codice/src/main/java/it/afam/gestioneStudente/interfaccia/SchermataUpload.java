package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneStudente.control.UploadFileControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/** Caricamento di un nuovo file (UC 3.4). */
public class SchermataUpload {

    @FXML private TextField campoTitolo;
    @FXML private TextArea campoDescrizione;
    @FXML private Label labelFile;
    @FXML private ProgressIndicator indicatoreCaricamento;
    @FXML private Label labelCaricamento;
    @FXML private Button bottoneScegliFile;
    @FXML private Button bottoneConferma;
    @FXML private Button bottoneAnnulla;

    private final UploadFileControl control = new UploadFileControl();
    private String percorsoFile;

    @FXML
    private void cliccaScegliFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Scegli file da caricare");
        var file = chooser.showOpenDialog(campoTitolo.getScene().getWindow());
        if (file != null) {
            percorsoFile = file.getAbsolutePath();
            labelFile.setText(file.getName());
        }
    }

    @FXML
    private void cliccaConferma() {
        impostaCaricamento(true);
        String titolo = campoTitolo.getText();
        String descrizione = campoDescrizione.getText();
        String fileDaCaricare = percorsoFile;

        Task<Boolean> taskUpload = new Task<>() {
            @Override
            protected Boolean call() {
                return control.caricaFile(titolo, descrizione, fileDaCaricare);
            }
        };

        taskUpload.setOnSucceeded(event -> {
            impostaCaricamento(false);
            if (taskUpload.getValue()) {
                MessaggioDiConferma.mostra("File caricato con successo");
                SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
            } else {
                MessaggioDiErrore.mostra("Caricamento non riuscito: verifica titolo (max 200 caratteri), "
                        + "dimensione (max 1 GB) e formato idoneo alla tipologia");
            }
        });

        taskUpload.setOnFailed(event -> {
            impostaCaricamento(false);
            Throwable errore = taskUpload.getException();
            if (errore instanceof ConnessioneException) {
                MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
            } else {
                MessaggioDiErrore.mostra("Caricamento non riuscito. Riprova tra qualche secondo.");
            }
        });

        Thread threadUpload = new Thread(taskUpload, "upload-file-afam");
        threadUpload.setDaemon(true);
        threadUpload.start();
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo("SchermataElencoFile.fxml");
    }

    private void impostaCaricamento(boolean caricamento) {
        indicatoreCaricamento.setVisible(caricamento);
        indicatoreCaricamento.setManaged(caricamento);
        labelCaricamento.setVisible(caricamento);
        labelCaricamento.setManaged(caricamento);

        campoTitolo.setDisable(caricamento);
        campoDescrizione.setDisable(caricamento);
        bottoneScegliFile.setDisable(caricamento);
        bottoneConferma.setDisable(caricamento);
        bottoneAnnulla.setDisable(caricamento);
    }
}
