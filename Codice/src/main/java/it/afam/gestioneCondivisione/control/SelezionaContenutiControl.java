package it.afam.gestioneCondivisione.control;

import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiFile;

import java.util.List;

/** Selezione dei contenuti da condividere tramite link (UC 4.1). */
public class SelezionaContenutiControl {

    /** Tutti i contenuti dello studente (pubblici e privati). */
    public List<DatiFile> caricaContenuti() {
        return DatabaseManager.getIstanza()
                .recuperaContenuti(SessioneCorrente.getIstanza().getIdStudente());
    }

    /** Memorizza i contenuti selezionati come contesto corrente. */
    public void confermaSelezione(int[] idContenuti) {
        SessioneCorrente.getIstanza().setContenutiSelezionati(idContenuti);
    }
}
