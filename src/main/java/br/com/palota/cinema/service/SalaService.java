package br.com.palota.cinema.service;

import br.com.palota.cinema.dao.SalaDao;
import br.com.palota.cinema.model.Sala;

import java.util.List;

public class SalaService {

    private SalaDao dao;

    public SalaService(SalaDao dao) {
        this.dao = dao;
    }

    public List<Sala> buscarTodos() {
        return dao.listar();
    }

    public Sala buscarPorId(Long id) {
        return dao.buscar(id);
    }

    public void salvar(Sala sala) {
        if (sala.getId() == null) {
            dao.inserir(sala);
        } else {
            dao.atualizar(sala);
        }
    }

    public void excluir(Long id) {
        dao.excluir(id);
    }
}
