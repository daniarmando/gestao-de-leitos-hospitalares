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
@Table (name = "gerenteenfermagem")
@PrimaryKeyJoinColumn(name = "idGerenteEnfermagem")
public class GerenteEnfermagem extends Pessoa implements Serializable, Cloneable {
    
    private String corenGerenteEnfermagem;
    private String statusGerenteEnfermagem;

    public GerenteEnfermagem() {
    }

    public GerenteEnfermagem(String corenGerenteEnfermagem, String statusGerenteEnfermagem, Integer idPessoa, String nomePessoa, String cpfPessoa, String rgPessoa, String sexoPessoa, Date dataNascimentoPessoa, String telefonePessoa, String celularPessoa, String enderecoPessoa, String numeroPessoa, String complementoPessoa, String bairroPessoa, String cepPessoa, String emailPessoa, String statusPessoa, Estado estado, Cidade cidade, Usuario usuario) {
        super(idPessoa, nomePessoa, cpfPessoa, rgPessoa, sexoPessoa, dataNascimentoPessoa, telefonePessoa, celularPessoa, enderecoPessoa, numeroPessoa, complementoPessoa, bairroPessoa, cepPessoa, emailPessoa, statusPessoa, estado, cidade);
        this.corenGerenteEnfermagem = corenGerenteEnfermagem;
        this.statusGerenteEnfermagem = statusGerenteEnfermagem;
    }

   
    /**
     * @return the corenGerenteEnfermagem
     */
    public String getCorenGerenteEnfermagem() {
        return corenGerenteEnfermagem;
    }

    /**
     * @param corenGerenteEnfermagem the corenGerenteEnfermagem to set
     */
    public void setCorenGerenteEnfermagem(String corenGerenteEnfermagem) {
        this.corenGerenteEnfermagem = corenGerenteEnfermagem;
    }

    /**
     * @return the statusGerenteEnfermagem
     */
    public String getStatusGerenteEnfermagem() {
        return statusGerenteEnfermagem;
    }

    /**
     * @param statusGerenteEnfermagem the statusGerenteEnfermagem to set
     */
    public void setStatusGerenteEnfermagem(String statusGerenteEnfermagem) {
        this.statusGerenteEnfermagem = statusGerenteEnfermagem;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.corenGerenteEnfermagem);
        hash = 29 * hash + Objects.hashCode(this.statusGerenteEnfermagem);
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
        final GerenteEnfermagem other = (GerenteEnfermagem) obj;
        if (!Objects.equals(this.corenGerenteEnfermagem, other.corenGerenteEnfermagem)) {
            return false;
        }
        if (!Objects.equals(this.statusGerenteEnfermagem, other.statusGerenteEnfermagem)) {
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
    public GerenteEnfermagem clone() {
        GerenteEnfermagem clone = new GerenteEnfermagem();
        clone.setCorenGerenteEnfermagem(corenGerenteEnfermagem);
        clone.setStatusGerenteEnfermagem(statusGerenteEnfermagem);
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
