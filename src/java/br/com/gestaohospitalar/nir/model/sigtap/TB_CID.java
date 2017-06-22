/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model.sigtap;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "tb_cid")
public class TB_CID implements Serializable {
    
    private String CO_CID;
    private String NO_CID;
    private String TP_AGRAVO;
    private String TP_SEXO;
    private String TP_ESTADIO;
    private Integer VL_CAMPOS_IRRADIADOS;
    private String chaveMesAno;

    public TB_CID() {
    }

    public TB_CID(String CO_CID, String NO_CID, String TP_AGRAVO, String TP_SEXO, String TP_ESTADIO, Integer VL_CAMPOS_IRRADIADOS, String chaveMesAno) {
        this.CO_CID = CO_CID;
        this.NO_CID = NO_CID;
        this.TP_AGRAVO = TP_AGRAVO;
        this.TP_SEXO = TP_SEXO;
        this.TP_ESTADIO = TP_ESTADIO;
        this.VL_CAMPOS_IRRADIADOS = VL_CAMPOS_IRRADIADOS;
        this.chaveMesAno = chaveMesAno;
    }

    /**
     * @return the CO_CID
     */
    @Id
    public String getCO_CID() {
        return CO_CID;
    }

    /**
     * @param CO_CID the CO_CID to set
     */
    public void setCO_CID(String CO_CID) {
        this.CO_CID = CO_CID;
    }

    /**
     * @return the NO_CID
     */
    public String getNO_CID() {
        return NO_CID;
    }

    /**
     * @param NO_CID the NO_CID to set
     */
    public void setNO_CID(String NO_CID) {
        this.NO_CID = NO_CID;
    }

    /**
     * @return the TP_AGRAVO
     */
    public String getTP_AGRAVO() {
        return TP_AGRAVO;
    }

    /**
     * @param TP_AGRAVO the TP_AGRAVO to set
     */
    public void setTP_AGRAVO(String TP_AGRAVO) {
        this.TP_AGRAVO = TP_AGRAVO;
    }

    /**
     * @return the TP_SEXO
     */
    public String getTP_SEXO() {
        return TP_SEXO;
    }

    /**
     * @param TP_SEXO the TP_SEXO to set
     */
    public void setTP_SEXO(String TP_SEXO) {
        this.TP_SEXO = TP_SEXO;
    }

    /**
     * @return the TP_ESTADIO
     */
    public String getTP_ESTADIO() {
        return TP_ESTADIO;
    }

    /**
     * @param TP_ESTADIO the TP_ESTADIO to set
     */
    public void setTP_ESTADIO(String TP_ESTADIO) {
        this.TP_ESTADIO = TP_ESTADIO;
    }

    /**
     * @return the VL_CAMPOS_IRRADIADOS
     */
    public Integer getVL_CAMPOS_IRRADIADOS() {
        return VL_CAMPOS_IRRADIADOS;
    }

    /**
     * @param VL_CAMPOS_IRRADIADOS the VL_CAMPOS_IRRADIADOS to set
     */
    public void setVL_CAMPOS_IRRADIADOS(Integer VL_CAMPOS_IRRADIADOS) {
        this.VL_CAMPOS_IRRADIADOS = VL_CAMPOS_IRRADIADOS;
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
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.CO_CID);
        hash = 41 * hash + Objects.hashCode(this.NO_CID);
        hash = 41 * hash + Objects.hashCode(this.TP_AGRAVO);
        hash = 41 * hash + Objects.hashCode(this.TP_SEXO);
        hash = 41 * hash + Objects.hashCode(this.TP_ESTADIO);
        hash = 41 * hash + Objects.hashCode(this.VL_CAMPOS_IRRADIADOS);
        hash = 41 * hash + Objects.hashCode(this.chaveMesAno);
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
        final TB_CID other = (TB_CID) obj;
        if (!Objects.equals(this.CO_CID, other.CO_CID)) {
            return false;
        }
        if (!Objects.equals(this.NO_CID, other.NO_CID)) {
            return false;
        }
        if (!Objects.equals(this.TP_AGRAVO, other.TP_AGRAVO)) {
            return false;
        }
        if (!Objects.equals(this.TP_SEXO, other.TP_SEXO)) {
            return false;
        }
        if (!Objects.equals(this.TP_ESTADIO, other.TP_ESTADIO)) {
            return false;
        }
        if (!Objects.equals(this.chaveMesAno, other.chaveMesAno)) {
            return false;
        }
        if (!Objects.equals(this.VL_CAMPOS_IRRADIADOS, other.VL_CAMPOS_IRRADIADOS)) {
            return false;
        }
        return true;
    }

    
}
