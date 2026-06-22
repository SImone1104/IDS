package it.afam.gestioneAutenticazione.control;

import it.afam.entity.EntityStudente;
import it.afam.utility.DatabaseManager;
import it.afam.utility.Utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

/**
 * Gestisce il caso d'uso Registrazione (UC 1.3): validazione dati, verifica
 * unicità email, generazione/invio del codice di attivazione (Jakarta Mail) e
 * salvataggio del nuovo studente.
 */
public class RegistrazioneControl {

    private static RegistrazioneControl istanza;

    private EntityStudente studenteProvvisorio;
    private String codiceAttivazione;
    private String ultimoErrore;

    private RegistrazioneControl() { }

    /** Istanza condivisa tra le schermate del caso d'uso Registrazione. */
    public static RegistrazioneControl getIstanza() {
        if (istanza == null) {
            istanza = new RegistrazioneControl();
        }
        return istanza;
    }

    /** Valida formato dati e corrispondenza password; se ok prepara lo studente provvisorio. */
    public boolean validaDati(String nome, String cognome, String codiceFiscale,
                              String email, String password, String confermaPassword) {
        ultimoErrore = controllaDati(nome, cognome, codiceFiscale, email, password, confermaPassword);
        if (ultimoErrore != null) {
            return false;
        }
        studenteProvvisorio = new EntityStudente(nome, cognome, codiceFiscale, email, Utils.hashPassword(password));
        studenteProvvisorio.setTelefono(""); // valorizzato successivamente in Gestione Profilo
        return true;
    }

    /** Messaggio dell'ultima validazione fallita (null se l'ultima è andata a buon fine). */
    public String getUltimoErrore() {
        return ultimoErrore;
    }

    private String controllaDati(String nome, String cognome, String codiceFiscale,
                                 String email, String password, String confermaPassword) {
        if (!Utils.validaNome(nome)) {
            return "Il nome deve contenere solo caratteri alfabetici";
        }
        if (!Utils.validaCognome(cognome)) {
            return "Il cognome deve contenere solo caratteri alfabetici";
        }
        if (nome.equalsIgnoreCase(cognome)) {
            return "Il nome deve essere diverso dal cognome";
        }
        if (!Utils.validaCodiceFiscale(codiceFiscale)) {
            return "Il codice fiscale deve contenere esattamente 16 caratteri";
        }
        if (!Utils.validaEmail(email)) {
            return "L'e-mail deve contenere una @ e almeno un punto";
        }
        if (!Utils.validaPassword(password)) {
            return "La password deve avere 8-16 caratteri, almeno una maiuscola e un numero";
        }
        if (!password.equals(confermaPassword)) {
            return "Le due password non coincidono";
        }
        return null;
    }

    /** Vero se l'email non è già registrata. */
    public boolean emailDisponibile(String email) {
        return !DatabaseManager.getIstanza().emailEsistente(email);
    }

    /** Genera il codice di attivazione e lo invia via e-mail. */
    public void generaEInviaCodiceAttivazione() {
        codiceAttivazione = Utils.generaCodiceAttivazione();
        inviaEmail(studenteProvvisorio.getEmail(),
                "AFAM Connect - Codice di attivazione",
                "Il tuo codice di attivazione è: " + codiceAttivazione);
    }

    /** Verifica il codice; se corretto salva definitivamente lo studente. */
    public boolean verificaCodiceAttivazione(String codice) {
        if (codice != null && codice.equals(codiceAttivazione)) {
            DatabaseManager.getIstanza().salvaStudente(studenteProvvisorio);
            return true;
        }
        return false;
    }

    /** Rigenera e re-invia il codice di attivazione. */
    public void richiediNuovoCodice() {
        generaEInviaCodiceAttivazione();
    }

    // --- invio e-mail diretto (Jakarta Mail), come da ODD ---
    private void inviaEmail(String destinatario, String oggetto, String corpo) {
        Properties cfg = caricaConfig();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", cfg.getProperty("smtp.host"));
        props.put("mail.smtp.port", cfg.getProperty("smtp.port"));
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(cfg.getProperty("smtp.user"), cfg.getProperty("smtp.password"));
            }
        });
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(cfg.getProperty("smtp.from")));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            msg.setSubject(oggetto);
            msg.setText(corpo);
            Transport.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("Invio e-mail non riuscito", e);
        }
    }

    private Properties caricaConfig() {
        Properties p = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/email.properties")) {
            p.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Configurazione e-mail non trovata", e);
        }
        return p;
    }
}
