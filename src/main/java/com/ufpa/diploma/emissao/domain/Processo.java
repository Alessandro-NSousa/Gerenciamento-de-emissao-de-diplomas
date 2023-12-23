package com.ufpa.diploma.emissao.domain;

import com.ufpa.diploma.emissao.domain.enumeration.Enviado;
import com.ufpa.diploma.emissao.domain.enumeration.Status;
import com.ufpa.diploma.emissao.domain.enumeration.StatusProcesso;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Processo.
 */
@Entity
@Table(name = "processo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Processo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_processo")
    private StatusProcesso statusProcesso;

    @NotNull
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @Column(name = "nome")
    private String nome;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "numero_da_defesa")
    private String numeroDaDefesa;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_sigaa")
    private Status statusSigaa;

    @Column(name = "numero_sipac")
    private String numeroSipac;

    @Enumerated(EnumType.STRING)
    @Column(name = "enviado_biblioteca")
    private Enviado enviadoBiblioteca;

    @ManyToOne
    private Turma turma;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Processo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusProcesso getStatusProcesso() {
        return this.statusProcesso;
    }

    public Processo statusProcesso(StatusProcesso statusProcesso) {
        this.setStatusProcesso(statusProcesso);
        return this;
    }

    public void setStatusProcesso(StatusProcesso statusProcesso) {
        this.statusProcesso = statusProcesso;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Processo matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return this.nome;
    }

    public Processo nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Processo data(LocalDate data) {
        this.setData(data);
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getNumeroDaDefesa() {
        return this.numeroDaDefesa;
    }

    public Processo numeroDaDefesa(String numeroDaDefesa) {
        this.setNumeroDaDefesa(numeroDaDefesa);
        return this;
    }

    public void setNumeroDaDefesa(String numeroDaDefesa) {
        this.numeroDaDefesa = numeroDaDefesa;
    }

    public Status getStatusSigaa() {
        return this.statusSigaa;
    }

    public Processo statusSigaa(Status statusSigaa) {
        this.setStatusSigaa(statusSigaa);
        return this;
    }

    public void setStatusSigaa(Status statusSigaa) {
        this.statusSigaa = statusSigaa;
    }

    public String getNumeroSipac() {
        return this.numeroSipac;
    }

    public Processo numeroSipac(String numeroSipac) {
        this.setNumeroSipac(numeroSipac);
        return this;
    }

    public void setNumeroSipac(String numeroSipac) {
        this.numeroSipac = numeroSipac;
    }

    public Enviado getEnviadoBiblioteca() {
        return this.enviadoBiblioteca;
    }

    public Processo enviadoBiblioteca(Enviado enviadoBiblioteca) {
        this.setEnviadoBiblioteca(enviadoBiblioteca);
        return this;
    }

    public void setEnviadoBiblioteca(Enviado enviadoBiblioteca) {
        this.enviadoBiblioteca = enviadoBiblioteca;
    }

    public Turma getTurma() {
        return this.turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Processo turma(Turma turma) {
        this.setTurma(turma);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Processo)) {
            return false;
        }
        return id != null && id.equals(((Processo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Processo{" +
            "id=" + getId() +
            ", statusProcesso='" + getStatusProcesso() + "'" +
            ", matricula='" + getMatricula() + "'" +
            ", nome='" + getNome() + "'" +
            ", data='" + getData() + "'" +
            ", numeroDaDefesa='" + getNumeroDaDefesa() + "'" +
            ", statusSigaa='" + getStatusSigaa() + "'" +
            ", numeroSipac='" + getNumeroSipac() + "'" +
            ", enviadoBiblioteca='" + getEnviadoBiblioteca() + "'" +
            "}";
    }
}
