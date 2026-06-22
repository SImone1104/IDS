package it.afam.gestioneStudente.interfaccia;

import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiAvviso;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiConferma;
import it.afam.gestioneAutenticazione.interfaccia.MessaggioDiErrore;
import it.afam.gestioneStudente.control.GestioneProfiloControl;
import it.afam.utility.ConnessioneException;
import it.afam.utility.SceneManager;
import it.afam.utility.dto.DatiBackground;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/** Form di aggiunta/modifica di una voce di background artistico (UC 2.2, 2.3). */
public class FormModificaBackgroundArtistico {

    @FXML private TextArea campoScuolaUniversita;
    @FXML private TextArea campoCollaborazioniFatte;
    @FXML private TextArea campoCollaborazioniAutori;
    @FXML private TextArea campoPartecipazioni;

    @FXML
    private void initialize() {
        GestioneProfiloControl control = GestioneProfiloControl.getIstanza();
        if (!control.isAggiunta() && control.getBackgroundCorrente() != null) {
            DatiBackground d = control.getBackgroundCorrente();
            campoScuolaUniversita.setText(d.scuolaUniversita());
            campoCollaborazioniFatte.setText(d.collaborazioniFatte());
            campoCollaborazioniAutori.setText(d.collaborazioniAutori());
            campoPartecipazioni.setText(d.partecipazioni());
        }
    }

    @FXML
    private void cliccaConferma() {
        GestioneProfiloControl control = GestioneProfiloControl.getIstanza();
        DatiBackground dato = new DatiBackground(0,
                campoScuolaUniversita.getText(), campoCollaborazioniFatte.getText(),
                campoCollaborazioniAutori.getText(), campoPartecipazioni.getText());
        try {
            boolean ok;
            String messaggio;
            if (control.isAggiunta()) {
                ok = control.aggiungiBackground(dato);
                messaggio = "Aggiunto nuovo background";
            } else {
                ok = control.modificaBackground(control.getBackgroundCorrente().id(), dato);
                messaggio = "Modifiche effettuate correttamente";
            }
            if (ok) {
                MessaggioDiConferma.mostra(messaggio);
                SceneManager.getIstanza().switchTo("SchermataBackgroundArtistico.fxml");
            } else {
                MessaggioDiErrore.mostra("Ogni campo può contenere al massimo 700 caratteri");
            }
        } catch (ConnessioneException e) {
            MessaggioDiAvviso.mostra("Connessione al database non riuscita. Riprova.");
        }
    }

    @FXML
    private void cliccaAnnulla() {
        SceneManager.getIstanza().switchTo("SchermataBackgroundArtistico.fxml");
    }
}
