package it.afam.gestioneStudente.control;

import it.afam.utility.SessioneCorrente;

/** Gestisce Seleziona Tipologia File (UC 3.3). */
public class TipologiaFileControl {

    /** Memorizza la tipologia scelta come contesto corrente. */
    public void selezionaTipologia(String tipologia) {
        SessioneCorrente.getIstanza().setTipologiaCorrente(tipologia);
    }
}
