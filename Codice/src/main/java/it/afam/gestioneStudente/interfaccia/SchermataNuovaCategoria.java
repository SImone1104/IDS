package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiConferma;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneStudente.control.CategoriaControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/** Creazione di una nuova categoria artistica (UC 3.2). */
public class SchermataNuovaCategoria {

    @FXML private TextField campoNomeCategoria;

    private final CategoriaControl control = new CategoriaControl();

    @FXML
    private void cliccaSalva() {
        try {
            if (control.creaCategoria(campoNomeCategoria.getText())) {
                MessaggioDiConferma.mostra("Categoria Artistica creata con successo");
                SceneManager.getIstanza().switchTo("SchermataTipologiaFile.fxml");
            } else {
                MessaggioDiErrore.mostra("Nome non valido o categoria già esistente");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo("SchermataGestioneContenuti.fxml");
    }
}
