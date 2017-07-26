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
@Table(name = "configuracaokanban")
public class ConfiguracaoKanban implements Serializable {
    
    private Integer idConfiguracaoKanban;
    private Integer valorVerdeKanban;
    private Integer valorAmareloKanban;
    private Integer valorVermelhoKanban;
    private Hospital hospital;

    public ConfiguracaoKanban() {
    }

    public ConfiguracaoKanban(Integer idConfiguracaoKanban, Integer valorVerdeKanban, Integer valorAmareloKanban, Integer valorVermelhoKanban, Hospital hospital) {
        this.idConfiguracaoKanban = idConfiguracaoKanban;
        this.valorVerdeKanban = valorVerdeKanban;
        this.valorAmareloKanban = valorAmareloKanban;
        this.valorVermelhoKanban = valorVermelhoKanban;
        this.hospital = hospital;
    }

    /**
     * @return the idConfiguracaoKanban
     */
    @Id
    @GeneratedValue
    public Integer getIdConfiguracaoKanban() {
        return idConfiguracaoKanban;
    }

    /**
     * @param idConfiguracaoKanban the idConfiguracaoKanban to set
     */
    public void setIdConfiguracaoKanban(Integer idConfiguracaoKanban) {
        this.idConfiguracaoKanban = idConfiguracaoKanban;
    }

    /**
     * @return the valorVerdeKanban
     */
    public Integer getValorVerdeKanban() {
        return valorVerdeKanban;
    }

    /**
     * @param valorVerdeKanban the valorVerdeKanban to set
     */
    public void setValorVerdeKanban(Integer valorVerdeKanban) {
        this.valorVerdeKanban = valorVerdeKanban;
    }

    /**
     * @return the valorAmareloKanban
     */
    public Integer getValorAmareloKanban() {
        return valorAmareloKanban;
    }

    /**
     * @param valorAmareloKanban the valorAmareloKanban to set
     */
    public void setValorAmareloKanban(Integer valorAmareloKanban) {
        this.valorAmareloKanban = valorAmareloKanban;
    }

    /**
     * @return the valorVermelhoKanban
     */
    public Integer getValorVermelhoKanban() {
        return valorVermelhoKanban;
    }

    /**
     * @param valorVermelhoKanban the valorVermelhoKanban to set
     */
    public void setValorVermelhoKanban(Integer valorVermelhoKanban) {
        this.valorVermelhoKanban = valorVermelhoKanban;
    }
    
      /**
     * @return the hospital
     */
    @ManyToOne
    @JoinColumn (name = "idHospital")
    public Hospital getHospital() {
        return hospital;
    }

    /**
     * @param hospital the hospital to set
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.idConfiguracaoKanban);
        hash = 97 * hash + Objects.hashCode(this.valorVerdeKanban);
        hash = 97 * hash + Objects.hashCode(this.valorAmareloKanban);
        hash = 97 * hash + Objects.hashCode(this.valorVermelhoKanban);
        hash = 97 * hash + Objects.hashCode(this.hospital);
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
        final ConfiguracaoKanban other = (ConfiguracaoKanban) obj;
        if (!Objects.equals(this.idConfiguracaoKanban, other.idConfiguracaoKanban)) {
            return false;
        }
        if (!Objects.equals(this.valorVerdeKanban, other.valorVerdeKanban)) {
            return false;
        }
        if (!Objects.equals(this.valorAmareloKanban, other.valorAmareloKanban)) {
            return false;
        }
        if (!Objects.equals(this.valorVermelhoKanban, other.valorVermelhoKanban)) {
            return false;
        }
        if (!Objects.equals(this.hospital, other.hospital)) {
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
    public ConfiguracaoKanban clone() {
        ConfiguracaoKanban clone = new ConfiguracaoKanban();
        clone.setIdConfiguracaoKanban(idConfiguracaoKanban);
        clone.setValorVerdeKanban(valorVerdeKanban);
        clone.setValorAmareloKanban(valorAmareloKanban);
        clone.setValorVermelhoKanban(valorVermelhoKanban);
        clone.setHospital(hospital);
        
        return clone;
    }  
 
}
