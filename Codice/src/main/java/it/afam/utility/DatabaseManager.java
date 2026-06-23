package it.afam.utility;

import it.afam.entity.EntityStudente;
import it.afam.utility.dto.DatiBackground;
import it.afam.utility.dto.DatiCategoria;
import it.afam.utility.dto.DatiCodice;
import it.afam.utility.dto.DatiFile;
import it.afam.utility.dto.DatiLink;
import it.afam.utility.dto.DatiStudente;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Repository: unico punto di accesso ai dati per tutti i sotto-sistemi.
 * Gestisce la connessione JDBC a SQLite (tramite BoundaryDBMS), inizializza lo
 * schema al primo avvio ed espone i metodi DAO di dominio.
 *
 * NOTA: i metodi DAO vengono aggiunti per sotto-sistema man mano che si
 * implementano le rispettive fette. Qui sono presenti quelli di gestioneAutenticazione.
 */
public class DatabaseManager {

    private static DatabaseManager istanza;
    private final BoundaryDBMS boundaryDBMS;

    private DatabaseManager() {
        this.boundaryDBMS = new BoundaryDBMS();
        inizializzaSchema();
    }

    public static DatabaseManager getIstanza() {
        if (istanza == null) {
            istanza = new DatabaseManager();
        }
        return istanza;
    }

    /** Crea le tabelle (se non esistono) eseguendo schema.sql. */
    private void inizializzaSchema() {
        try (InputStream in = getClass().getResourceAsStream("/schema.sql")) {
            if (in == null) {
                throw new ConnessioneException("schema.sql non trovato nelle risorse");
            }
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String riga;
                while ((riga = br.readLine()) != null) {
                    if (!riga.trim().startsWith("--")) {
                        sb.append(riga).append('\n');
                    }
                }
            }
            try (Statement st = boundaryDBMS.apriConnessione().createStatement()) {
                for (String comando : sb.toString().split(";")) {
                    if (!comando.trim().isEmpty()) {
                        st.execute(comando);
                    }
                }
            }
        } catch (SQLException | java.io.IOException e) {
            throw new ConnessioneException("Errore durante l'inizializzazione dello schema", e);
        }
    }

    // ============================================================
    //  gestioneAutenticazione
    // ============================================================

    /** Verifica le credenziali; restituisce lo studente se corrette, altrimenti null. */
    public EntityStudente verificaCredenziali(String email, String passwordHash) {
        String sql = "SELECT * FROM Studente WHERE email = ? AND password = ?";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{email, passwordHash})) {
            if (rs.next()) {
                return mappaStudente(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante la verifica delle credenziali", e);
        }
    }

    /** Vero se esiste già uno studente con quell'email. */
    public boolean emailEsistente(String email) {
        String sql = "SELECT 1 FROM Studente WHERE email = ?";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{email})) {
            return rs.next();
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante la verifica dell'email", e);
        }
    }

    /** Salva un nuovo studente (la password deve essere già hashata). */
    public void salvaStudente(EntityStudente studente) {
        String sql = "INSERT INTO Studente (nome, cognome, codice_fiscale, email, telefono, percorso_foto, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{
                studente.getNome(), studente.getCognome(), studente.getCodiceFiscale(),
                studente.getEmail(), studente.getTelefono(), studente.getPercorsoFoto(),
                studente.getPassword()
        });
    }

    /** Aggiorna la password (hashata) dello studente con quell'email. */
    public void aggiornaPassword(String email, String hashPassword) {
        String sql = "UPDATE Studente SET password = ? WHERE email = ?";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{hashPassword, email});
    }

    /** Restituisce lo studente con quell'email, o null. */
    public EntityStudente recuperaStudentePerEmail(String email) {
        String sql = "SELECT * FROM Studente WHERE email = ?";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{email})) {
            return rs.next() ? mappaStudente(rs) : null;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dello studente", e);
        }
    }

    // ============================================================
    //  gestioneStudente - Gestione Profilo
    // ============================================================

    /** Recupera i dati anagrafici dello studente. */
    public EntityStudente recuperaDatiPersonali(int idStudente) {
        String sql = "SELECT * FROM Studente WHERE id_studente = ?";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idStudente})) {
            return rs.next() ? mappaStudente(rs) : null;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei dati personali", e);
        }
    }

    /** Aggiorna email, telefono e foto dello studente. */
    public void aggiornaDatiPersonali(int idStudente, String email, String telefono, String percorsoFoto) {
        String sql = "UPDATE Studente SET email = ?, telefono = ?, percorso_foto = ? WHERE id_studente = ?";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{email, telefono, percorsoFoto, idStudente});
    }

    /** Recupera le voci di background artistico dello studente. */
    public List<DatiBackground> recuperaBackground(int idStudente) {
        String sql = "SELECT * FROM Background_Artistico WHERE id_studente = ?";
        List<DatiBackground> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idStudente})) {
            while (rs.next()) {
                lista.add(new DatiBackground(
                        rs.getInt("id_background"),
                        rs.getString("scuola_universita"),
                        rs.getString("collaborazioni_fatte"),
                        rs.getString("collaborazioni_autori"),
                        rs.getString("partecipazioni")));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero del background", e);
        }
    }

    /** Aggiunge una voce di background artistico. */
    public void aggiungiBackground(int idStudente, DatiBackground d) {
        String sql = "INSERT INTO Background_Artistico "
                + "(scuola_universita, collaborazioni_fatte, collaborazioni_autori, partecipazioni, id_studente) "
                + "VALUES (?, ?, ?, ?, ?)";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{
                d.scuolaUniversita(), d.collaborazioniFatte(), d.collaborazioniAutori(),
                d.partecipazioni(), idStudente});
    }

    /** Aggiorna una voce di background artistico esistente. */
    public void aggiornaBackground(int idInformazione, DatiBackground d) {
        String sql = "UPDATE Background_Artistico SET scuola_universita = ?, collaborazioni_fatte = ?, "
                + "collaborazioni_autori = ?, partecipazioni = ? WHERE id_background = ?";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{
                d.scuolaUniversita(), d.collaborazioniFatte(), d.collaborazioniAutori(),
                d.partecipazioni(), idInformazione});
    }

    /** Elimina una voce di background artistico. */
    public void eliminaBackground(int idInformazione, int idStudente) {
        String sql = "DELETE FROM Background_Artistico WHERE id_background = ? AND id_studente = ?";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{idInformazione, idStudente});
    }

    // ============================================================
    //  gestioneStudente - Gestione Contenuti
    // ============================================================

    public List<DatiCategoria> recuperaCategorie(int idStudente) {
        String sql = "SELECT id_categoria, nome_categoria FROM Categoria_Artistica WHERE id_studente = ?";
        List<DatiCategoria> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idStudente})) {
            while (rs.next()) {
                lista.add(new DatiCategoria(rs.getInt("id_categoria"), rs.getString("nome_categoria")));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero delle categorie", e);
        }
    }

    public boolean categoriaEsistente(String nome, int idStudente) {
        String sql = "SELECT 1 FROM Categoria_Artistica WHERE nome_categoria = ? AND id_studente = ?";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{nome, idStudente})) {
            return rs.next();
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante la verifica della categoria", e);
        }
    }

    public void salvaCategoria(String nome, int idStudente) {
        String sql = "INSERT INTO Categoria_Artistica (nome_categoria, id_studente) VALUES (?, ?)";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{nome, idStudente});
    }

    public List<DatiFile> recuperaFile(int idCategoria, String tipologia) {
        String sql = "SELECT * FROM Contenuto_File WHERE id_categoria = ? AND tipologia = ?";
        List<DatiFile> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idCategoria, tipologia})) {
            while (rs.next()) {
                lista.add(mappaFile(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei file", e);
        }
    }

    /** Salva un nuovo file; formato e dimensione sono derivati dal file scelto, privacy iniziale Privato. */
    public void salvaFile(int idStudente, int idCategoria, String tipologia,
                          String titolo, String descrizione, String percorsoFile) {
        String sql = "INSERT INTO Contenuto_File "
                + "(titolo, descrizione, tipologia, formato, dimensione, stato_privacy, percorso_fisico, id_categoria) "
                + "VALUES (?, ?, ?, ?, ?, 'Privato', ?, ?)";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{
                titolo, descrizione, tipologia, estensione(percorsoFile),
                new File(percorsoFile).length(), percorsoFile, idCategoria});
    }

    public DatiFile recuperaFilePerId(int idFile) {
        String sql = "SELECT * FROM Contenuto_File WHERE id_file = ?";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idFile})) {
            return rs.next() ? mappaFile(rs) : null;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero del file", e);
        }
    }

    public void aggiornaFile(int idFile, String titolo, String descrizione, String percorsoFile) {
        String sql = "UPDATE Contenuto_File SET titolo = ?, descrizione = ?, formato = ?, "
                + "dimensione = ?, percorso_fisico = ? WHERE id_file = ?";
        boundaryDBMS.eseguiAggiornamento(sql, new Object[]{
                titolo, descrizione, estensione(percorsoFile),
                new File(percorsoFile).length(), percorsoFile, idFile});
    }

    public void eliminaFile(int idFile) {
        Connection conn = boundaryDBMS.apriConnessione();
        try {
            boolean autoCommitPrecedente = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                boundaryDBMS.eseguiAggiornamento("DELETE FROM Dettaglio_Link WHERE id_file = ?", new Object[]{idFile});
                boundaryDBMS.eseguiAggiornamento("DELETE FROM Dettaglio_Codice WHERE id_file = ?", new Object[]{idFile});
                boundaryDBMS.eseguiAggiornamento("DELETE FROM Contenuto_File WHERE id_file = ?", new Object[]{idFile});
                conn.commit();
            } catch (RuntimeException | SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(autoCommitPrecedente);
            }
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante l'eliminazione del file", e);
        }
    }

    public void aggiornaPrivacy(int idFile, String privacy) {
        boundaryDBMS.eseguiAggiornamento(
                "UPDATE Contenuto_File SET stato_privacy = ? WHERE id_file = ?", new Object[]{privacy, idFile});
    }

    // ============================================================
    //  gestioneCondivisione - Link (UC 4.x) e Codici (UC 7.x)
    // ============================================================

    /** Tutti i contenuti dello studente (pubblici e privati). */
    public List<DatiFile> recuperaContenuti(int idStudente) {
        String sql = "SELECT cf.*, ca.nome_categoria FROM Contenuto_File cf "
                + "JOIN Categoria_Artistica ca ON cf.id_categoria = ca.id_categoria "
                + "WHERE ca.id_studente = ?";
        return eseguiSelectFile(sql, idStudente);
    }

    /** Solo i contenuti privati dello studente. */
    public List<DatiFile> recuperaContenutiPrivati(int idStudente) {
        String sql = "SELECT cf.*, ca.nome_categoria FROM Contenuto_File cf "
                + "JOIN Categoria_Artistica ca ON cf.id_categoria = ca.id_categoria "
                + "WHERE ca.id_studente = ? AND cf.stato_privacy = 'Privato'";
        return eseguiSelectFile(sql, idStudente);
    }

    private List<DatiFile> eseguiSelectFile(String sql, int idStudente) {
        List<DatiFile> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idStudente})) {
            while (rs.next()) {
                lista.add(mappaFileConCategoria(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei contenuti", e);
        }
    }

    /** Salva un link e le associazioni ai file selezionati (scadenza 14 giorni). */
    public void salvaLink(String link, int idStudente, int[] idContenuti) {
        String sqlLink = "INSERT INTO Link_Condivisione "
                + "(token_url, data_creazione, data_scadenza, visualizzato, stato_link, id_studente) "
                + "VALUES (?, ?, ?, 0, 'Attivo', ?)";
        boundaryDBMS.eseguiAggiornamento(sqlLink, new Object[]{
                link, LocalDate.now().toString(), LocalDate.now().plusDays(14).toString(), idStudente});
        int idLink = recuperaIdPerToken("Link_Condivisione", "id_link", "token_url", link);
        for (int idFile : idContenuti) {
            boundaryDBMS.eseguiAggiornamento(
                    "INSERT INTO Dettaglio_Link (id_link, id_file) VALUES (?, ?)",
                    new Object[]{idLink, idFile});
        }
    }

    public List<DatiLink> recuperaLink(int idStudente) {
        String sql = "SELECT * FROM Link_Condivisione WHERE id_studente = ?";
        List<DatiLink> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idStudente})) {
            while (rs.next()) {
                lista.add(new DatiLink(
                        rs.getInt("id_link"), rs.getString("token_url"),
                        rs.getString("data_scadenza"), rs.getInt("visualizzato") == 1,
                        rs.getString("stato_link")));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei link", e);
        }
    }

    public void aggiornaStatoLink(int idLink) {
        boundaryDBMS.eseguiAggiornamento(
                "UPDATE Link_Condivisione SET stato_link = 'Revocato' WHERE id_link = ?", new Object[]{idLink});
    }

    /** Tutti i link ancora attivi (con la loro data di scadenza). */
    public List<DatiLink> recuperaLinkConScadenza() {
        String sql = "SELECT * FROM Link_Condivisione WHERE stato_link = 'Attivo'";
        List<DatiLink> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{})) {
            while (rs.next()) {
                lista.add(new DatiLink(
                        rs.getInt("id_link"), rs.getString("token_url"),
                        rs.getString("data_scadenza"), rs.getInt("visualizzato") == 1,
                        rs.getString("stato_link")));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei link da verificare", e);
        }
    }

    /** Marca un link come scaduto. */
    public void disattivaLink(int idLink) {
        boundaryDBMS.eseguiAggiornamento(
                "UPDATE Link_Condivisione SET stato_link = 'Scaduto' WHERE id_link = ?", new Object[]{idLink});
    }

    /** Salva un codice di sblocco e le associazioni ai file privati selezionati. */
    public void salvaCodice(String codice, int idStudente, int[] idContenuti) {
        String sqlCod = "INSERT INTO Codice_Sblocco (token_testuale, stato_codice, id_studente) "
                + "VALUES (?, 'Attivo', ?)";
        boundaryDBMS.eseguiAggiornamento(sqlCod, new Object[]{codice, idStudente});
        int idCodice = recuperaIdPerToken("Codice_Sblocco", "id_codice", "token_testuale", codice);
        for (int idFile : idContenuti) {
            boundaryDBMS.eseguiAggiornamento(
                    "INSERT INTO Dettaglio_Codice (id_codice, id_file) VALUES (?, ?)",
                    new Object[]{idCodice, idFile});
        }
    }

    public List<DatiCodice> recuperaCodici(int idStudente) {
        String sql = "SELECT * FROM Codice_Sblocco WHERE id_studente = ?";
        List<DatiCodice> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idStudente})) {
            while (rs.next()) {
                lista.add(new DatiCodice(
                        rs.getInt("id_codice"), rs.getString("token_testuale"),
                        "Attivo".equals(rs.getString("stato_codice"))));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei codici", e);
        }
    }

    public void aggiornaStatoCodice(int idCodice) {
        boundaryDBMS.eseguiAggiornamento(
                "UPDATE Codice_Sblocco SET stato_codice = 'Revocato' WHERE id_codice = ?", new Object[]{idCodice});
    }

    // ============================================================
    //  consultazione (UC 5.x) e accesso via link (UC 6.x)
    // ============================================================

    public List<DatiStudente> cercaStudenti(String nome, String cognome) {
        nome = nome == null ? "" : nome.trim();
        cognome = cognome == null ? "" : cognome.trim();
        String sql = "SELECT id_studente, nome, cognome FROM Studente "
                + "WHERE (? = '' OR LOWER(nome) LIKE LOWER(?)) "
                + "AND (? = '' OR LOWER(cognome) LIKE LOWER(?)) "
                + "ORDER BY cognome, nome";
        String filtroNome = nome + "%";
        String filtroCognome = cognome + "%";
        List<DatiStudente> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{
                nome, filtroNome, cognome, filtroCognome})) {
            while (rs.next()) {
                lista.add(new DatiStudente(rs.getInt("id_studente"),
                        rs.getString("nome"), rs.getString("cognome")));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante la ricerca degli studenti", e);
        }
    }

    /** Profilo di uno studente consultato. */
    public EntityStudente recuperaProfilo(int idStudente) {
        return recuperaDatiPersonali(idStudente);
    }

    /** Categorie del profilo consultato (per la consultazione pubblica). */
    public List<DatiCategoria> recuperaCategoriePubbliche(int idStudente) {
        return recuperaCategorie(idStudente);
    }

    /** File pubblici di una categoria/tipologia. */
    public List<DatiFile> recuperaFilePubblici(int idCategoria, String tipologia) {
        String sql = "SELECT * FROM Contenuto_File WHERE id_categoria = ? AND tipologia = ? "
                + "AND stato_privacy = 'Pubblico'";
        List<DatiFile> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{idCategoria, tipologia})) {
            while (rs.next()) {
                lista.add(mappaFile(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei file pubblici", e);
        }
    }

    /** Verifica che il codice di sblocco sia valido e attivo per quello studente. */
    public boolean verificaCodiceSblocco(String codice, int idStudente) {
        String sql = "SELECT 1 FROM Codice_Sblocco WHERE token_testuale = ? AND id_studente = ? "
                + "AND stato_codice = 'Attivo'";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{codice, idStudente})) {
            return rs.next();
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante la verifica del codice", e);
        }
    }

    /** File privati associati a un codice di sblocco. */
    public List<DatiFile> recuperaFilePerCodice(String codice) {
        String sql = "SELECT cf.*, ca.nome_categoria FROM Contenuto_File cf "
                + "JOIN Dettaglio_Codice dc ON cf.id_file = dc.id_file "
                + "JOIN Codice_Sblocco cs ON dc.id_codice = cs.id_codice "
                + "JOIN Categoria_Artistica ca ON cf.id_categoria = ca.id_categoria "
                + "WHERE cs.token_testuale = ?";
        return eseguiSelectFileTokenConCategoria(sql, codice);
    }

    /** Verifica che un link sia valido e attivo. */
    public boolean verificaStatoLink(String token) {
        String sql = "SELECT 1 FROM Link_Condivisione WHERE token_url = ? AND stato_link = 'Attivo'";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{token})) {
            return rs.next();
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante la verifica del link", e);
        }
    }

    /** File associati a un link e marcatura dell'avvenuta visualizzazione. */
    public List<DatiFile> recuperaFilePerLink(String token) {
        boundaryDBMS.eseguiAggiornamento(
                "UPDATE Link_Condivisione SET visualizzato = 1 WHERE token_url = ?", new Object[]{token});
        String sql = "SELECT cf.*, ca.nome_categoria FROM Contenuto_File cf "
                + "JOIN Dettaglio_Link dl ON cf.id_file = dl.id_file "
                + "JOIN Link_Condivisione lc ON dl.id_link = lc.id_link "
                + "JOIN Categoria_Artistica ca ON cf.id_categoria = ca.id_categoria "
                + "WHERE lc.token_url = ?";
        return eseguiSelectFileTokenConCategoria(sql, token);
    }

    /** Recupera il file fisico dato il suo identificativo. */
    public File recuperaFileFisico(int idFile) {
        DatiFile f = recuperaFilePerId(idFile);
        return f == null ? null : new File(f.percorso());
    }

    private List<DatiFile> eseguiSelectFileToken(String sql, String token) {
        List<DatiFile> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{token})) {
            while (rs.next()) {
                lista.add(mappaFile(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei contenuti condivisi", e);
        }
    }

    private List<DatiFile> eseguiSelectFileTokenConCategoria(String sql, String token) {
        List<DatiFile> lista = new ArrayList<>();
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{token})) {
            while (rs.next()) {
                lista.add(mappaFileConCategoria(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dei contenuti condivisi", e);
        }
    }

    private int recuperaIdPerToken(String tabella, String colId, String colToken, String token) {
        String sql = "SELECT " + colId + " FROM " + tabella + " WHERE " + colToken + " = ?";
        try (ResultSet rs = boundaryDBMS.eseguiQuery(sql, new Object[]{token})) {
            return rs.next() ? rs.getInt(colId) : -1;
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante il recupero dell'identificativo", e);
        }
    }

    private DatiFile mappaFile(ResultSet rs) throws SQLException {
        return new DatiFile(
                rs.getInt("id_file"),
                rs.getString("titolo"),
                rs.getString("descrizione"),
                rs.getString("tipologia"),
                rs.getString("stato_privacy"),
                rs.getString("percorso_fisico"),
                null);
    }

    private DatiFile mappaFileConCategoria(ResultSet rs) throws SQLException {
        return new DatiFile(
                rs.getInt("id_file"),
                rs.getString("titolo"),
                rs.getString("descrizione"),
                rs.getString("tipologia"),
                rs.getString("stato_privacy"),
                rs.getString("percorso_fisico"),
                rs.getString("nome_categoria"));
    }

    private String estensione(String percorso) {
        int p = percorso.lastIndexOf('.');
        return p >= 0 ? percorso.substring(p + 1).toLowerCase() : "";
    }

    // ---- helper di mapping ----

    private EntityStudente mappaStudente(ResultSet rs) throws SQLException {
        EntityStudente s = new EntityStudente();
        s.setId(rs.getInt("id_studente"));
        s.setNome(rs.getString("nome"));
        s.setCognome(rs.getString("cognome"));
        s.setCodiceFiscale(rs.getString("codice_fiscale"));
        s.setEmail(rs.getString("email"));
        s.setTelefono(rs.getString("telefono"));
        s.setPercorsoFoto(rs.getString("percorso_foto"));
        s.setPassword(rs.getString("password"));
        return s;
    }
}
