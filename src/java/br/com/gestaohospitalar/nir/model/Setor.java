/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "setor")
public class Setor implements Serializable {
    
    private Integer idSetor;
    private String tipoSetor;
    private String statusSetor; 
    private Hospital hospital;
    
    public Setor() {
    }

    public Setor(Integer idSetor, String tipoSetor, String statusSetor, Hospital hospital) {
        this.idSetor = idSetor;
        this.tipoSetor = tipoSetor;
        this.statusSetor = statusSetor;
        this.hospital = hospital;
    }

    /**
     * @return the idSetor
     */
    @Id
    @GeneratedValue
    public Integer getIdSetor() {
        return idSetor;
    }

    /**
     * @param idSetor the idSetor to set
     */
    public void setIdSetor(Integer idSetor) {
        this.idSetor = idSetor;
    }

    /**
     * @return the tipoSetor
     */
    public String getTipoSetor() {
        return tipoSetor;
    }

    /**
     * @param tipoSetor the tipoSetor to set
     */
    public void setTipoSetor(String tipoSetor) {
        this.tipoSetor = tipoSetor;
    }

    /**
     * @return the statusSetor
     */
    public String getStatusSetor() {
        return statusSetor;
    }

    /**
     * @param statusSetor the statusSetor to set
     */
    public void setStatusSetor(String statusSetor) {
        this.statusSetor = statusSetor;
    }

    /**
     * @return the hospital
     */
    @ManyToOne
    @JoinColumn(name="idHospital")
    public Hospital getHospital() {
        return hospital;
    }

    /**
     * @param hospital the hospital to set
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

     /**
     * Método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Setor clone() {
        Setor clone = new Setor();
        clone.setIdSetor(idSetor);
        clone.setTipoSetor(tipoSetor);
        clone.setStatusSetor(statusSetor);
        clone.setHospital(hospital);

        return clone;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.idSetor);
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
        final Setor other = (Setor) obj;
        if (!Objects.equals(this.idSetor, other.idSetor)) {
            return false;
        }
        return true;
    }
    
}
