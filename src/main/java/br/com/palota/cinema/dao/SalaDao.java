package br.com.palota.cinema.dao;

import br.com.palota.cinema.model.Sala;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class SalaDao {

    public List<Sala> findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cinemaPu");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("select s from Sala s order by s.id");
        return  query.getResultList();
    }
}
