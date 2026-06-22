package it.afam.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Adapter tecnico verso il DBMS SQLite (JDBC). Invocato esclusivamente dal
 * DatabaseManager. Mantiene un'unica connessione al file afam.db.
 * In caso di errore JDBC lancia ConnessioneException (UC 0.1).
 */
public class BoundaryDBMS {

    private static final String URL = "jdbc:sqlite:afam.db";
    private Connection connessione;

    /** Apre (se necessario) e restituisce la connessione al database. */
    public Connection apriConnessione() {
        try {
            if (connessione == null || connessione.isClosed()) {
                connessione = DriverManager.getConnection(URL);
                try (var st = connessione.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON;");
                }
            }
            return connessione;
        } catch (SQLException e) {
            throw new ConnessioneException("Impossibile connettersi al database", e);
        }
    }

    public void chiudiConnessione() {
        try {
            if (connessione != null && !connessione.isClosed()) {
                connessione.close();
            }
        } catch (SQLException e) {
            throw new ConnessioneException("Errore nella chiusura della connessione", e);
        }
    }

    /** Esegue una query parametrizzata e restituisce il ResultSet. */
    public ResultSet eseguiQuery(String sql, Object[] parametri) {
        try {
            PreparedStatement ps = apriConnessione().prepareStatement(sql);
            applicaParametri(ps, parametri);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante l'esecuzione della query", e);
        }
    }

    /** Esegue un INSERT/UPDATE/DELETE parametrizzato e restituisce le righe modificate. */
    public int eseguiAggiornamento(String sql, Object[] parametri) {
        try (PreparedStatement ps = apriConnessione().prepareStatement(sql)) {
            applicaParametri(ps, parametri);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new ConnessioneException("Errore durante l'aggiornamento dei dati", e);
        }
    }

    private void applicaParametri(PreparedStatement ps, Object[] parametri) throws SQLException {
        if (parametri != null) {
            for (int i = 0; i < parametri.length; i++) {
                ps.setObject(i + 1, parametri[i]);
            }
        }
    }
}
