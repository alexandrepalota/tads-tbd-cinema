package br.com.palota.cinema.dao;

import br.com.palota.cinema.exception.BusinessException;
import br.com.palota.cinema.model.Filme;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class FilmeDao {

    private final String PERSISTENCE_UNIT = "cinemaPu";

    public List<Filme> listar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("select f from Filme f order by f.id");
        List<Filme> lista = query.getResultList();
        em.close();
        emf.close();
        return lista;
    }

    public Filme buscar(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        Filme filme = em.find(Filme.class, id);
        em.close();
        emf.close();
        return filme;
    }

    public void inserir(Filme filme) {
        if (filme.getId() != null) {
            this.atualizar(filme);
            return;
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(filme);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public void atualizar(Filme filme) {
        if (filme.getId() == null) {
            this.inserir(filme);
            return;
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Filme filmeAntigo = em.find(Filme.class, filme.getId());
        filmeAntigo.setTitulo(filme.getTitulo());
        filmeAntigo.setSinopse(filme.getSinopse());
        filmeAntigo.setGenero(filme.getGenero());
        em.merge(filme);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public void excluir(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Filme filme = em.find(Filme.class, id);
        if (filme == null) {
            em.close();
            emf.close();
            throw new BusinessException("Filme não encontrado");
        }
        em.remove(filme);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

}
