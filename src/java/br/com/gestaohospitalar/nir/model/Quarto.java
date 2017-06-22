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
@Table(name = "quarto")
public class Quarto implements Serializable {

    private Integer idQuarto;
    private String tipoQuarto;
    private String statusQuarto;
    private Setor setor;

    public Quarto() {
    }

    public Quarto(Integer idQuarto, String tipoQuarto, String statusQuarto, Setor setor) {
        this.idQuarto = idQuarto;
        this.tipoQuarto = tipoQuarto;
        this.statusQuarto = statusQuarto;
        this.setor = setor;
    }

    /**
     * @return the idQuarto
     */
    @Id
    @GeneratedValue
    public Integer getIdQuarto() {
        return idQuarto;
    }

    /**
     * @param idQuarto the idQuarto to set
     */
    public void setIdQuarto(Integer idQuarto) {
        this.idQuarto = idQuarto;
    }

    /**
     * @return the tipoQuarto
     */
    public String getTipoQuarto() {
        return tipoQuarto;
    }

    /**
     * @param tipoQuarto the tipoQuarto to set
     */
    public void setTipoQuarto(String tipoQuarto) {
        this.tipoQuarto = tipoQuarto;
    }

    /**
     * @return the setor
     */
    @ManyToOne
    @JoinColumn(table = "quarto", name = "idSetor")
    public Setor getSetor() {
        return setor;
    }

    /**
     * @param setor the setor to set
     */
    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    /**
     * @return the statusQuarto
     */
    public String getStatusQuarto() {
        return statusQuarto;
    }

    /**
     * @param statusQuarto the statusQuarto to set
     */
    public void setStatusQuarto(String statusQuarto) {
        this.statusQuarto = statusQuarto;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.idQuarto);
        hash = 89 * hash + Objects.hashCode(this.tipoQuarto);
        hash = 89 * hash + Objects.hashCode(this.statusQuarto);
        hash = 89 * hash + Objects.hashCode(this.setor);
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
        final Quarto other = (Quarto) obj;
        if (!Objects.equals(this.tipoQuarto, other.tipoQuarto)) {
            return false;
        }
        if (!Objects.equals(this.statusQuarto, other.statusQuarto)) {
            return false;
        }
        if (!Objects.equals(this.idQuarto, other.idQuarto)) {
            return false;
        }
        if (!Objects.equals(this.setor, other.setor)) {
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
    public Quarto clone() {
        Quarto clone = new Quarto();
        clone.setIdQuarto(idQuarto);
        clone.setTipoQuarto(tipoQuarto);
        clone.setStatusQuarto(statusQuarto);
        clone.setSetor(setor);

        return clone;
    }

}
