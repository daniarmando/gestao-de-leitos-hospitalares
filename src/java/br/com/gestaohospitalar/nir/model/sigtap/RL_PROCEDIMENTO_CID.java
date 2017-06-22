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
@Table(name = "rl_procedimento_cid")
public class RL_PROCEDIMENTO_CID implements Serializable {

    private String ST_PRINCIPAL;
    private Date DT_COMPETENCIA;
    private String chaveMesAno;
    private TB_CID tb_cid;
    private TB_PROCEDIMENTO tb_procedimento;

    public RL_PROCEDIMENTO_CID() {
    }

    public RL_PROCEDIMENTO_CID(TB_PROCEDIMENTO tb_procedimento, String ST_PRINCIPAL, Date DT_COMPETENCIA, TB_CID tb_cid, String chaveMesAno) {
        this.tb_procedimento = tb_procedimento;
        this.ST_PRINCIPAL = ST_PRINCIPAL;
        this.DT_COMPETENCIA = DT_COMPETENCIA;
        this.tb_cid = tb_cid;
        this.chaveMesAno = chaveMesAno;
    }

    /**
     * @return the CO_PROCEDIMENTO
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "CO_PROCEDIMENTO")
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
     * @return the ST_PRINCIPAL
     */
    public String getST_PRINCIPAL() {
        return ST_PRINCIPAL;
    }

    /**
     * @param ST_PRINCIPAL the ST_PRINCIPAL to set
     */
    public void setST_PRINCIPAL(String ST_PRINCIPAL) {
        this.ST_PRINCIPAL = ST_PRINCIPAL;
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
     * @return the tb_cid
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "CO_CID")
    public TB_CID getTb_cid() {
        return tb_cid;
    }

    /**
     * @param tb_cid the tb_cid to set
     */
    public void setTb_cid(TB_CID tb_cid) {
        this.tb_cid = tb_cid;
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
        hash = 79 * hash + Objects.hashCode(this.ST_PRINCIPAL);
        hash = 79 * hash + Objects.hashCode(this.DT_COMPETENCIA);
        hash = 79 * hash + Objects.hashCode(this.tb_cid);
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
        final RL_PROCEDIMENTO_CID other = (RL_PROCEDIMENTO_CID) obj;
        if (!Objects.equals(this.ST_PRINCIPAL, other.ST_PRINCIPAL)) {
            return false;
        }
        if (!Objects.equals(this.chaveMesAno, other.chaveMesAno)) {
            return false;
        }
        if (!Objects.equals(this.tb_procedimento, other.tb_procedimento)) {
            return false;
        }
        if (!Objects.equals(this.DT_COMPETENCIA, other.DT_COMPETENCIA)) {
            return false;
        }
        if (!Objects.equals(this.tb_cid, other.tb_cid)) {
            return false;
        }
        return true;
    }

}
