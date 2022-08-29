package br.com.palota.cinema.dao;

import br.com.palota.cinema.model.Sala;

public class SalaDao extends DAO<Sala> {

    public SalaDao() {
        super(Sala.class);
    }

    public void atualizar(Sala sala) {
        if (sala.getId() != null) {
            this.abrirTransacao();
            Sala salaAntiga = buscarPorId(sala.getId());
            salaAntiga.setNome(sala.getNome());
            salaAntiga.setCapacidade(sala.getCapacidade());
            em.merge(salaAntiga);
            this.fecharTransacao();
        }
    }
}
