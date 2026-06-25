package it.afam.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Gestisce la cartella locale dei file caricati dall'applicazione. */
public final class ArchivioFile {

    private static final Path BASE = Path.of("afam_files");
    private static final Path CONTENUTI = BASE.resolve("contenuti");
    private static final Path FOTO = BASE.resolve("foto");

    private ArchivioFile() { }

    public static String archiviaContenuto(String percorsoOrigine) {
        return archivia(percorsoOrigine, CONTENUTI);
    }

    public static String archiviaFoto(String percorsoOrigine) {
        if (percorsoOrigine == null || percorsoOrigine.isBlank()) {
            return percorsoOrigine;
        }
        return archivia(percorsoOrigine, FOTO);
    }

    public static File risolvi(String percorso) {
        if (percorso == null || percorso.isBlank()) {
            return null;
        }
        File file = new File(percorso);
        if (file.isAbsolute()) {
            return file;
        }
        return Path.of(percorso).toFile();
    }

    private static String archivia(String percorsoOrigine, Path cartellaDestinazione) {
        if (percorsoOrigine == null || percorsoOrigine.isBlank()) {
            return percorsoOrigine;
        }

        Path origine = Path.of(percorsoOrigine);
        if (!origine.isAbsolute()) {
            return percorsoOrigine;
        }

        try {
            Files.createDirectories(cartellaDestinazione);
            Path destinazione = destinazioneUnivoca(cartellaDestinazione, origine.getFileName().toString());
            Files.copy(origine, destinazione, StandardCopyOption.REPLACE_EXISTING);
            return destinazione.toString();
        } catch (IOException e) {
            throw new ConnessioneException("Errore durante l'archiviazione del file", e);
        }
    }

    private static Path destinazioneUnivoca(Path cartella, String nomeFile) {
        Path destinazione = cartella.resolve(nomeFile);
        if (!Files.exists(destinazione)) {
            return destinazione;
        }

        String nome = nomeFile;
        String estensione = "";
        int punto = nomeFile.lastIndexOf('.');
        if (punto > 0) {
            nome = nomeFile.substring(0, punto);
            estensione = nomeFile.substring(punto);
        }

        int progressivo = 1;
        do {
            destinazione = cartella.resolve(nome + "_" + progressivo + estensione);
            progressivo++;
        } while (Files.exists(destinazione));
        return destinazione;
    }
}
