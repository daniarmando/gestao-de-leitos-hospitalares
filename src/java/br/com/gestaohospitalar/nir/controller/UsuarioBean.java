/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.EnfermeiroDAOImpl;
import br.com.gestaohospitalar.nir.DAO.GerenteEnfermagemDAOImpl;
import br.com.gestaohospitalar.nir.DAO.NIRDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.model.Enfermeiro;
import br.com.gestaohospitalar.nir.model.GerenteEnfermagem;
import br.com.gestaohospitalar.nir.model.NIR;
import br.com.gestaohospitalar.nir.model.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public final class UsuarioBean implements Serializable {

    private Usuario usuario;
    private UsuarioDAOImpl daoUsuario = new UsuarioDAOImpl();

    private NIR nir;
    private NIRDAOImpl daoNIR = new NIRDAOImpl();

    private GerenteEnfermagem gerenteEnfermagem;
    private GerenteEnfermagemDAOImpl daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();

    private Enfermeiro enfermeiro;
    private EnfermeiroDAOImpl daoEnfermeiro = new EnfermeiroDAOImpl();

    String tipoAutorizacao = "";
    private Integer autorizacao = 0;

    public UsuarioBean() {

        this.usuario = new Usuario();

        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                //Recupera o login digitado pelo usuário 
                usuario.setLogin(((User) authentication.getPrincipal()).getUsername());

                //Lista o usuário por meio do seu login
                this.usuario = daoUsuario.usuarioPorLogin(this.usuario.getLogin());

                //Recupera as autorizações do usuário logado 
                List<Object> autorizacoes = new ArrayList(((User) authentication.getPrincipal()).getAuthorities());

                //Pega a autorização do usuário logado e atribui a variável String tipoAutorização
                for (Object a : autorizacoes) {
                    tipoAutorizacao = a.toString();
                }

                //atribui um valor para a variável Integer autorizacao de acordo com a autorização do usuário 
                //logado para depois utilizar na renderização de componentes JSF,
                //também traz uma lista da pessoa que pertence ao usuário logado através do ID
                switch (tipoAutorizacao) {
                    case "ROLE_nir":
                        this.autorizacao = 1;
                        this.nir = new NIR();
                        this.nir = this.daoNIR.listarPorId(this.usuario.getPessoa().getIdPessoa());
                        break;
                    case "ROLE_gen":
                        this.autorizacao = 2;
                        this.gerenteEnfermagem = new GerenteEnfermagem();
                        this.gerenteEnfermagem = this.daoGerenteEnfermagem.listarPorId(this.usuario.getPessoa().getIdPessoa());
                        break;
                    case "ROLE_enf":
                        this.autorizacao = 3;
                        this.enfermeiro = new Enfermeiro();
                        this.enfermeiro = this.daoEnfermeiro.listarPorId(this.usuario.getPessoa().getIdPessoa());
                        break;
                    default:
                        System.out.println("Erro");
                        break;
                }
            }
        }
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the nir
     */
    public NIR getNir() {
        return nir;
    }

    /**
     * @param nir the nir to set
     */
    public void setNir(NIR nir) {
        this.nir = nir;
    }

    /**
     * @return the gerenteEnfermagem
     */
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
     * @return the enfermeiro
     */
    public Enfermeiro getEnfermeiro() {
        return enfermeiro;
    }

    /**
     * @param enfermeiro the enfermeiro to set
     */
    public void setEnfermeiro(Enfermeiro enfermeiro) {
        this.enfermeiro = enfermeiro;
    }

    /**
     * @return the autorizacao
     */
    public Integer getAutorizacao() {
        return autorizacao;
    }

}