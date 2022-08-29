package br.com.palota.cinema.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_ingressos")
public class Ingresso {

    @EmbeddedId
    private IngressoPK id;

    private String nomeCliente;

    public Ingresso() {
    }

    public Ingresso(String nomeCliente, Integer lugar, Sessao sessao) {
        if (this.id == null) {
            this.id = new IngressoPK(sessao, lugar);
        } else {
            this.id.setSessao(sessao);
            this.id.setLugar(lugar);
        }
        this.nomeCliente = nomeCliente;
    }

    public IngressoPK getId() {
        return id;
    }

    public void setId(IngressoPK id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Integer getLugar() {
        return id.getLugar();
    }

    public void setLugar(Integer lugar) {
        this.id.setLugar(lugar);
    }

    public Sessao getSessao() {
        return this.id.getSessao();
    }

    public void setSessao(Sessao sessao) {
        this.id.setSessao(sessao);
    }
}
