package it.afam.gestioneCondivisione.control;

import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiFile;

import java.util.List;

/** Selezione dei contenuti privati da condividere tramite codice (UC 7.1). */
public class SelezionaContenutiPrivatiControl {

    /** Solo i contenuti privati dello studente. */
    public List<DatiFile> caricaContenutiPrivati() {
        return DatabaseManager.getIstanza()
                .recuperaContenutiPrivati(SessioneCorrente.getIstanza().getIdStudente());
    }

    /** Memorizza i contenuti selezionati come contesto corrente. */
    public void confermaSelezione(int[] idContenuti) {
        SessioneCorrente.getIstanza().setContenutiSelezionati(idContenuti);
    }
}
