package it.afam.utility.dto;

/** DTO per un contenuto/file. */
public record DatiFile(int id, String titolo, String descrizione,
                       String tipologia, String privacy, String percorso) {
}
