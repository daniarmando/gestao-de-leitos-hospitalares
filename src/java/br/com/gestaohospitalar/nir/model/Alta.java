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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "alta")
public class Alta implements Serializable {
    
    private Integer idAlta;
    private Date dataHoraInformacao;
    private Date dataHoraRealizacao;
    private Internacao internacao;
    private Medico medico;
    private AltaQualificada altaQualificada;

    public Alta() {
    }

    public Alta(Integer idAlta, Date dataHoraInformacao, Date dataHoraRealizacao, Internacao internacao, Medico medico, AltaQualificada altaQualificada) {
        this.idAlta = idAlta;
        this.dataHoraInformacao = dataHoraInformacao;
        this.dataHoraRealizacao = dataHoraRealizacao;
        this.internacao = internacao;
        this.medico = medico;
        this.altaQualificada = altaQualificada;
    }

    /**
     * @return the idAlta
     */
    @Id
    @GeneratedValue
    public Integer getIdAlta() {
        return idAlta;
    }

    /**
     * @param idAlta the idAlta to set
     */
    public void setIdAlta(Integer idAlta) {
        this.idAlta = idAlta;
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
     * @return the medico
     */
    @ManyToOne
    @JoinColumn (name = "idMedico")
    public Medico getMedico() {
        return medico;
    }

    /**
     * @param medico the medico to set
     */
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    /**
     * @return the altaQualificada
     */
    @OneToOne
    @JoinColumn(name = "idAltaQualificada")
    public AltaQualificada getAltaQualificada() {
        return altaQualificada;
    }

    /**
     * @param altaQualificada the altaQualificada to set
     */
    public void setAltaQualificada(AltaQualificada altaQualificada) {
        this.altaQualificada = altaQualificada;
    }

    /**
     * método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Alta clone() {
        Alta clone = new Alta();
        clone.setDataHoraInformacao(dataHoraInformacao);
        clone.setDataHoraRealizacao(dataHoraRealizacao);
        clone.setMedico(medico);
        
        return clone;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.idAlta);
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
        final Alta other = (Alta) obj;
        if (!Objects.equals(this.idAlta, other.idAlta)) {
            return false;
        }
        return true;
    }
    
}
