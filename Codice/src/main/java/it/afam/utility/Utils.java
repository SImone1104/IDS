package it.afam.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Metodi statici condivisi: validazione input, hashing password (SHA-256 senza salt,
 * come da ODD/SDD), generazione di codici e token.
 * I requisiti di validazione derivano dalle schede dei casi d'uso del RAD.
 */
public final class Utils {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALFANUMERICO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private Utils() { }

    // ---- Validazioni (RAD) ----

    /** Solo caratteri alfabetici. */
    public static boolean validaNome(String nome) {
        return nome != null && nome.matches("[A-Za-zÀ-ÿ]+");
    }

    /** Solo caratteri alfabetici. */
    public static boolean validaCognome(String cognome) {
        return cognome != null && cognome.matches("[A-Za-zÀ-ÿ]+");
    }

    /** Deve contenere una @ e almeno un punto. */
    public static boolean validaEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /** Da 8 a 16 caratteri, almeno una lettera maiuscola e almeno un numero. */
    public static boolean validaPassword(String password) {
        return password != null
                && password.length() >= 8 && password.length() <= 16
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*");
    }

    /** Esattamente 16 caratteri. */
    public static boolean validaCodiceFiscale(String cf) {
        return cf != null && cf.length() == 16;
    }

    /** Esattamente 10 caratteri, solo numerici. */
    public static boolean validaTelefono(String telefono) {
        return telefono != null && telefono.matches("[0-9]{10}");
    }

    // ---- Generazione codici / token ----

    public static String generaCodiceOTP() {
        return String.format("%06d", RANDOM.nextInt(1_000_000)); // 6 cifre
    }

    public static String generaCodiceAttivazione() {
        return generaAlfanumerico(8);
    }

    public static String generaCodiceRecupero() {
        return generaAlfanumerico(8);
    }

    public static String generaTokenLink() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generaCodiceCondivisione() {
        return generaAlfanumerico(8);
    }

    private static String generaAlfanumerico(int lunghezza) {
        StringBuilder sb = new StringBuilder(lunghezza);
        for (int i = 0; i < lunghezza; i++) {
            sb.append(ALFANUMERICO.charAt(RANDOM.nextInt(ALFANUMERICO.length())));
        }
        return sb.toString();
    }

    // ---- Hashing password (SHA-256, senza salt) ----

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo SHA-256 non disponibile", e);
        }
    }

    public static boolean verificaPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }
}
