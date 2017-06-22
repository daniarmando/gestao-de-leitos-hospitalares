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
@Table (name = "tb_modalidade")
public class TB_MODALIDADE implements Serializable {
    
    private String CO_MODALIDADE;
    private String NO_MODALIDADE;
    private Date DT_COMPETENCIA;
    private String chaveMesAno;

    public TB_MODALIDADE() {
    }

    public TB_MODALIDADE(String CO_MODALIDADE, String NO_MODALIDADE, Date DT_COMPETENCIA, String chaveMesAno) {
        this.CO_MODALIDADE = CO_MODALIDADE;
        this.NO_MODALIDADE = NO_MODALIDADE;
        this.DT_COMPETENCIA = DT_COMPETENCIA;
        this.chaveMesAno = chaveMesAno;
    }

    /**
     * @return the CO_MODALIDADE
     */
    @Id
    public String getCO_MODALIDADE() {
        return CO_MODALIDADE;
    }

    /**
     * @param CO_MODALIDADE the CO_MODALIDADE to set
     */
    public void setCO_MODALIDADE(String CO_MODALIDADE) {
        this.CO_MODALIDADE = CO_MODALIDADE;
    }

    /**
     * @return the NO_MODALIDADE
     */
    public String getNO_MODALIDADE() {
        return NO_MODALIDADE;
    }

    /**
     * @param NO_MODALIDADE the NO_MODALIDADE to set
     */
    public void setNO_MODALIDADE(String NO_MODALIDADE) {
        this.NO_MODALIDADE = NO_MODALIDADE;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.CO_MODALIDADE);
        hash = 61 * hash + Objects.hashCode(this.NO_MODALIDADE);
        hash = 61 * hash + Objects.hashCode(this.DT_COMPETENCIA);
        hash = 61 * hash + Objects.hashCode(this.chaveMesAno);
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
        final TB_MODALIDADE other = (TB_MODALIDADE) obj;
        if (!Objects.equals(this.CO_MODALIDADE, other.CO_MODALIDADE)) {
            return false;
        }
        if (!Objects.equals(this.NO_MODALIDADE, other.NO_MODALIDADE)) {
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
