package br.com.palota.cinema.service;

import br.com.palota.cinema.dao.FilmeDao;
import br.com.palota.cinema.model.Filme;

import java.util.List;

public class FilmeService {

    private FilmeDao dao;

    public FilmeService(FilmeDao dao) {
        this.dao = dao;
    }

    public List<Filme> buscarTodos() {
        return dao.listar();
    }

    public Filme buscarPorId(Long id) {
        return dao.buscar(id);
    }

    public void salvar(Filme filme) {
        if (filme.getId() == null) {
            dao.inserir(filme);
        } else {
            dao.atualizar(filme);
        }
    }

    public void excluir(Long id) {
        dao.excluir(id);
    }
}
