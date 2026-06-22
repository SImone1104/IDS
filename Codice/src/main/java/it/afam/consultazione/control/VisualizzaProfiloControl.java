package it.afam.consultazione.control;

import it.afam.consultazione.interfaccia.SchermataContenutiCondivisi;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SceneManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiCategoria;
import it.afam.utility.dto.DatiFile;

import java.util.List;

/** Consultazione del profilo: contenuti pubblici, privati tramite codice, visualizzazione (UC 5.2-5.4). */
public class VisualizzaProfiloControl {

    public List<DatiCategoria> mostraContenutiPubblici() {
        return DatabaseManager.getIstanza()
                .recuperaCategoriePubbliche(SessioneCorrente.getIstanza().getIdProfiloConsultato());
    }

    public void selezionaCategoria(int idCategoria) {
        SessioneCorrente.getIstanza().setIdCategoriaCorrente(idCategoria);
    }

    public List<DatiFile> selezionaTipologia(String tipologia) {
        SessioneCorrente.getIstanza().setTipologiaCorrente(tipologia);
        return DatabaseManager.getIstanza()
                .recuperaFilePubblici(SessioneCorrente.getIstanza().getIdCategoriaCorrente(), tipologia);
    }

    /** Verifica il codice di sblocco; se valido carica i file privati associati. */
    public boolean verificaCodiceSblocco(String codice) {
        int idProfilo = SessioneCorrente.getIstanza().getIdProfiloConsultato();
        if (!DatabaseManager.getIstanza().verificaCodiceSblocco(codice, idProfilo)) {
            return false;
        }
        SchermataContenutiCondivisi.contenuti = DatabaseManager.getIstanza().recuperaFilePerCodice(codice);
        return true;
    }

    /** Apre il visualizzatore o il riproduttore in base al tipo di contenuto (UC 5.4). */
    public void visualizzaContenuto(int idFile) {
        DatiFile f = DatabaseManager.getIstanza().recuperaFilePerId(idFile);
        if (f == null) {
            return;
        }
        SessioneCorrente.getIstanza().setIdFileCorrente(idFile);
        SessioneCorrente.getIstanza().setSchermataRitorno("SchermataCategoriaContenuti.fxml");
        apriVisualizzatore(f.tipologia());
    }

    static void apriVisualizzatore(String tipologia) {
        if ("Audio".equals(tipologia) || "Video".equals(tipologia)) {
            SceneManager.getIstanza().switchTo("SchermataRiproduttoreMultimediale.fxml");
        } else {
            SceneManager.getIstanza().switchTo("SchermataVisualizzatoreDocumenti.fxml");
        }
    }
}
