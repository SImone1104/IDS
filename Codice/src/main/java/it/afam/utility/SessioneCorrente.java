package it.afam.utility;

/**
 * Singleton che mantiene lo stato della sessione: identificativo dello studente
 * autenticato e contesto di navigazione (categoria, tipologia, file, profilo
 * consultato, contenuti selezionati). Azzerato al logout.
 */
public class SessioneCorrente {

    private static SessioneCorrente istanza;

    private int idStudente;
    private int idCategoriaCorrente;
    private String tipologiaCorrente;
    private int idFileCorrente;
    private int idProfiloConsultato;
    private int[] contenutiSelezionati;
    private String modalitaSelezione;   // "LINK" o "CODICE": contesto di SchermataSelezionaContenuti
    private String schermataRitorno;    // schermata a cui tornare dai visualizzatori di contenuto

    private SessioneCorrente() { }

    public static SessioneCorrente getIstanza() {
        if (istanza == null) {
            istanza = new SessioneCorrente();
        }
        return istanza;
    }

    public int getIdStudente() { return idStudente; }
    public void setIdStudente(int idStudente) { this.idStudente = idStudente; }

    public int getIdCategoriaCorrente() { return idCategoriaCorrente; }
    public void setIdCategoriaCorrente(int idCategoria) { this.idCategoriaCorrente = idCategoria; }

    public String getTipologiaCorrente() { return tipologiaCorrente; }
    public void setTipologiaCorrente(String tipologia) { this.tipologiaCorrente = tipologia; }

    public int getIdFileCorrente() { return idFileCorrente; }
    public void setIdFileCorrente(int idFile) { this.idFileCorrente = idFile; }

    public int getIdProfiloConsultato() { return idProfiloConsultato; }
    public void setIdProfiloConsultato(int idProfilo) { this.idProfiloConsultato = idProfilo; }

    public int[] getContenutiSelezionati() { return contenutiSelezionati; }
    public void setContenutiSelezionati(int[] idContenuti) { this.contenutiSelezionati = idContenuti; }

    public String getModalitaSelezione() { return modalitaSelezione; }
    public void setModalitaSelezione(String modalita) { this.modalitaSelezione = modalita; }

    public String getSchermataRitorno() { return schermataRitorno; }
    public void setSchermataRitorno(String schermata) { this.schermataRitorno = schermata; }

    /** Azzera tutti i dati della sessione (logout). */
    public void pulisci() {
        idStudente = 0;
        idCategoriaCorrente = 0;
        tipologiaCorrente = null;
        idFileCorrente = 0;
        idProfiloConsultato = 0;
        contenutiSelezionati = null;
        modalitaSelezione = null;
    }
}
