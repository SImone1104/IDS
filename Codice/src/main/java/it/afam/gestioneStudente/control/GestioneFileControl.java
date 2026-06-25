package it.afam.gestioneStudente.control;

import it.afam.utility.ArchivioFile;
import it.afam.gestioneStudente.interfaccia.MessaggioDiConferma;
import it.afam.utility.DatabaseManager;
import it.afam.utility.dto.DatiFile;

/**
 * Gestisce Elimina File (UC 3.5), Modifica File (UC 3.6) e Gestisci Privacy (UC 3.7).
 * Singleton: il file selezionato deve sopravvivere fino alla conferma dell'azione.
 */
public class GestioneFileControl {

    private static GestioneFileControl istanza;

    private int idFileCorrente;

    private GestioneFileControl() { }

    public static GestioneFileControl getIstanza() {
        if (istanza == null) {
            istanza = new GestioneFileControl();
        }
        return istanza;
    }

    public void selezionaFile(int idFile) {
        this.idFileCorrente = idFile;
    }

    /** Mostra la conferma irreversibile; se confermata elimina il file. */
    public void eliminaFile() {
        if (MessaggioDiConferma.conferma("Eliminare definitivamente il file? L'operazione è irreversibile.")) {
            confermaEliminaFile();
        }
    }

    public void confermaEliminaFile() {
        DatabaseManager.getIstanza().eliminaFile(idFileCorrente);
    }

    public DatiFile caricaModificaFile() {
        return DatabaseManager.getIstanza().recuperaFilePerId(idFileCorrente);
    }

    public boolean modificaFile(String titolo, String descrizione, String percorsoFile) {
        if (titolo == null || titolo.isBlank() || titolo.length() > 200) {
            return false;
        }
        String percorsoArchiviato = ArchivioFile.archiviaContenuto(percorsoFile);
        DatabaseManager.getIstanza().aggiornaFile(idFileCorrente, titolo, descrizione, percorsoArchiviato);
        return true;
    }

    public String caricaPrivacy() {
        DatiFile f = DatabaseManager.getIstanza().recuperaFilePerId(idFileCorrente);
        return f == null ? null : f.privacy();
    }

    public void modificaPrivacy(String privacy) {
        DatabaseManager.getIstanza().aggiornaPrivacy(idFileCorrente, privacy);
    }
}
