package com.ufpa.diploma.emissao.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 * A EntregaDiploma.
 */
@Entity
@Table(name = "entrega_diploma")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntregaDiploma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "data_de_entrega")
    private LocalDate dataDeEntrega;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "observacoes")
    private String observacoes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "turma" }, allowSetters = true)
    private Processo processo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EntregaDiploma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataDeEntrega() {
        return this.dataDeEntrega;
    }

    public EntregaDiploma dataDeEntrega(LocalDate dataDeEntrega) {
        this.setDataDeEntrega(dataDeEntrega);
        return this;
    }

    public void setDataDeEntrega(LocalDate dataDeEntrega) {
        this.dataDeEntrega = dataDeEntrega;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public EntregaDiploma observacoes(String observacoes) {
        this.setObservacoes(observacoes);
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Processo getProcesso() {
        return this.processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public EntregaDiploma processo(Processo processo) {
        this.setProcesso(processo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntregaDiploma)) {
            return false;
        }
        return id != null && id.equals(((EntregaDiploma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntregaDiploma{" +
            "id=" + getId() +
            ", dataDeEntrega='" + getDataDeEntrega() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            "}";
    }
}
