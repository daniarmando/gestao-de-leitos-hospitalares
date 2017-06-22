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
@Table (name = "tb_rubrica")
public class TB_RUBRICA implements Serializable {
    
    private String CO_RUBRICA;
    private String NO_RUBRICA;
    private Date DT_COMPETENCIA;
    private String chaveMesAno;

    public TB_RUBRICA() {
    }

    public TB_RUBRICA(String CO_RUBRICA, String NO_RUBRICA, Date DT_COMPETENCIA, String chaveMesAno) {
        this.CO_RUBRICA = CO_RUBRICA;
        this.NO_RUBRICA = NO_RUBRICA;
        this.DT_COMPETENCIA = DT_COMPETENCIA;
        this.chaveMesAno = chaveMesAno;
    }

    /**
     * @return the CO_RUBRICA
     */
    @Id
    public String getCO_RUBRICA() {
        return CO_RUBRICA;
    }

    /**
     * @param CO_RUBRICA the CO_RUBRICA to set
     */
    public void setCO_RUBRICA(String CO_RUBRICA) {
        this.CO_RUBRICA = CO_RUBRICA;
    }

    /**
     * @return the NO_RUBRICA
     */
    public String getNO_RUBRICA() {
        return NO_RUBRICA;
    }

    /**
     * @param NO_RUBRICA the NO_RUBRICA to set
     */
    public void setNO_RUBRICA(String NO_RUBRICA) {
        this.NO_RUBRICA = NO_RUBRICA;
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
     * @return the mesAnoImportacao
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.CO_RUBRICA);
        hash = 31 * hash + Objects.hashCode(this.NO_RUBRICA);
        hash = 31 * hash + Objects.hashCode(this.DT_COMPETENCIA);
        hash = 31 * hash + Objects.hashCode(this.chaveMesAno);
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
        final TB_RUBRICA other = (TB_RUBRICA) obj;
        if (!Objects.equals(this.CO_RUBRICA, other.CO_RUBRICA)) {
            return false;
        }
        if (!Objects.equals(this.NO_RUBRICA, other.NO_RUBRICA)) {
            return false;
        }
        if (!Objects.equals(this.chaveMesAno, other.chaveMesAno)) {
            return false;
        }
        if (!Objects.equals(this.DT_COMPETENCIA, other.DT_COMPETENCIA)) {
            return false;
        }
        return true;
    }
    
}
