package it.afam.utility.dto;

/** DTO per una voce del background artistico. */
public record DatiBackground(int id, String scuolaUniversita, String collaborazioniFatte,
                             String collaborazioniAutori, String partecipazioni) {
}
