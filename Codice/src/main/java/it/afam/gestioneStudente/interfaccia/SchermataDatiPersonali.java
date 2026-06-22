package it.afam.gestioneStudente.interfaccia;

import it.afam.entity.EntityStudente;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneStudente.control.GestioneProfiloControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/** Visualizza i dati personali dello studente (UC 2.1). */
public class SchermataDatiPersonali {

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
            labelFoto.setText("Foto: " + (s.getPercorsoFoto() == null ? "(non impostata)" : s.getPercorsoFoto()));
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
}
