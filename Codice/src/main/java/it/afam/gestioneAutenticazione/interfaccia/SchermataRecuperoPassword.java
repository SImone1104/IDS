package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.gestioneAutenticazione.control.RecuperaPasswordControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/** Avvio del recupero password tramite e-mail (UC 1.4). */
public class SchermataRecuperoPassword {

    @FXML private TextField email;

    @FXML
    private void cliccaConferma() {
        try {
            if (RecuperaPasswordControl.getIstanza().verificaEmail(email.getText())) {
                SceneManager.getIstanza().switchTo("SchermataInserimentoCodice.fxml");
            } else {
                MessaggioDiErrore.mostra("Studente non trovato");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaIndietro() {
        SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
    }
}
