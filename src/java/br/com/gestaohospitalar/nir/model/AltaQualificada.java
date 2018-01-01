/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "altaqualificada")
public class AltaQualificada implements Serializable {
    
    private Integer idAltaQualificada;
    private Date dataHoraInformacao;
    private Date dataHoraPrevisao;
    private Date dataHoraRealizacao;
    private Internacao internacao;

    public AltaQualificada() {
    }

    public AltaQualificada(Integer idAltaQualificada, Date dataHoraInformacao, Date dataHoraPrevisao, Date dataHoraRealizacao, Internacao internacao) {
        this.idAltaQualificada = idAltaQualificada;
        this.dataHoraInformacao = dataHoraInformacao;
        this.dataHoraPrevisao = dataHoraPrevisao;
        this.dataHoraRealizacao = dataHoraRealizacao;
        this.internacao = internacao;
    }

    /**
     * @return the idAltaQualificada
     */
    @Id
    @GeneratedValue
    public Integer getIdAltaQualificada() {
        return idAltaQualificada;
    }

    /**
     * @param idAltaQualificada the idAltaQualificada to set
     */
    public void setIdAltaQualificada(Integer idAltaQualificada) {
        this.idAltaQualificada = idAltaQualificada;
    }

    /**
     * @return the dataHoraInformacao
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHoraInformacao() {
        return dataHoraInformacao;
    }

    /**
     * @param dataHoraInformacao the dataHoraInformacao to set
     */
    public void setDataHoraInformacao(Date dataHoraInformacao) {
        this.dataHoraInformacao = dataHoraInformacao;
    }

    /**
     * @return the dataHoraPrevisao
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHoraPrevisao() {
        return dataHoraPrevisao;
    }

    /**
     * @param dataHoraPrevisao the dataHoraPrevisao to set
     */
    public void setDataHoraPrevisao(Date dataHoraPrevisao) {
        this.dataHoraPrevisao = dataHoraPrevisao;
    }

    /**
     * @return the dataHoraRealizacao
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHoraRealizacao() {
        return dataHoraRealizacao;
    }

    /**
     * @param dataHoraRealizacao the dataHoraRealizacao to set
     */
    public void setDataHoraRealizacao(Date dataHoraRealizacao) {
        this.dataHoraRealizacao = dataHoraRealizacao;
    }

    /**
     * @return the internacao
     */
    @ManyToOne
    @JoinColumn (name = "idInternacao")
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
     * método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public AltaQualificada clone() {
        AltaQualificada clone = new AltaQualificada();
        clone.setDataHoraInformacao(dataHoraInformacao);
        clone.setDataHoraPrevisao(dataHoraPrevisao);
        clone.setDataHoraRealizacao(dataHoraRealizacao);
        
        return clone;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.idAltaQualificada);
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
        final AltaQualificada other = (AltaQualificada) obj;
        if (!Objects.equals(this.idAltaQualificada, other.idAltaQualificada)) {
            return false;
        }
        return true;
    }        
    
}
