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
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "estado")
public class Estado implements Serializable {
    
    private Integer idEstado;
    private String nomeEstado;
    private String ufEstado;

    public Estado() {
    }

    public Estado(Integer idEstado, String nomeEstado, String ufEstado) {
        this.idEstado = idEstado;
        this.nomeEstado = nomeEstado;
        this.ufEstado = ufEstado;
    }

    /**
     * @return the idEstado
     */
    @Id
    @GeneratedValue
    public Integer getIdEstado() {
        return idEstado;
    }

    /**
     * @param idEstado the idEstado to set
     */
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * @return the nomeEstado
     */
    public String getNomeEstado() {
        return nomeEstado;
    }

    /**
     * @param nomeEstado the nomeEstado to set
     */
    public void setNomeEstado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
    }

    /**
     * @return the ufEstado
     */
    public String getUfEstado() {
        return ufEstado;
    }

    /**
     * @param ufEstado the ufEstado to set
     */
    public void setUfEstado(String ufEstado) {
        this.ufEstado = ufEstado;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.idEstado);
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
        final Estado other = (Estado) obj;
        if (!Objects.equals(this.idEstado, other.idEstado)) {
            return false;
        }
        return true;
    }

}
