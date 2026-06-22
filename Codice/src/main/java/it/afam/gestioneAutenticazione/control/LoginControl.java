package it.afam.gestioneAutenticazione.control;

import it.afam.entity.EntityStudente;
import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;
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
 * Gestisce il caso d'uso Login con verifica OTP a due fattori (UC 1.1).
 * Il login federato SPID/eIDAS (UC 1.2) non è implementato: come indicato
 * nell'SDD, è previsto come estensione futura.
 */
public class LoginControl {

    private static LoginControl istanza;

    private String emailInVerifica;
    private String codiceVerifica;

    private LoginControl() { }

    /** Istanza condivisa tra le schermate del caso d'uso Login. */
    public static LoginControl getIstanza() {
        if (istanza == null) {
            istanza = new LoginControl();
        }
        return istanza;
    }

    /** Verifica le credenziali; se corrette genera e invia l'OTP. */
    public boolean accedi(String email, String password) {
        EntityStudente studente = DatabaseManager.getIstanza()
                .verificaCredenziali(email, Utils.hashPassword(password));
        if (studente == null) {
            return false;
        }
        emailInVerifica = email;
        codiceVerifica = Utils.generaCodiceOTP();
        inviaEmail(email, "AFAM Connect - Codice di accesso",
                "Il tuo codice di verifica (OTP) è: " + codiceVerifica);
        return true;
    }

    /** Verifica l'OTP; se corretto popola la sessione corrente. */
    public boolean verificaCodice(String codice) {
        if (codice != null && codice.equals(codiceVerifica)) {
            EntityStudente studente = DatabaseManager.getIstanza().recuperaStudentePerEmail(emailInVerifica);
            SessioneCorrente.getIstanza().setIdStudente(studente.getId());
            return true;
        }
        return false;
    }

    /** Rigenera e re-invia il codice OTP. */
    public void richiediNuovoCodice() {
        codiceVerifica = Utils.generaCodiceOTP();
        inviaEmail(emailInVerifica, "AFAM Connect - Nuovo codice di accesso",
                "Il tuo nuovo codice di verifica (OTP) è: " + codiceVerifica);
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
