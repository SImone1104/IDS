package it.afam.consultazione.control;

import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiStudente;

import java.util.List;

/** Gestisce la ricerca di uno studente e l'apertura del suo profilo (UC 5.1). */
public class CercaStudenteControl {

    public List<DatiStudente> cercaStudente(String nome, String cognome) {
        return DatabaseManager.getIstanza().cercaStudenti(nome, cognome);
    }

    public void selezionaStudente(int idStudente) {
        SessioneCorrente.getIstanza().setIdProfiloConsultato(idStudente);
    }
}
