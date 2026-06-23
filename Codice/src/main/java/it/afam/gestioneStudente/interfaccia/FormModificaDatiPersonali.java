package it.afam.gestioneStudente.interfaccia;

import it.afam.entity.EntityStudente;
import it.afam.gestioneStudente.control.GestioneProfiloControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/** Form di modifica dei dati personali (UC 2.1). */
public class FormModificaDatiPersonali {

    @FXML private TextField campoEmail;
    @FXML private TextField campoTelefono;
    @FXML private Label labelFoto;

    private String percorsoFoto;

    @FXML
    private void initialize() {
        try {
            EntityStudente s = GestioneProfiloControl.getIstanza().caricaDatiPersonali();
            campoEmail.setText(s.getEmail());
            campoTelefono.setText(s.getTelefono());
            percorsoFoto = s.getPercorsoFoto();
            if (percorsoFoto != null && !percorsoFoto.isEmpty()) {
                labelFoto.setText(percorsoFoto);
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaScegliFoto() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Scegli foto profilo");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"));
        var file = chooser.showOpenDialog(campoEmail.getScene().getWindow());
        if (file != null) {
            percorsoFoto = file.getAbsolutePath();
            labelFoto.setText(file.getName());
        }
    }

    @FXML
    private void cliccaConfermaModifiche() {
        try {
            boolean ok = GestioneProfiloControl.getIstanza()
                    .modificaDatiPersonali(campoEmail.getText(), campoTelefono.getText(), percorsoFoto);
            if (ok) {
                MessaggioDiConferma.mostra("Modifiche effettuate correttamente");
                SceneManager.getIstanza().switchTo("SchermataDatiPersonali.fxml");
            } else {
                MessaggioDiErrore.mostra("Dati non validi (controlla e-mail, telefono e formato foto)");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo("SchermataDatiPersonali.fxml");
    }
}
