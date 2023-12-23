package com.ufpa.diploma.emissao.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Turma.
 */
@Entity
@Table(name = "turma")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Turma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "curso")
    private String curso;

    @Column(name = "sigla")
    private String sigla;

    @Column(name = "ano")
    private String ano;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Turma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurso() {
        return this.curso;
    }

    public Turma curso(String curso) {
        this.setCurso(curso);
        return this;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getSigla() {
        return this.sigla;
    }

    public Turma sigla(String sigla) {
        this.setSigla(sigla);
        return this;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getAno() {
        return this.ano;
    }

    public Turma ano(String ano) {
        this.setAno(ano);
        return this;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turma)) {
            return false;
        }
        return id != null && id.equals(((Turma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Turma{" +
            "id=" + getId() +
            ", curso='" + getCurso() + "'" +
            ", sigla='" + getSigla() + "'" +
            ", ano='" + getAno() + "'" +
            "}";
    }
}
