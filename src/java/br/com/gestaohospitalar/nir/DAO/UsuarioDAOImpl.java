/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Usuario;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class UsuarioDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public void salvar(Usuario usuario) throws DAOException {

        try {
            this.session.saveOrUpdate(usuario);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Usuário. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Usuário.");
        }
    }

    public Usuario usuarioPorLogin(String login) {

        return (Usuario) this.session.createCriteria(Usuario.class)
                .add(Restrictions.eq("login", login))
                .uniqueResult();
    }

    public Usuario usuarioPorIdPessoa(Integer idPessoa) {

        return (Usuario) this.session.createCriteria(Usuario.class)
                .add(Restrictions.eq("pessoa.idPessoa", idPessoa))
                .uniqueResult();
    }

    public boolean isLoginCadastrado(String login) {

        Long resultado = (Long) this.session.createCriteria(Usuario.class)
                .setProjection(Projections.count("login"))
                .add(Restrictions.eq("login", login))
                .uniqueResult();

        return resultado > 0;
    }
}
