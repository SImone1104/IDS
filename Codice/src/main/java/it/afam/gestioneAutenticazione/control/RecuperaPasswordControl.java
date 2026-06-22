package it.afam.gestioneAutenticazione.control;

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
 * Gestisce il caso d'uso Recupera password (UC 1.4): verifica dell'email,
 * generazione/invio del codice di recupero e aggiornamento della password.
 * Singleton: istanza condivisa tra le schermate del caso d'uso.
 */
public class RecuperaPasswordControl {

    private static RecuperaPasswordControl istanza;

    private String emailRecupero;
    private String codiceRecupero;

    private RecuperaPasswordControl() { }

    public static RecuperaPasswordControl getIstanza() {
        if (istanza == null) {
            istanza = new RecuperaPasswordControl();
        }
        return istanza;
    }

    /** Verifica che l'email esista; se sì genera e invia il codice di recupero. */
    public boolean verificaEmail(String email) {
        if (!DatabaseManager.getIstanza().emailEsistente(email)) {
            return false;
        }
        emailRecupero = email;
        codiceRecupero = Utils.generaCodiceRecupero();
        inviaEmail(email, "AFAM Connect - Recupero password",
                "Il tuo codice di recupero è: " + codiceRecupero);
        return true;
    }

    /** Verifica il codice di recupero inserito. */
    public boolean verificaCodiceRecupero(String codice) {
        return codice != null && codice.equals(codiceRecupero);
    }

    /** Rigenera e re-invia il codice di recupero. */
    public void richiediNuovoCodice() {
        codiceRecupero = Utils.generaCodiceRecupero();
        inviaEmail(emailRecupero, "AFAM Connect - Nuovo codice di recupero",
                "Il tuo nuovo codice di recupero è: " + codiceRecupero);
    }

    /** Aggiorna la password (hashata) dello studente. */
    public void aggiornaPassword(String nuovaPassword) {
        DatabaseManager.getIstanza().aggiornaPassword(emailRecupero, Utils.hashPassword(nuovaPassword));
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
