/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "sigtapuploadlog")
public class SigtapUploadLog implements Serializable{
    
    private Integer id;
    private Date dataImportacao;
    private String chaveMesAno;

    public SigtapUploadLog() {
    }

    public SigtapUploadLog(Integer id, Date dataImportacao, String chaveMesAno) {
        this.id = id;
        this.dataImportacao = dataImportacao;
        this.chaveMesAno = chaveMesAno;
    }

    /**
     * @return the id
     */
    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the dataImportacao
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getDataImportacao() {
        return dataImportacao;
    }

    /**
     * @param dataImportacao the dataImportacao to set
     */
    public void setDataImportacao(Date dataImportacao) {
        this.dataImportacao = dataImportacao;
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
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final SigtapUploadLog other = (SigtapUploadLog) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
