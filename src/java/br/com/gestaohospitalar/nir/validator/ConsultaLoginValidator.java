/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.validator;

/**
 *
 * @author Daniel
 */
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.model.Usuario;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ConsultaLoginValidator {

    public static Boolean verificar(Usuario usuario, Usuario copiaUsuario) {
        UsuarioDAOImpl daoUsuario = new UsuarioDAOImpl();

        //Se for cadastro novo
        if (usuario.getId() == null || usuario.getId().equals(0)) {
            if (daoUsuario.verificarUsuarioPorLogin(usuario.getLogin())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login já cadastrado no sistema.", null));
                return false;
            } else {
                return true;
            }

            //se for alteração
        } else {
            if (!usuario.getLogin().equals(copiaUsuario.getLogin())) {
                if (daoUsuario.verificarUsuarioPorLogin(usuario.getLogin())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login já cadastrado no sistema.", null));
                    return false;
                } else {
                    return true;
                }
            }
        }

        return true;
    }
}
