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
@Table (name = "parametros")
public class Parametros implements Serializable {
    
    private Integer idParametros;    
    private Boolean leitoIncompativelPaciente;
    private Boolean procedimentoIncompativelLeito; 
    private Boolean procedimentoIncompativelPaciente;
    private Boolean cidIncompativelLeito;
    private Boolean cidIncompativelPaciente;
    
    private Hospital hospital;

    public Parametros() {
    }

    public Parametros(Integer idParametros, Boolean leitoIncompativelPaciente, Boolean procedimentoIncompativelLeito, Boolean procedimentoIncompativelPaciente, Boolean cidIncompativelLeito, Boolean cidIncompativelPaciente, Hospital hospital) {
        this.idParametros = idParametros;
        this.leitoIncompativelPaciente = leitoIncompativelPaciente;
        this.procedimentoIncompativelLeito = procedimentoIncompativelLeito;
        this.procedimentoIncompativelPaciente = procedimentoIncompativelPaciente;
        this.cidIncompativelLeito = cidIncompativelLeito;
        this.cidIncompativelPaciente = cidIncompativelPaciente;
        this.hospital = hospital;
    }

    /**
     * @return the idParametros
     */
    @Id
    @GeneratedValue
    public Integer getIdParametros() {
        return idParametros;
    }

    /**
     * @param idParametros the idParametros to set
     */
    public void setIdParametros(Integer idParametros) {
        this.idParametros = idParametros;
    }

    /**
     * libera registrar internação quando idade mínima e máxima e/ou 
     * sexo aceitos pelo leito for incompatível com a idade e/ou sexo do paciente
     * 
     * @return the leitoIncompativelPaciente
     */
    public Boolean getLeitoIncompativelPaciente() {
        return leitoIncompativelPaciente;
    }

    /**
     * @param leitoIncompativelPaciente the leitoIncompativelPaciente to set
     */
    public void setLeitoIncompativelPaciente(Boolean leitoIncompativelPaciente) {
        this.leitoIncompativelPaciente = leitoIncompativelPaciente;
    }

    /**
     * libera pesquisar todos os procedimentos na hora de selecionar o procedimento da internação 
     * e permite registrar internação com procedimento incompatível com o tipo do leito
     * 
     * @return the procedimentoIncompativelLeito
     */
    public Boolean getProcedimentoIncompativelLeito() {
        return procedimentoIncompativelLeito;
    }

    /**
     * @param procedimentoIncompativelLeito the procedimentoIncompativelLeito to set
     */
    public void setProcedimentoIncompativelLeito(Boolean procedimentoIncompativelLeito) {
        this.procedimentoIncompativelLeito = procedimentoIncompativelLeito;
    }

    /**
     * libera registrar internação quando idade mínima e máxima e/ou 
     * sexo aceitos pelo procedimento for incompatível com a idade e/ou sexo do paciente
     * 
     * @return the procedimentoIncompativelPaciente
     */
    public Boolean getProcedimentoIncompativelPaciente() {
        return procedimentoIncompativelPaciente;
    }

    /**
     * @param procedimentoIncompativelPaciente the procedimentoIncompativelPaciente to set
     */
    public void setProcedimentoIncompativelPaciente(Boolean procedimentoIncompativelPaciente) {
        this.procedimentoIncompativelPaciente = procedimentoIncompativelPaciente;
    }
    
     /**
     * libera registrar internação quando tipo sexo aceito pelo cid for 
     * incompatível com o tipo sexo do leito
     * 
     * @return the cidIncompativelLeito
     */
    public Boolean getCidIncompativelLeito() {
        return cidIncompativelLeito;
    }

    /**
     * @param cidIncompativelLeito the cidIncompativelLeito to set
     */
    public void setCidIncompativelLeito(Boolean cidIncompativelLeito) {
        this.cidIncompativelLeito = cidIncompativelLeito;
    }
    
     /**
     * libera registrar internação quando tipo sexo aceito pelo cid for 
     * incompatível com o sexo do paciente
     * 
     * @return the cidIncompativelPaciente
     */
    public Boolean getCidIncompativelPaciente() {
        return cidIncompativelPaciente;
    }

    /**
     * @param cidIncompativelPaciente the cidIncompativelPaciente to set
     */
    public void setCidIncompativelPaciente(Boolean cidIncompativelPaciente) {
        this.cidIncompativelPaciente = cidIncompativelPaciente;
    }

    /**
     * @return the hospital
     */
    @ManyToOne
    @JoinColumn(name = "idHospital")
    public Hospital getHospital() {
        return hospital;
    }

    /**
     * @param hospital the hospital to set
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.idParametros);
        hash = 29 * hash + Objects.hashCode(this.leitoIncompativelPaciente);
        hash = 29 * hash + Objects.hashCode(this.procedimentoIncompativelLeito);
        hash = 29 * hash + Objects.hashCode(this.procedimentoIncompativelPaciente);
        hash = 29 * hash + Objects.hashCode(this.cidIncompativelLeito);
        hash = 29 * hash + Objects.hashCode(this.cidIncompativelPaciente);
        hash = 29 * hash + Objects.hashCode(this.hospital);
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
        final Parametros other = (Parametros) obj;
        if (!Objects.equals(this.idParametros, other.idParametros)) {
            return false;
        }
        if (!Objects.equals(this.leitoIncompativelPaciente, other.leitoIncompativelPaciente)) {
            return false;
        }
        if (!Objects.equals(this.procedimentoIncompativelLeito, other.procedimentoIncompativelLeito)) {
            return false;
        }
        if (!Objects.equals(this.procedimentoIncompativelPaciente, other.procedimentoIncompativelPaciente)) {
            return false;
        }
        if (!Objects.equals(this.cidIncompativelLeito, other.cidIncompativelLeito)) {
            return false;
        }
        if (!Objects.equals(this.cidIncompativelPaciente, other.cidIncompativelPaciente)) {
            return false;
        }
        if (!Objects.equals(this.hospital, other.hospital)) {
            return false;
        }
        return true;
    }

}
