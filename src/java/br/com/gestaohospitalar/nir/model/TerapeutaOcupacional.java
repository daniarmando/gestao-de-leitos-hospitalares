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
@Table (name = "terapeutaocupacional")
@PrimaryKeyJoinColumn(name = "idTerapeutaOcupacional")
public class TerapeutaOcupacional extends Pessoa implements Serializable, Cloneable {
    
    private String crefitoTerapeutaOcupacional;
    private String statusTerapeutaOcupacional;

    public TerapeutaOcupacional() {
    }

    public TerapeutaOcupacional(String crefitoTerapeutaOcupacional, String statusTerapeutaOcupacional, Integer idPessoa, String nomePessoa, String cpfPessoa, String rgPessoa, String sexoPessoa, Date dataNascimentoPessoa, String telefonePessoa, String celularPessoa, String enderecoPessoa, String numeroPessoa, String complementoPessoa, String bairroPessoa, String cepPessoa, String emailPessoa, String statusPessoa, Estado estado, Cidade cidade, Usuario usuario) {
        super(idPessoa, nomePessoa, cpfPessoa, rgPessoa, sexoPessoa, dataNascimentoPessoa, telefonePessoa, celularPessoa, enderecoPessoa, numeroPessoa, complementoPessoa, bairroPessoa, cepPessoa, emailPessoa, statusPessoa, estado, cidade);
        this.crefitoTerapeutaOcupacional = crefitoTerapeutaOcupacional;
        this.statusTerapeutaOcupacional = statusTerapeutaOcupacional;
    }

    /**
     * @return the crefitoTerapeutaOcupacional
     */
    public String getCrefitoTerapeutaOcupacional() {
        return crefitoTerapeutaOcupacional;
    }

    /**
     * @param crefitoTerapeutaOcupacional the crefitoTerapeutaOcupacional to set
     */
    public void setCrefitoTerapeutaOcupacional(String crefitoTerapeutaOcupacional) {
        this.crefitoTerapeutaOcupacional = crefitoTerapeutaOcupacional;
    }

    /**
     * @return the statusTerapeutaOcupacional
     */
    public String getStatusTerapeutaOcupacional() {
        return statusTerapeutaOcupacional;
    }

    /**
     * @param statusTerapeutaOcupacional the statusTerapeutaOcupacional to set
     */
    public void setStatusTerapeutaOcupacional(String statusTerapeutaOcupacional) {
        this.statusTerapeutaOcupacional = statusTerapeutaOcupacional;
    }

     /**
     * Método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public TerapeutaOcupacional clone() {
        TerapeutaOcupacional clone = new TerapeutaOcupacional();
        clone.setCrefitoTerapeutaOcupacional(crefitoTerapeutaOcupacional);
        clone.setStatusTerapeutaOcupacional(statusTerapeutaOcupacional);
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
        TerapeutaOcupacional other = (TerapeutaOcupacional) obj;
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
