package it.afam.gestioneStudente.control;

import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;

import java.io.File;

/** Gestisce Upload File (UC 3.4) con le validazioni dei requisiti speciali del RAD. */
public class UploadFileControl {

    private static final long MAX_DIMENSIONE = 1024L * 1024L * 1024L; // 1 GB

    /** Valida e carica il file nella categoria/tipologia correnti. */
    public boolean caricaFile(String titolo, String descrizione, String percorsoFile) {
        if (titolo == null || titolo.isBlank() || titolo.length() > 200) {
            return false;
        }
        if (percorsoFile == null) {
            return false;
        }
        File f = new File(percorsoFile);
        if (!f.exists() || f.length() > MAX_DIMENSIONE) {
            return false;
        }
        SessioneCorrente sessione = SessioneCorrente.getIstanza();
        if (!formatoIdoneo(sessione.getTipologiaCorrente(), estensione(percorsoFile))) {
            return false;
        }
        DatabaseManager.getIstanza().salvaFile(
                sessione.getIdStudente(), sessione.getIdCategoriaCorrente(),
                sessione.getTipologiaCorrente(), titolo, descrizione, percorsoFile);
        return true;
    }

    private boolean formatoIdoneo(String tipologia, String estensione) {
        if (tipologia == null) {
            return false;
        }
        return switch (tipologia) {
            case "Immagine" -> estensione.equals("png") || estensione.equals("jpg") || estensione.equals("jpeg");
            case "Audio" -> estensione.equals("mp3") || estensione.equals("wav") || estensione.equals("m4a");
            case "Video" -> estensione.equals("mp4") || estensione.equals("avi") || estensione.equals("mov");
            case "Documento" -> estensione.equals("pdf");
            default -> false;
        };
    }

    private String estensione(String percorso) {
        int p = percorso.lastIndexOf('.');
        return p >= 0 ? percorso.substring(p + 1).toLowerCase() : "";
    }
}
