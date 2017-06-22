/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "higienizacao")
public class Higienizacao implements Serializable {

    private Integer idHigienizacao;
    private Internacao internacao;
    private Date dataHoraInicio;
    private Date dataHoraFim;
    private Integer tempoHigienizacaoMinutos;
    private String observacoesHigienizacao;
    private List<Funcionario> funcionarios;

    public Higienizacao() {
    }

    public Higienizacao(Integer idHigienizacao, Internacao internacao, Date dataHoraInicio, Date dataHoraFim, Integer tempoHigienizacaoMinutos, String observacoesHigienizacao, List<Funcionario> funcionarios) {
        this.idHigienizacao = idHigienizacao;
        this.internacao = internacao;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.tempoHigienizacaoMinutos = tempoHigienizacaoMinutos;
        this.observacoesHigienizacao = observacoesHigienizacao;
        this.funcionarios = funcionarios;
    }

    /**
     * @return the idHigienizacao
     */
    @Id
    @GeneratedValue
    public Integer getIdHigienizacao() {
        return idHigienizacao;
    }

    /**
     * @param idHigienizacao the idHigienizacao to set
     */
    public void setIdHigienizacao(Integer idHigienizacao) {
        this.idHigienizacao = idHigienizacao;
    }

    /**
     * @return the internacao
     */
    @ManyToOne
    @JoinColumn(name = "idInternacao")
    public Internacao getInternacao() {
        return internacao;
    }

    /**
     * @param internacao the internacao to set
     */
    public void setInternacao(Internacao internacao) {
        this.internacao = internacao;
    }

    /**
     * @return the dataHoraInicio
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    /**
     * @param dataHoraInicio the dataHoraInicio to set
     */
    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    /**
     * @return the dataHoraFim
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    /**
     * @param dataHoraFim the dataHoraFim to set
     */
    public void setDataHoraFim(Date dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    /**
     * @return the tempoHigienizacaoMinutos
     */
    public Integer getTempoHigienizacaoMinutos() {
        return tempoHigienizacaoMinutos;
    }

    /**
     * @param tempoHigienizacaoMinutos the tempoHigienizacaoMinutos to set
     */
    public void setTempoHigienizacaoMinutos(Integer tempoHigienizacaoMinutos) {
        this.tempoHigienizacaoMinutos = tempoHigienizacaoMinutos;
    }
    
     /**
     * @return the observacoesHigienizacao
     */
    public String getObservacoesHigienizacao() {
        return observacoesHigienizacao;
    }

    /**
     * @param observacoesHigienizacao the observacoesHigienizacao to set
     */
    public void setObservacoesHigienizacao(String observacoesHigienizacao) {
        this.observacoesHigienizacao = observacoesHigienizacao;
    }

    /**
     * @return the funcionarios
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "higienizacao_funcionario",
            joinColumns = {
                @JoinColumn(name = "higienizacao_idHigienizacao",
                        referencedColumnName = "idHigienizacao")},
            inverseJoinColumns = {
                @JoinColumn(name = "funcionarios_idFuncionario",
                        referencedColumnName = "idFuncionario")})
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    /**
     * @param funcionarios the funcionarios to set
     */
    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.idHigienizacao);
        hash = 31 * hash + Objects.hashCode(this.internacao);
        hash = 31 * hash + Objects.hashCode(this.dataHoraInicio);
        hash = 31 * hash + Objects.hashCode(this.dataHoraFim);
        hash = 31 * hash + Objects.hashCode(this.tempoHigienizacaoMinutos);
        hash = 31 * hash + Objects.hashCode(this.observacoesHigienizacao);
        hash = 31 * hash + Objects.hashCode(this.funcionarios);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Higienizacao other = (Higienizacao) obj;
        if (!Objects.equals(this.observacoesHigienizacao, other.observacoesHigienizacao)) {
            return false;
        }
        if (!Objects.equals(this.idHigienizacao, other.idHigienizacao)) {
            return false;
        }
        if (!Objects.equals(this.internacao, other.internacao)) {
            return false;
        }
        if (!Objects.equals(this.dataHoraInicio, other.dataHoraInicio)) {
            return false;
        }
        if (!Objects.equals(this.dataHoraFim, other.dataHoraFim)) {
            return false;
        }
        if (!Objects.equals(this.tempoHigienizacaoMinutos, other.tempoHigienizacaoMinutos)) {
            return false;
        }
        if (!Objects.equals(this.funcionarios, other.funcionarios)) {
            return false;
        }
        return true;
    }

     /**
     * Método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Higienizacao clone() {
        Higienizacao clone = new Higienizacao();
        clone.setIdHigienizacao(idHigienizacao);
        clone.setInternacao(internacao);
        clone.setDataHoraInicio(dataHoraInicio);
        clone.setDataHoraFim(dataHoraFim);
        clone.setTempoHigienizacaoMinutos(tempoHigienizacaoMinutos);
        clone.setFuncionarios(funcionarios);
        
        return clone;
    }

}
