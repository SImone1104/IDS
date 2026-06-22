package it.afam.gestioneOperazioniAutomatiche;

import java.time.LocalDate;

/**
 * Interfaccia verso l'attore Tempo (orologio di sistema): fornisce la data
 * corrente necessaria alla verifica della validità dei link (UC 4.5).
 */
public class BoundaryTempo {

    public LocalDate ottieniDataCorrente() {
        return LocalDate.now();
    }
}
