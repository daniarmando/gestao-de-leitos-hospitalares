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
@Table (name = "tb_financiamento")
public class TB_FINANCIAMENTO implements Serializable {
    
    private String CO_FINANCIAMENTO;
    private String NO_FINANCIAMENTO;
    private Date DT_COMPETENCIA;
    private String chaveMesAno;

    public TB_FINANCIAMENTO() {
    }

    public TB_FINANCIAMENTO(String CO_FINANCIAMENTO, String NO_FINANCIAMENTO, Date DT_COMPETENCIA, String chaveMesAno) {
        this.CO_FINANCIAMENTO = CO_FINANCIAMENTO;
        this.NO_FINANCIAMENTO = NO_FINANCIAMENTO;
        this.DT_COMPETENCIA = DT_COMPETENCIA;
        this.chaveMesAno = chaveMesAno;
    }

    /**
     * @return the CO_FINANCIAMENTO
     */
    @Id
    public String getCO_FINANCIAMENTO() {
        return CO_FINANCIAMENTO;
    }

    /**
     * @param CO_FINANCIAMENTO the CO_FINANCIAMENTO to set
     */
    public void setCO_FINANCIAMENTO(String CO_FINANCIAMENTO) {
        this.CO_FINANCIAMENTO = CO_FINANCIAMENTO;
    }

    /**
     * @return the NO_FINANCIAMENTO
     */
    public String getNO_FINANCIAMENTO() {
        return NO_FINANCIAMENTO;
    }

    /**
     * @param NO_FINANCIAMENTO the NO_FINANCIAMENTO to set
     */
    public void setNO_FINANCIAMENTO(String NO_FINANCIAMENTO) {
        this.NO_FINANCIAMENTO = NO_FINANCIAMENTO;
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
        hash = 59 * hash + Objects.hashCode(this.CO_FINANCIAMENTO);
        hash = 59 * hash + Objects.hashCode(this.NO_FINANCIAMENTO);
        hash = 59 * hash + Objects.hashCode(this.DT_COMPETENCIA);
        hash = 59 * hash + Objects.hashCode(this.chaveMesAno);
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
        final TB_FINANCIAMENTO other = (TB_FINANCIAMENTO) obj;
        if (!Objects.equals(this.CO_FINANCIAMENTO, other.CO_FINANCIAMENTO)) {
            return false;
        }
        if (!Objects.equals(this.NO_FINANCIAMENTO, other.NO_FINANCIAMENTO)) {
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

