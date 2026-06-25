package it.afam.entity;

import it.afam.utility.dto.DatiBackground; //permette di avere una lista di tipo datiBackground

import java.util.ArrayList;
import java.util.List;

/**
 * Unica classe entity del sistema: rappresenta lo studente autenticato.
 * Coerente con l'ODD (package it.afam.entity) e con la tabella Studente dell'SDD.
 */
public class EntityStudente {

    private int id;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    private String password;          // hash SHA-256
    private String telefono;
    private String percorsoFoto;
    private List<DatiBackground> datiBackground = new ArrayList<>();

    public EntityStudente() {
    }

    public EntityStudente(String nome, String cognome, String codiceFiscale, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPercorsoFoto() { return percorsoFoto; }
    public void setPercorsoFoto(String percorsoFoto) { this.percorsoFoto = percorsoFoto; }

    public List<DatiBackground> getDatiBackground() { return datiBackground; }
    public void setDatiBackground(List<DatiBackground> datiBackground) { this.datiBackground = datiBackground; }

    public void aggiungiBackground(DatiBackground dato) { this.datiBackground.add(dato); }
    public void rimuoviBackground(int indice) { this.datiBackground.remove(indice); }
}
