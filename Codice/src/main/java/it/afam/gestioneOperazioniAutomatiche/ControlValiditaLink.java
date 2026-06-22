package it.afam.gestioneOperazioniAutomatiche;

import it.afam.utility.DatabaseManager;
import it.afam.utility.dto.DatiLink;

import java.time.LocalDate;

/**
 * Controllo automatico della validità temporale dei link (UC 4.5).
 * Invocato dal trigger temporale: disattiva i link la cui data di scadenza è
 * stata superata.
 */
public class ControlValiditaLink {

    private final BoundaryTempo boundaryTempo = new BoundaryTempo();

    public void verificaValiditaLink() {
        LocalDate oggi = boundaryTempo.ottieniDataCorrente();
        for (DatiLink link : DatabaseManager.getIstanza().recuperaLinkConScadenza()) {
            LocalDate scadenza = LocalDate.parse(link.scadenza());
            if (oggi.isAfter(scadenza)) {
                DatabaseManager.getIstanza().disattivaLink(link.id());
            }
        }
    }
}
