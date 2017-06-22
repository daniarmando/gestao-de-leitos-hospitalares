/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model.sigtap;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "tb_tipo_leito")
public class TB_TIPO_LEITO implements Serializable {
    
    private String CO_TIPO_LEITO;
    private String NO_TIPO_LEITO;
    private Date DT_COMPETENCIA;
    private String chaveMesAno;

    public TB_TIPO_LEITO() {
    }

    public TB_TIPO_LEITO(String CO_TIPO_LEITO, String NO_TIPO_LEITO, Date DT_COMPETENCIA, String chaveMesAno) {
        this.CO_TIPO_LEITO = CO_TIPO_LEITO;
        this.NO_TIPO_LEITO = NO_TIPO_LEITO;
        this.DT_COMPETENCIA = DT_COMPETENCIA;
        this.chaveMesAno = chaveMesAno;        
    }

    /**
     * @return the CO_TIPO_LEITO
     */
    @Id
    public String getCO_TIPO_LEITO() {
        return CO_TIPO_LEITO;
    }

    /**
     * @param CO_TIPO_LEITO the CO_TIPO_LEITO to set
     */
    public void setCO_TIPO_LEITO(String CO_TIPO_LEITO) {
        this.CO_TIPO_LEITO = CO_TIPO_LEITO;
    }

    /**
     * @return the NO_TIPO_LEITO
     */
    public String getNO_TIPO_LEITO() {
        return NO_TIPO_LEITO;
    }

    /**
     * @param NO_TIPO_LEITO the NO_TIPO_LEITO to set
     */
    public void setNO_TIPO_LEITO(String NO_TIPO_LEITO) {
        this.NO_TIPO_LEITO = NO_TIPO_LEITO;
    }

    /**
     * @return the DT_COMPETENCIA
     */

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getDT_COMPETENCIA() {
        return DT_COMPETENCIA;
    }

    /**
     * @param DT_COMPETENCIA the DT_COMPETENCIA to set
     */
    public void setDT_COMPETENCIA(Date DT_COMPETENCIA) {
        this.DT_COMPETENCIA = DT_COMPETENCIA;
    }

    /**
     * @return the chaveMesAno
     */
    public String getChaveMesAno() {
        return chaveMesAno;
    }

    /**
     * @param chaveMesAno the chaveMesAno to set
     */
    public void setChaveMesAno(String chaveMesAno) {
        this.chaveMesAno = chaveMesAno;
    }

    //hashCode e equals não gerados pela IDE
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((CO_TIPO_LEITO == null) ? 0 : CO_TIPO_LEITO.hashCode());
        return result;
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
        TB_TIPO_LEITO other = (TB_TIPO_LEITO) obj;
        if (CO_TIPO_LEITO == null) {
            if (other.CO_TIPO_LEITO != null) {
                return false;
            }
        } else if (!CO_TIPO_LEITO.equals(other.CO_TIPO_LEITO)) {
            return false;
        }
        return true;
    }
    
}
