package it.afam.gestioneAutenticazione.interfaccia;

import it.afam.utility.SceneManager;
import javafx.fxml.FXML;

/** Schermata principale: snodo verso login, registrazione e recupero password. */
public class SchermataPrincipale {

    @FXML
    private void cliccaLogin() {
        SceneManager.getIstanza().switchTo("SchermataLogin.fxml");
    }

    @FXML
    private void cliccaRegistrazione() {
        SceneManager.getIstanza().switchTo("SchermataRegistrazione.fxml");
    }

    @FXML
    private void cliccaRecuperaPassword() {
        SceneManager.getIstanza().switchTo("SchermataRecuperoPassword.fxml");
    }
}
