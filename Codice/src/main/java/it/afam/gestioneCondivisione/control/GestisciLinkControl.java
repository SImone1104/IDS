package it.afam.gestioneCondivisione.control;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiConferma;
import it.afam.gestioneCondivisione.interfaccia.MessaggioConfermaLink;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.Utils;
import it.afam.utility.dto.DatiLink;

import java.util.List;

/**
 * Gestisce Genera Link (UC 4.2), Visualizza Elenco Link (UC 4.3) e Revoca Link (UC 4.4).
 * Singleton: il link selezionato per la revoca deve sopravvivere fino alla conferma.
 */
public class GestisciLinkControl {

    private static GestisciLinkControl istanza;

    private int idLinkCorrente;

    private GestisciLinkControl() { }

    public static GestisciLinkControl getIstanza() {
        if (istanza == null) {
            istanza = new GestisciLinkControl();
        }
        return istanza;
    }

    /** Genera un link univoco per i contenuti selezionati, lo salva e lo mostra. */
    public void generaLink() {
        String link = Utils.generaTokenLink();
        SessioneCorrente s = SessioneCorrente.getIstanza();
        DatabaseManager.getIstanza().salvaLink(link, s.getIdStudente(), s.getContenutiSelezionati());
        MessaggioConfermaLink.mostra(link);
    }

    public List<DatiLink> caricaElencoLink() {
        return DatabaseManager.getIstanza()
                .recuperaLink(SessioneCorrente.getIstanza().getIdStudente());
    }

    /** Mostra la conferma di revoca; se confermata revoca il link. */
    public void revocaLink(int idLink) {
        idLinkCorrente = idLink;
        if (MessaggioDiConferma.conferma("Sei sicuro di voler revocare questo link?")) {
            confermaRevocaLink();
        }
    }

    public void confermaRevocaLink() {
        DatabaseManager.getIstanza().aggiornaStatoLink(idLinkCorrente);
    }
}
