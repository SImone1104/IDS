package it.afam.gestioneStudente.control;

import it.afam.entity.EntityStudente;
import it.afam.gestioneStudente.interfaccia.MessaggioDiConferma;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.Utils;
import it.afam.utility.dto.DatiBackground;

import java.util.List;

/**
 * Gestisce i casi d'uso Modifica Dati Personali (2.1), Modifica/Aggiungi/Elimina
 * Background Artistico (2.2, 2.3, 2.4). Singleton: stato condiviso tra le schermate.
 */
public class GestioneProfiloControl {

    private static final int MAX_CARATTERI = 700;

    private static GestioneProfiloControl istanza;

    private int idInformazioneCorrente;
    private boolean aggiunta;                 // true = aggiunta, false = modifica
    private DatiBackground backgroundCorrente; // per il prefill del form in modifica

    private GestioneProfiloControl() { }

    public static GestioneProfiloControl getIstanza() {
        if (istanza == null) {
            istanza = new GestioneProfiloControl();
        }
        return istanza;
    }

    // ---- Dati personali (UC 2.1) ----

    public EntityStudente caricaDatiPersonali() {
        return DatabaseManager.getIstanza()
                .recuperaDatiPersonali(SessioneCorrente.getIstanza().getIdStudente());
    }

    public boolean modificaDatiPersonali(String email, String telefono, String percorsoFoto) {
        if (!Utils.validaEmail(email) || !Utils.validaTelefono(telefono) || !validaFoto(percorsoFoto)) {
            return false;
        }
        DatabaseManager.getIstanza().aggiornaDatiPersonali(
                SessioneCorrente.getIstanza().getIdStudente(), email, telefono, percorsoFoto);
        return true;
    }

    private boolean validaFoto(String percorso) {
        if (percorso == null || percorso.isEmpty()) {
            return true; // foto facoltativa
        }
        String p = percorso.toLowerCase();
        return p.endsWith(".png") || p.endsWith(".jpg") || p.endsWith(".jpeg");
    }

    // ---- Background artistico (UC 2.2, 2.3, 2.4) ----

    public List<DatiBackground> caricaBackgroundArtistico() {
        return DatabaseManager.getIstanza()
                .recuperaBackground(SessioneCorrente.getIstanza().getIdStudente());
    }

    /** Prepara il form per una nuova voce. */
    public void preparaAggiunta() {
        aggiunta = true;
        idInformazioneCorrente = 0;
        backgroundCorrente = null;
    }

    /** Prepara il form per la modifica di una voce esistente. */
    public void preparaModifica(DatiBackground dato) {
        aggiunta = false;
        idInformazioneCorrente = dato.id();
        backgroundCorrente = dato;
    }

    public boolean isAggiunta() { return aggiunta; }
    public DatiBackground getBackgroundCorrente() { return backgroundCorrente; }

    public boolean aggiungiBackground(DatiBackground dato) {
        if (!validaLunghezze(dato)) {
            return false;
        }
        DatabaseManager.getIstanza()
                .aggiungiBackground(SessioneCorrente.getIstanza().getIdStudente(), dato);
        return true;
    }

    public boolean modificaBackground(int idInformazione, DatiBackground dato) {
        if (!validaLunghezze(dato)) {
            return false;
        }
        DatabaseManager.getIstanza().aggiornaBackground(idInformazione, dato);
        return true;
    }

    /** Mostra la conferma di eliminazione; se confermata elimina la voce. */
    public void eliminaBackground(int idInformazione) {
        idInformazioneCorrente = idInformazione;
        if (MessaggioDiConferma.conferma("Sei sicuro di eliminare questa informazione?")) {
            confermaEliminaBackground();
        }
    }

    public void confermaEliminaBackground() {
        DatabaseManager.getIstanza()
                .eliminaBackground(idInformazioneCorrente, SessioneCorrente.getIstanza().getIdStudente());
    }

    private boolean validaLunghezze(DatiBackground d) {
        return lunghezzaOk(d.scuolaUniversita()) && lunghezzaOk(d.collaborazioniFatte())
                && lunghezzaOk(d.collaborazioniAutori()) && lunghezzaOk(d.partecipazioni());
    }

    private boolean lunghezzaOk(String s) {
        return s == null || s.length() <= MAX_CARATTERI;
    }
}
