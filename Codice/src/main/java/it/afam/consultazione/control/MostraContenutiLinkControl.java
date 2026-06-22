package it.afam.consultazione.control;

import it.afam.consultazione.interfaccia.SchermataContenutiCondivisi;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiFile;

/** Accesso ai contenuti tramite link di condivisione (UC 6.1, 6.2). */
public class MostraContenutiLinkControl {

    /** Vero se il link esiste ed è ancora attivo (non scaduto/revocato). */
    public boolean linkValido(String link) {
        return DatabaseManager.getIstanza().verificaStatoLink(link);
    }

    /** Risolve il link: se valido carica i file e mostra la schermata Contenuti Condivisi. */
    public void mostraContenutiTramiteLink(String link) {
        if (!DatabaseManager.getIstanza().verificaStatoLink(link)) {
            MessaggioDiErrore.mostra("Link non valido o scaduto");
            return;
        }
        SchermataContenutiCondivisi.contenuti = DatabaseManager.getIstanza().recuperaFilePerLink(link);
        SchermataContenutiCondivisi.accessoTramiteLink = true; // accesso esterno: nessuna navigazione indietro
        SceneManager.getIstanza().switchTo("SchermataContenutiCondivisi.fxml");
    }

    /** Apre il visualizzatore/riproduttore per un contenuto condiviso (UC 6.2). */
    public void visualizzaContenutoTramiteLink(int idFile) {
        DatiFile f = DatabaseManager.getIstanza().recuperaFilePerId(idFile);
        if (f == null) {
            return;
        }
        SessioneCorrente.getIstanza().setIdFileCorrente(idFile);
        SessioneCorrente.getIstanza().setSchermataRitorno("SchermataContenutiCondivisi.fxml");
        VisualizzaProfiloControl.apriVisualizzatore(f.tipologia());
    }
}
