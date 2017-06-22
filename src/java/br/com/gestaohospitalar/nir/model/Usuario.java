/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable, Cloneable {

    private Integer id;
    private String login;
    private String senha;
    private boolean status;
    private List<Autorizacao> autorizacoes;
    private Pessoa pessoa;

    public Usuario() {
    }

    public Usuario(Integer id, String login, String senha, boolean status, List<Autorizacao> autorizacoes, Pessoa pessoa) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.status = status;
        this.autorizacoes = autorizacoes;
        this.pessoa = pessoa;
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
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the status
     */
    @Column(name = "status", columnDefinition = "BOOLEAN")
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return the autorizacoes
     */
    @OneToMany
    public List<Autorizacao> getAutorizacoes() {
        return autorizacoes;
    }

    /**
     * @param autorizacoes the autorizacoes to set
     */
    public void setAutorizacoes(List<Autorizacao> autorizacoes) {
        this.autorizacoes = autorizacoes;
    }

    /**
     * @return the pessoa
     */
    @ManyToOne
    @JoinColumn(name = "idPessoa")
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.login);
        hash = 97 * hash + Objects.hashCode(this.senha);
        hash = 97 * hash + (this.status ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.autorizacoes);
        hash = 97 * hash + Objects.hashCode(this.pessoa);
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
        final Usuario other = (Usuario) obj;
        if (this.status != other.status) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.senha, other.senha)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.autorizacoes, other.autorizacoes)) {
            return false;
        }
        if (!Objects.equals(this.pessoa, other.pessoa)) {
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
    public Usuario clone() {
        Usuario clone = new Usuario();
        clone.setLogin(login);
        clone.setSenha(senha);
        clone.setStatus(status);

        return clone;
    }

}
