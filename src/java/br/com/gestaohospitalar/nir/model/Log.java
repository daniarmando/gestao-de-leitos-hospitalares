/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "log")
public class Log implements Serializable {
    
    private Integer id;
    private String tipo;
    private String detalhe;
    private Date dataHora;
    private String objeto;
    private Integer idObjeto;
    private Usuario usuario;

    public Log() {
    }

    public Log(Integer id, String tipo, String detalhe, Date dataHora, String objeto, Integer idObjeto, Usuario usuario) {
        this.id = id;
        this.tipo = tipo;
        this.detalhe = detalhe;
        this.dataHora = dataHora;
        this.objeto = objeto;
        this.idObjeto = idObjeto;
        this.usuario = usuario;
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
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the detalhe
     */
    public String getDetalhe() {
        return detalhe;
    }

    /**
     * @param detalhe the detalhe to set
     */
    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

    /**
     * @return the dataHora
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHora() {
        return dataHora;
    }

    /**
     * @param dataHora the dataHora to set
     */
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * @return the objeto
     */
    public String getObjeto() {
        return objeto;
    }

    /**
     * @param objeto the objeto to set
     */
    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    /**
     * @return the idObjeto
     */
    public Integer getIdObjeto() {
        return idObjeto;
    }

    /**
     * @param idObjeto the idObjeto to set
     */
    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    /**
     * @return the usuario
     */
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.tipo);
        hash = 89 * hash + Objects.hashCode(this.detalhe);
        hash = 89 * hash + Objects.hashCode(this.dataHora);
        hash = 89 * hash + Objects.hashCode(this.objeto);
        hash = 89 * hash + Objects.hashCode(this.idObjeto);
        hash = 89 * hash + Objects.hashCode(this.usuario);
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
        final Log other = (Log) obj;
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.detalhe, other.detalhe)) {
            return false;
        }
        if (!Objects.equals(this.objeto, other.objeto)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dataHora, other.dataHora)) {
            return false;
        }
        if (!Objects.equals(this.idObjeto, other.idObjeto)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        return true;
    }
    
    
    
}
