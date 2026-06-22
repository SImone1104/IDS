package it.afam.utility.dto;

/** DTO per un link di condivisione. */
public record DatiLink(int id, String url, String scadenza,
                       boolean visualizzato, String stato) {
}
