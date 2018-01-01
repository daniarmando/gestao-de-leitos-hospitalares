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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "enfermeiro")
@PrimaryKeyJoinColumn(name = "idEnfermeiro")
public class Enfermeiro extends Pessoa implements Serializable, Cloneable {

    private String corenEnfermeiro;
    private String statusEnfermeiro;
    private GerenteEnfermagem gerenteEnfermagem;

    public Enfermeiro() {
    }

    public Enfermeiro(String corenEnfermeiro, String statusEnfermeiro, GerenteEnfermagem gerenteEnfermagem, Integer idPessoa, String nomePessoa, String cpfPessoa, String rgPessoa, String sexoPessoa, Date dataNascimentoPessoa, String telefonePessoa, String celularPessoa, String enderecoPessoa, String numeroPessoa, String complementoPessoa, String bairroPessoa, String cepPessoa, String emailPessoa, String statusPessoa, Estado estado, Cidade cidade) {
        super(idPessoa, nomePessoa, cpfPessoa, rgPessoa, sexoPessoa, dataNascimentoPessoa, telefonePessoa, celularPessoa, enderecoPessoa, numeroPessoa, complementoPessoa, bairroPessoa, cepPessoa, emailPessoa, statusPessoa, estado, cidade);
        this.corenEnfermeiro = corenEnfermeiro;
        this.statusEnfermeiro = statusEnfermeiro;
        this.gerenteEnfermagem = gerenteEnfermagem;
    }

    

    /**
     * @return the corenEnfermeiro
     */
    public String getCorenEnfermeiro() {
        return corenEnfermeiro;
    }

    /**
     * @param corenEnfermeiro the corenEnfermeiro to set
     */
    public void setCorenEnfermeiro(String corenEnfermeiro) {
        this.corenEnfermeiro = corenEnfermeiro;
    }

    /**
     * @return the statusEnfermeiro
     */
    public String getStatusEnfermeiro() {
        return statusEnfermeiro;
    }

    /**
     * @param statusEnfermeiro the statusEnfermeiro to set
     */
    public void setStatusEnfermeiro(String statusEnfermeiro) {
        this.statusEnfermeiro = statusEnfermeiro;
    }

    /**
     * @return the gerenteEnfermagem
     */
    @ManyToOne
    @JoinColumn(name = "idGerenteEnfermagem")
    public GerenteEnfermagem getGerenteEnfermagem() {
        return gerenteEnfermagem;
    }

    /**
     * @param gerenteEnfermagem the gerenteEnfermagem to set
     */
    public void setGerenteEnfermagem(GerenteEnfermagem gerenteEnfermagem) {
        this.gerenteEnfermagem = gerenteEnfermagem;
    }
   
    /**
     * Método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Enfermeiro clone() {
        Enfermeiro clone = new Enfermeiro();
        clone.setCorenEnfermeiro(corenEnfermeiro);
        clone.setStatusEnfermeiro(statusEnfermeiro);
        clone.setGerenteEnfermagem(gerenteEnfermagem);
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
    
      //hashCode e equals não gerados pela IDE
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPessoa == null) ? 0 : idPessoa.hashCode());
        return result;
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
        Enfermeiro other = (Enfermeiro) obj;
        if (idPessoa == null) {
            if (other.idPessoa != null) {
                return false;
            }
        } else if (!idPessoa.equals(other.idPessoa)) {
            return false;
        }
        return true;
    }
}
