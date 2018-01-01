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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa implements Serializable {

    protected Integer idPessoa;
    protected String nomePessoa;
    protected String cpfPessoa;
    protected String rgPessoa;
    protected String sexoPessoa;
    protected Date dataNascimentoPessoa;
    protected String telefonePessoa;
    protected String celularPessoa;
    protected String enderecoPessoa;
    protected String numeroPessoa;
    protected String complementoPessoa;
    protected String bairroPessoa;
    protected String cepPessoa;
    protected String emailPessoa;
    protected String statusPessoa;
    protected Estado estado;
    protected Cidade cidade;

    public Pessoa() {
    }

    public Pessoa(Integer idPessoa, String nomePessoa, String cpfPessoa, String rgPessoa, String sexoPessoa, Date dataNascimentoPessoa, String telefonePessoa, String celularPessoa, String enderecoPessoa, String numeroPessoa, String complementoPessoa, String bairroPessoa, String cepPessoa, String emailPessoa, String statusPessoa, Estado estado, Cidade cidade) {
        this.idPessoa = idPessoa;
        this.nomePessoa = nomePessoa;
        this.cpfPessoa = cpfPessoa;
        this.rgPessoa = rgPessoa;
        this.sexoPessoa = sexoPessoa;
        this.dataNascimentoPessoa = dataNascimentoPessoa;
        this.telefonePessoa = telefonePessoa;
        this.celularPessoa = celularPessoa;
        this.enderecoPessoa = enderecoPessoa;
        this.numeroPessoa = numeroPessoa;
        this.complementoPessoa = complementoPessoa;
        this.bairroPessoa = bairroPessoa;
        this.cepPessoa = cepPessoa;
        this.emailPessoa = emailPessoa;
        this.statusPessoa = statusPessoa;
        this.estado = estado;
        this.cidade = cidade;
    }

    /**
     * @return the idPessoa
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdPessoa() {
        return idPessoa;
    }

    /**
     * @param idPessoa the idPessoa to set
     */
    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    /**
     * @return the nomePessoa
     */
    public String getNomePessoa() {
        return nomePessoa;
    }

    /**
     * @param nomePessoa the nomePessoa to set
     */
    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    /**
     * @return the cpfPessoa
     */
    public String getCpfPessoa() {
        return cpfPessoa;
    }

    /**
     * @param cpfPessoa the cpfPessoa to set
     */
    public void setCpfPessoa(String cpfPessoa) {
        this.cpfPessoa = cpfPessoa;
    }

    /**
     * @return the rgPessoa
     */
    public String getRgPessoa() {
        return rgPessoa;
    }

    /**
     * @param rgPessoa the rgPessoa to set
     */
    public void setRgPessoa(String rgPessoa) {
        this.rgPessoa = rgPessoa;
    }

    /**
     * @return the sexoPessoa
     */
    public String getSexoPessoa() {
        return sexoPessoa;
    }

    /**
     * @param sexoPessoa the sexoPessoa to set
     */
    public void setSexoPessoa(String sexoPessoa) {
        this.sexoPessoa = sexoPessoa;
    }

    /**
     * @return the dataNascimentoPessoa
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getDataNascimentoPessoa() {
        return dataNascimentoPessoa;
    }

    /**
     * @param dataNascimentoPessoa the dataNascimentoPessoa to set
     */
    public void setDataNascimentoPessoa(Date dataNascimentoPessoa) {
        this.dataNascimentoPessoa = dataNascimentoPessoa;
    }

    /**
     * @return the telefonePessoa
     */
    public String getTelefonePessoa() {
        return telefonePessoa;
    }

    /**
     * @param telefonePessoa the telefonePessoa to set
     */
    public void setTelefonePessoa(String telefonePessoa) {
        this.telefonePessoa = telefonePessoa;
    }

    /**
     * @return the celularPessoa
     */
    public String getCelularPessoa() {
        return celularPessoa;
    }

    /**
     * @param celularPessoa the celularPessoa to set
     */
    public void setCelularPessoa(String celularPessoa) {
        this.celularPessoa = celularPessoa;
    }

    /**
     * @return the enderecoPessoa
     */
    public String getEnderecoPessoa() {
        return enderecoPessoa;
    }

    /**
     * @param enderecoPessoa the enderecoPessoa to set
     */
    public void setEnderecoPessoa(String enderecoPessoa) {
        this.enderecoPessoa = enderecoPessoa;
    }

    /**
     * @return the numeroPessoa
     */
    public String getNumeroPessoa() {
        return numeroPessoa;
    }

    /**
     * @param numeroPessoa the numeroPessoa to set
     */
    public void setNumeroPessoa(String numeroPessoa) {
        this.numeroPessoa = numeroPessoa;
    }

    /**
     * @return the complementoPessoa
     */
    public String getComplementoPessoa() {
        return complementoPessoa;
    }

    /**
     * @param complementoPessoa the complementoPessoa to set
     */
    public void setComplementoPessoa(String complementoPessoa) {
        this.complementoPessoa = complementoPessoa;
    }

    /**
     * @return the bairroPessoa
     */
    public String getBairroPessoa() {
        return bairroPessoa;
    }

    /**
     * @param bairroPessoa the bairroPessoa to set
     */
    public void setBairroPessoa(String bairroPessoa) {
        this.bairroPessoa = bairroPessoa;
    }

    /**
     * @return the cepPessoa
     */
    public String getCepPessoa() {
        return cepPessoa;
    }

    /**
     * @param cepPessoa the cepPessoa to set
     */
    public void setCepPessoa(String cepPessoa) {
        this.cepPessoa = cepPessoa;
    }

    /**
     * @return the emailPessoa
     */
    public String getEmailPessoa() {
        return emailPessoa;
    }

    /**
     * @param emailPessoa the emailPessoa to set
     */
    public void setEmailPessoa(String emailPessoa) {
        this.emailPessoa = emailPessoa;
    }

    /**
     * @return the statusPessoa
     */
    public String getStatusPessoa() {
        return statusPessoa;
    }

    /**
     * @param statusPessoa the statusPessoa to set
     */
    public void setStatusPessoa(String statusPessoa) {
        this.statusPessoa = statusPessoa;
    }

    /**
     * @return the estado
     */
    @ManyToOne
    @JoinColumn(name = "idEstado")
    public Estado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * @return the cidade
     */
    @ManyToOne
    @JoinColumn(name = "idCidade")
    public Cidade getCidade() {
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
    
    public String nomeReduzidoPessoa() {
        return this.nomePessoa.split(" ")[0];
    }

    public String nomeReduzidoPessoa(String nome) {
        return nome.split(" ")[0];
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.idPessoa);
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
        final Pessoa other = (Pessoa) obj;
        if (!Objects.equals(this.idPessoa, other.idPessoa)) {
            return false;
        }
        return true;
    }

}
