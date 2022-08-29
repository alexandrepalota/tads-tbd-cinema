package br.com.palota.cinema.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class IngressoPK implements Serializable {

    @ManyToOne
    private Sessao sessao;

    private Integer lugar;

    public IngressoPK() {
    }

    public IngressoPK(Sessao sessao, Integer lugar) {
        this.sessao = sessao;
        this.lugar = lugar;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public Integer getLugar() {
        return lugar;
    }

    public void setLugar(Integer lugar) {
        this.lugar = lugar;
    }
}
