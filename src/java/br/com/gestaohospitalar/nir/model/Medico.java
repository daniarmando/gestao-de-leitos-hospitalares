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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "medico")
@PrimaryKeyJoinColumn(name = "idMedico")
public class Medico extends Pessoa implements Serializable, Cloneable {
    
    private Integer crmMedico;
    private String statusMedico;

    public Medico() {
    }

    public Medico(Integer crmMedico, String statusMedico, Integer idPessoa, String nomePessoa, String cpfPessoa, String rgPessoa, String sexoPessoa, Date dataNascimentoPessoa, String telefonePessoa, String celularPessoa, String enderecoPessoa, String numeroPessoa, String complementoPessoa, String bairroPessoa, String cepPessoa, String emailPessoa, String statusPessoa, Estado estado, Cidade cidade, Usuario usuario) {
        super(idPessoa, nomePessoa, cpfPessoa, rgPessoa, sexoPessoa, dataNascimentoPessoa, telefonePessoa, celularPessoa, enderecoPessoa, numeroPessoa, complementoPessoa, bairroPessoa, cepPessoa, emailPessoa, statusPessoa, estado, cidade);
        this.crmMedico = crmMedico;
        this.statusMedico = statusMedico;
    }
 
    /**
     * @return the crmMedico
     */
    public Integer getCrmMedico() {
        return crmMedico;
    }

    /**
     * @param crmMedico the crmMedico to set
     */
    public void setCrmMedico(Integer crmMedico) {
        this.crmMedico = crmMedico;
    }

    /**
     * @return the statusMedico
     */
    public String getStatusMedico() {
        return statusMedico;
    }

    /**
     * @param statusMedico the statusMedico to set
     */
    public void setStatusMedico(String statusMedico) {
        this.statusMedico = statusMedico;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.crmMedico);
        hash = 17 * hash + Objects.hashCode(this.statusMedico);
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
        final Medico other = (Medico) obj;
        if (!Objects.equals(this.statusMedico, other.statusMedico)) {
            return false;
        }
        if (!Objects.equals(this.crmMedico, other.crmMedico)) {
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
    public Medico clone() {
        Medico clone = new Medico();
        clone.setCrmMedico(crmMedico);
        clone.setStatusMedico(statusMedico);
        clone.setIdPessoa(idPessoa);
        clone.setNomePessoa(nomePessoa);
        clone.setCpfPessoa(cpfPessoa);
        clone.setRgPessoa(rgPessoa);
        clone.setSexoPessoa(sexoPessoa);
        clone.setDataNascimentoPessoa(dataNascimentoPessoa);
        clone.setTelefonePessoa(telefonePessoa);
        clone.setCelularPessoa(celularPessoa);
        clone.setEnderecoPessoa(enderecoPessoa);
        clone.setNumeroPessoa(numeroPessoa);
        clone.setComplementoPessoa(complementoPessoa);
        clone.setBairroPessoa(bairroPessoa);
        clone.setCepPessoa(cepPessoa);
        clone.setEmailPessoa(emailPessoa);
        clone.setStatusPessoa(statusPessoa);
        clone.setEstado(estado);
        clone.setCidade(cidade);

        return clone;
    }
}
