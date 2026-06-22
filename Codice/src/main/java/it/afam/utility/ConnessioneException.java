package it.afam.utility;

/**
 * Eccezione unchecked lanciata dal DatabaseManager in caso di caduta della
 * connessione con il DBMS (UC 0.1). Viene catturata dai control, che mostrano
 * un MessaggioDiAvviso con le opzioni Riprova e Annulla.
 */
public class ConnessioneException extends RuntimeException {

    public ConnessioneException(String message) {
        super(message);
    }

    public ConnessioneException(String message, Throwable cause) {
        super(message, cause);
    }
}
