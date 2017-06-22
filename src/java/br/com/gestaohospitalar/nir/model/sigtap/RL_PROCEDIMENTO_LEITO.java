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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "rl_procedimento_leito")
public class RL_PROCEDIMENTO_LEITO implements Serializable {
    
    private Date DT_COMPETENCIA;
    private String chaveMesAno;
    private TB_PROCEDIMENTO tb_procedimento;
    private TB_TIPO_LEITO tb_tipo_leito;    

    public RL_PROCEDIMENTO_LEITO() {
    }

    public RL_PROCEDIMENTO_LEITO(TB_PROCEDIMENTO tb_procedimento, Date DT_COMPETENCIA, TB_TIPO_LEITO tb_tipo_leito, String chaveMesAno) {
        this.tb_procedimento = tb_procedimento;
        this.DT_COMPETENCIA = DT_COMPETENCIA;
        this.tb_tipo_leito = tb_tipo_leito;
        this.chaveMesAno = chaveMesAno;
    }

    /**
     * @return the tb_procedimento
     */
    @Id
    @ManyToOne
    @JoinColumn(name="CO_PROCEDIMENTO")
    public TB_PROCEDIMENTO getTb_procedimento() {
        return tb_procedimento;
    }

    /**
     * @param tb_procedimento the tb_procedimento to set
     */
    public void setTb_procedimento(TB_PROCEDIMENTO tb_procedimento) {
        this.tb_procedimento = tb_procedimento;
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
     * @return the tb_tipo_leito
     */
    @Id
    @ManyToOne
    @JoinColumn(name="CO_TIPO_LEITO")
    public TB_TIPO_LEITO getTb_tipo_leito() {
        return tb_tipo_leito;
    }

    /**
     * @param tb_tipo_leito the tb_tipo_leito to set
     */
    public void setTb_tipo_leito(TB_TIPO_LEITO tb_tipo_leito) {
        this.tb_tipo_leito = tb_tipo_leito;
    }

    /**
     * @return the chaveMesAno
     */
    @Id
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
        hash = 79 * hash + Objects.hashCode(this.tb_procedimento);
        hash = 79 * hash + Objects.hashCode(this.DT_COMPETENCIA);
        hash = 79 * hash + Objects.hashCode(this.tb_tipo_leito);
        hash = 79 * hash + Objects.hashCode(this.chaveMesAno);
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
        final RL_PROCEDIMENTO_LEITO other = (RL_PROCEDIMENTO_LEITO) obj;
        if (!Objects.equals(this.chaveMesAno, other.chaveMesAno)) {
            return false;
        }
        if (!Objects.equals(this.tb_procedimento, other.tb_procedimento)) {
            return false;
        }
        if (!Objects.equals(this.DT_COMPETENCIA, other.DT_COMPETENCIA)) {
            return false;
        }
        if (!Objects.equals(this.tb_tipo_leito, other.tb_tipo_leito)) {
            return false;
        }
        return true;
    }
    
}
