/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import br.com.gestaohospitalar.nir.model.enumerator.TipoUsuario;
import br.com.gestaohospitalar.nir.util.NIRCriptografiaUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    private TipoUsuario tipo;
    private boolean status;
    private List<Autorizacao> autorizacoes;
    private Pessoa pessoa;

    public Usuario() {
    }

    public Usuario(Integer id, String login, String senha, TipoUsuario tipo, boolean status, List<Autorizacao> autorizacoes, Pessoa pessoa) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
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
    
    @Enumerated(EnumType.STRING)
    public TipoUsuario getTipo() {
        return this.tipo;
    }
    
    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
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
    
     public void criptografarSenha() {
        this.senha =  NIRCriptografiaUtil.md5(this.senha);
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
