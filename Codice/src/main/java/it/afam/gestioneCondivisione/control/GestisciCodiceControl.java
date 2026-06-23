package it.afam.gestioneCondivisione.control;

import it.afam.gestioneCondivisione.interfaccia.MessaggioDiConferma;
import it.afam.gestioneCondivisione.interfaccia.SchermataCodice;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.Utils;
import it.afam.utility.dto.DatiCodice;

import java.util.List;

/**
 * Gestisce Genera Codice (UC 7.2), Visualizza Elenco Codici (UC 7.3) e Revoca Codice (UC 7.4).
 * Singleton: il codice selezionato per la revoca deve sopravvivere fino alla conferma.
 */
public class GestisciCodiceControl {

    private static GestisciCodiceControl istanza;

    private int idCodiceCorrente;

    private GestisciCodiceControl() { }

    public static GestisciCodiceControl getIstanza() {
        if (istanza == null) {
            istanza = new GestisciCodiceControl();
        }
        return istanza;
    }

    /** Genera un codice univoco per i contenuti privati selezionati, lo salva e lo mostra. */
    public void generaCodice() {
        String codice = Utils.generaCodiceCondivisione();
        SessioneCorrente s = SessioneCorrente.getIstanza();
        DatabaseManager.getIstanza().salvaCodice(codice, s.getIdStudente(), s.getContenutiSelezionati());
        SchermataCodice.mostra(codice);
    }

    public List<DatiCodice> caricaElencoCodici() {
        return DatabaseManager.getIstanza()
                .recuperaCodici(SessioneCorrente.getIstanza().getIdStudente());
    }

    /** Mostra la conferma di revoca; se confermata revoca il codice. */
    public void revocaCodice(int idCodice) {
        idCodiceCorrente = idCodice;
        if (MessaggioDiConferma.conferma("Sei sicuro di voler revocare questo codice?")) {
            confermaRevocaCodice();
        }
    }

    public void confermaRevocaCodice() {
        DatabaseManager.getIstanza().aggiornaStatoCodice(idCodiceCorrente);
    }
}
