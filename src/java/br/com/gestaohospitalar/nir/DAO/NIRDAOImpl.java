/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.NIR;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class NIRDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public void salvar(NIR nir) throws DAOException {

        try {
            this.session.saveOrUpdate(nir);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar NIR. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar NIR.");
        }
    }

    public List<NIR> listar() {
        return (List<NIR>) this.session.createCriteria(NIR.class).list();
    }

    public NIR nirPorId(Integer id) {

        return (NIR) this.session.createCriteria(NIR.class)
                .add(Restrictions.idEq(id))
                .uniqueResult();
    }
}
