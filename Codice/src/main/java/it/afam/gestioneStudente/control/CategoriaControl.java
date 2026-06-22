package it.afam.gestioneStudente.control;

import it.afam.utility.DatabaseManager;
import it.afam.utility.SessioneCorrente;
import it.afam.utility.dto.DatiCategoria;

import java.util.List;

/** Gestisce Seleziona Categoria (UC 3.1) e Crea Categoria (UC 3.2). */
public class CategoriaControl {

    public List<DatiCategoria> caricaCategorie() {
        return DatabaseManager.getIstanza()
                .recuperaCategorie(SessioneCorrente.getIstanza().getIdStudente());
    }

    public void selezionaCategoria(int idCategoria) {
        SessioneCorrente.getIstanza().setIdCategoriaCorrente(idCategoria);
    }

    /** Crea una nuova categoria (nome non vuoto e univoco); la imposta come corrente. */
    public boolean creaCategoria(String nome) {
        if (nome == null || nome.isBlank()) {
            return false;
        }
        int idStudente = SessioneCorrente.getIstanza().getIdStudente();
        if (DatabaseManager.getIstanza().categoriaEsistente(nome, idStudente)) {
            return false;
        }
        DatabaseManager.getIstanza().salvaCategoria(nome, idStudente);
        for (DatiCategoria c : DatabaseManager.getIstanza().recuperaCategorie(idStudente)) {
            if (c.nome().equals(nome)) {
                SessioneCorrente.getIstanza().setIdCategoriaCorrente(c.id());
                break;
            }
        }
        return true;
    }
}
