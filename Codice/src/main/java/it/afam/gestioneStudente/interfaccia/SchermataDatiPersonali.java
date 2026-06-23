package it.afam.gestioneStudente.interfaccia;

import it.afam.entity.EntityStudente;
import it.afam.gestioneStudente.control.GestioneProfiloControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/** Visualizza i dati personali dello studente (UC 2.1). */
public class SchermataDatiPersonali {

    @FXML private ImageView fotoProfilo;
    @FXML private Label labelNome;
    @FXML private Label labelCognome;
    @FXML private Label labelEmail;
    @FXML private Label labelTelefono;
    @FXML private Label labelFoto;

    @FXML
    private void initialize() {
        try {
            EntityStudente s = GestioneProfiloControl.getIstanza().caricaDatiPersonali();
            labelNome.setText("Nome: " + s.getNome());
            labelCognome.setText("Cognome: " + s.getCognome());
            labelEmail.setText("E-mail: " + s.getEmail());
            labelTelefono.setText("Telefono: " + (s.getTelefono() == null ? "" : s.getTelefono()));
            mostraFoto(s.getPercorsoFoto());
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita.");
        }
    }

    @FXML
    private void cliccaModificaDati() {
        SceneManager.getIstanza().switchTo("FormModificaDatiPersonali.fxml");
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataGestioneProfilo.fxml");
    }

    private void mostraFoto(String percorsoFoto) {
        if (percorsoFoto == null || percorsoFoto.isBlank()) {
            labelFoto.setText("Foto non impostata");
            fotoProfilo.setImage(null);
            return;
        }

        File file = new File(percorsoFoto);
        if (!file.exists()) {
            labelFoto.setText("Foto non disponibile");
            fotoProfilo.setImage(null);
            return;
        }

        Image immagine = new Image(file.toURI().toString(), 190, 190, true, true);
        fotoProfilo.setImage(immagine);
        labelFoto.setText("Foto profilo");
    }
}
