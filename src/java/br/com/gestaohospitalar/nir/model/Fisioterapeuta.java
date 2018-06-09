/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table (name = "fisioterapeuta")
@PrimaryKeyJoinColumn(name = "idFisioterapeuta")
public class Fisioterapeuta extends Pessoa implements Serializable, Cloneable {
    
    private String crefitoFisioterapeuta;
    private String statusFisioterapeuta;

    public Fisioterapeuta() {
    }

    public Fisioterapeuta(String crefitoFisioterapeuta, String statusFisioterapeuta, Integer idPessoa, String nomePessoa, String cpfPessoa, String rgPessoa, String sexoPessoa, Date dataNascimentoPessoa, String telefonePessoa, String celularPessoa, String enderecoPessoa, String numeroPessoa, String complementoPessoa, String bairroPessoa, String cepPessoa, String emailPessoa, String statusPessoa, Estado estado, Cidade cidade, Usuario usuario) {
        super(idPessoa, nomePessoa, cpfPessoa, rgPessoa, sexoPessoa, dataNascimentoPessoa, telefonePessoa, celularPessoa, enderecoPessoa, numeroPessoa, complementoPessoa, bairroPessoa, cepPessoa, emailPessoa, statusPessoa, estado, cidade);
        this.crefitoFisioterapeuta = crefitoFisioterapeuta;
        this.statusFisioterapeuta = statusFisioterapeuta;
    }
    
    /**
     * @return the crefitoFisioterapeuta
     */
    public String getCrefitoFisioterapeuta() {
        return crefitoFisioterapeuta;
    }

    /**
     * @param crefitoFisioterapeuta the crefitoFisioterapeuta to set
     */
    public void setCrefitoFisioterapeuta(String crefitoFisioterapeuta) {
        this.crefitoFisioterapeuta = crefitoFisioterapeuta;
    }

    /**
     * @return the statusFisioterapeuta
     */
    public String getStatusFisioterapeuta() {
        return statusFisioterapeuta;
    }

    /**
     * @param statusFisioterapeuta the statusFisioterapeuta to set
     */
    public void setStatusFisioterapeuta(String statusFisioterapeuta) {
        this.statusFisioterapeuta = statusFisioterapeuta;
    }
    
    /**
     * método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Fisioterapeuta clone() {
        Fisioterapeuta clone = new Fisioterapeuta();
        clone.setCrefitoFisioterapeuta(crefitoFisioterapeuta);
        clone.setStatusFisioterapeuta(statusFisioterapeuta);
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
        Fisioterapeuta other = (Fisioterapeuta) obj;
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
