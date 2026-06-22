package it.afam.utility.dto;

/** DTO per un contenuto/file. Il campo categoria è valorizzato solo dove serve (es. selezione contenuti). */
public record DatiFile(int id, String titolo, String descrizione,
                       String tipologia, String privacy, String percorso, String categoria) {
}
