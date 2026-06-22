package it.afam.gestioneAutenticazione.control;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiConferma;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;

/**
 * Gestisce il caso d'uso Logout (UC 1.5): richiede conferma, interrompe la
 * sessione e riporta alla schermata principale.
 */
public class LogoutControl {

    /** Mostra la richiesta di conferma; se confermata effettua il logout. */
    public void richiediConfermaLogout() {
        if (MessaggioDiConferma.conferma("Sei sicuro di voler uscire?")) {
            effettuaLogout();
        }
    }

    /** Azzera la sessione e torna alla schermata principale. */
    public void effettuaLogout() {
        SessioneCorrente.getIstanza().pulisci();
        SceneManager.getIstanza().switchTo("SchermataPrincipale.fxml");
    }
}
