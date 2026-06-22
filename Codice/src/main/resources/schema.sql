-- Schema database AFAM Connect (SQLite) -- coerente con SDD, sezione "Struttura delle tabelle"
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Studente (
    id_studente     INTEGER PRIMARY KEY AUTOINCREMENT,
    nome            TEXT NOT NULL,
    cognome         TEXT NOT NULL,
    codice_fiscale  TEXT NOT NULL UNIQUE,
    email           TEXT NOT NULL UNIQUE,
    telefono        TEXT NOT NULL,
    percorso_foto   TEXT,
    password        TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Background_Artistico (
    id_background          INTEGER PRIMARY KEY AUTOINCREMENT,
    scuola_universita      TEXT,
    collaborazioni_fatte   TEXT,
    collaborazioni_autori  TEXT,
    partecipazioni         TEXT,
    id_studente            INTEGER NOT NULL,
    FOREIGN KEY (id_studente) REFERENCES Studente(id_studente)
);

CREATE TABLE IF NOT EXISTS Categoria_Artistica (
    id_categoria    INTEGER PRIMARY KEY AUTOINCREMENT,
    nome_categoria  TEXT NOT NULL,
    id_studente     INTEGER NOT NULL,
    FOREIGN KEY (id_studente) REFERENCES Studente(id_studente)
);

CREATE TABLE IF NOT EXISTS Contenuto_File (
    id_file         INTEGER PRIMARY KEY AUTOINCREMENT,
    titolo          TEXT NOT NULL,
    descrizione     TEXT,
    tipologia       TEXT NOT NULL,
    formato         TEXT NOT NULL,
    dimensione      INTEGER NOT NULL,
    stato_privacy   TEXT NOT NULL CHECK (stato_privacy IN ('Pubblico', 'Privato', 'Non in elenco')),
    percorso_fisico TEXT NOT NULL,
    id_categoria    INTEGER NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES Categoria_Artistica(id_categoria)
);

CREATE TABLE IF NOT EXISTS Link_Condivisione (
    id_link         INTEGER PRIMARY KEY AUTOINCREMENT,
    token_url       TEXT NOT NULL UNIQUE,
    data_creazione  TEXT NOT NULL,
    data_scadenza   TEXT NOT NULL,
    visualizzato    INTEGER NOT NULL DEFAULT 0,
    stato_link      TEXT NOT NULL CHECK (stato_link IN ('Attivo', 'Revocato', 'Scaduto')),
    id_studente     INTEGER NOT NULL,
    FOREIGN KEY (id_studente) REFERENCES Studente(id_studente)
);

CREATE TABLE IF NOT EXISTS Dettaglio_Link (
    id_link  INTEGER NOT NULL,
    id_file  INTEGER NOT NULL,
    PRIMARY KEY (id_link, id_file),
    FOREIGN KEY (id_link) REFERENCES Link_Condivisione(id_link),
    FOREIGN KEY (id_file) REFERENCES Contenuto_File(id_file)
);

CREATE TABLE IF NOT EXISTS Codice_Sblocco (
    id_codice       INTEGER PRIMARY KEY AUTOINCREMENT,
    token_testuale  TEXT NOT NULL UNIQUE,
    stato_codice    TEXT NOT NULL CHECK (stato_codice IN ('Attivo', 'Revocato')),
    id_studente     INTEGER NOT NULL,
    FOREIGN KEY (id_studente) REFERENCES Studente(id_studente)
);

CREATE TABLE IF NOT EXISTS Dettaglio_Codice (
    id_codice  INTEGER NOT NULL,
    id_file    INTEGER NOT NULL,
    PRIMARY KEY (id_codice, id_file),
    FOREIGN KEY (id_codice) REFERENCES Codice_Sblocco(id_codice),
    FOREIGN KEY (id_file) REFERENCES Contenuto_File(id_file)
);
