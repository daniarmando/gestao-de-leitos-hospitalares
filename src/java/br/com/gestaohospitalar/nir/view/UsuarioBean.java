/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.HospitalDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.model.Hospital;
import br.com.gestaohospitalar.nir.model.Usuario;
import br.com.gestaohospitalar.nir.model.enumerator.TipoUsuario;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
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

    private UsuarioDAOImpl daoUsuario;
    private Usuario usuario;       
    
    private Hospital hospital;

    //String tipoAutorizacao = "";
    private Integer autorizacao = 0;

    //usado para abrir ou fechar o notification bar de chat, começa true para começar abrindo
    private boolean isOpenNBChat = true; 

    public UsuarioBean() {

        this.usuario = new Usuario();
        this.daoUsuario = new UsuarioDAOImpl();

        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                //recupera o login digitado pelo usuário 
                usuario.setLogin(((User) authentication.getPrincipal()).getUsername());

                //lista o usuário por meio do seu login
                this.usuario = this.daoUsuario.porLogin(this.usuario.getLogin());               

                //recupera as autorizações do usuário logado 
                //List<Object> autorizacoes = new ArrayList(((User) authentication.getPrincipal()).getAuthorities());

                //pega a autorização do usuário logado e atribui a variável String tipoAutorização
                //for (Object a : autorizacoes) {
                    //this.tipoAutorizacao = a.toString();
                //}

                
                //monta a autorizaçao do usuario
                switch (TipoUsuario.valueOf(this.usuario.getTipo())) {
                    case NIR:
                        this.autorizacao = 1;
                        break;
                    case MEDICO:
                    case GERENTE_ENFERMAGEM:
                        this.autorizacao = 2;
                        break;
                    case ENFERMEIRO:
                        this.autorizacao = 3;
                        break;
                    default:
                        System.out.println("Erro");
                        break;
                }
                
                //carrega o id do hospital
                //passa o id manualmente, depois tem que corrigir relacionar a pessoa ou usuario com a tabela hospital
                this.hospital = new HospitalDAOImpl().porId(1); 
            }
        }
    }
    
    public String getSair() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/j_spring_security_logout";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getAutorizacao() {
        return autorizacao;
    }
    
    public Hospital getHospital() {
        return this.hospital;
    }    
    
    public void abreFechaNB() {
        if (this.isOpenNBChat) {
            this.isOpenNBChat = false;
            RequestContext.getCurrentInstance().execute("PF('nbJanelaChat').show();");
        } else {
            this.isOpenNBChat = true;
            RequestContext.getCurrentInstance().execute("PF('nbJanelaChat').hide();");
        }

    }

}
