package br.com.palota.cinema.dao;

import br.com.palota.cinema.exception.BusinessException;
import br.com.palota.cinema.model.Sala;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class SalaDao {

    private final String PERSISTENCE_UNIT = "cinemaPu";

    public List<Sala> listar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("select s from Sala s order by s.id");
        List<Sala> lista = query.getResultList();
        em.close();
        emf.close();
        return lista;
    }

    public Sala buscar(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        Sala sala = em.find(Sala.class, id);
        em.close();
        emf.close();
        return sala;
    }

    public void inserir(Sala sala) {
        if (sala.getId() != null) {
            this.atualizar(sala);
            return;
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(sala);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public void atualizar(Sala sala) {
        if (sala.getId() == null) {
            this.inserir(sala);
            return;
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Sala salaAntiga = em.find(Sala.class, sala.getId());
        salaAntiga.setNome(sala.getNome());
        salaAntiga.setCapacidade(sala.getCapacidade());
        em.merge(sala);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public void excluir(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Sala sala = em.find(Sala.class, id);
        if (sala == null) {
            em.getTransaction().rollback();
            em.getTransaction();
            emf.close();
            throw new BusinessException("Sala não encontrada");
        }
        em.remove(id);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

}
