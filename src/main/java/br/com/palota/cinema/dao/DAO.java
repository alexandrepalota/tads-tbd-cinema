package br.com.palota.cinema.dao;

import br.com.palota.cinema.exception.DatabaseException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class DAO<E> {

    private static EntityManagerFactory emf;
    protected EntityManager em;
    private Class<E> classe;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("cinemaPu");
        } catch (Exception e) {
            throw new DatabaseException("Erro ao tentar se conectar com o banco de dados");
        }
    }

    public DAO() {
        this(null);
    }

    public DAO(Class<E> classe) {
        this.classe = classe;
        em = emf.createEntityManager();
    }

    public DAO<E> abrirTransacao() {
        em.getTransaction().begin();
        return this;
    }

    public DAO<E> fecharTransacao() {
        em.getTransaction().commit();
        return this;
    }

    public DAO<E> incluir(E entidade) {
        em.persist(entidade);
        return this;
    }

    public DAO<E> incluirAtomico(E entidade) {
        return this.abrirTransacao().incluir(entidade).fecharTransacao();
    }

    public E buscarPorId(Object id) {
        return this.em.find(this.classe, id);
    }

    public List<E> obterTodos() {
        return this.obterTodos(100, 0);
    }

    public List<E> obterTodos(int limit, int offset) {
        if (classe == null) {
            throw new UnsupportedOperationException("Classe nula.");
        }
        String jpql = "select e from " + classe.getName() + " e";
        TypedQuery<E> query = em.createQuery(jpql, classe);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public void excluir(Object id) {
        E entidade = this.buscarPorId(id);
        if (entidade != null) {
            this.abrirTransacao();
            em.remove(entidade);
            this.fecharTransacao();
        } else {
            throw new DatabaseException("Entidade não encontrada");
        }
    }

    public void fechar() {
        em.close();
    }
}
