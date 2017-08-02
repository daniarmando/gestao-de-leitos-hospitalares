/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Parametros;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class ParametrosDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public Parametros parametrosPorId(Integer id) {
        return (Parametros) this.session.createCriteria(Parametros.class)
                .add(Restrictions.idEq(id))
                .uniqueResult();
    }

    public Parametros parametrosPorIdHospital(Integer idHospital) {
        return (Parametros) this.session.createCriteria(Parametros.class)
                .add(Restrictions.eq("hospital.idHospital", idHospital))
                .uniqueResult();
    }

    public void salvar(Parametros parametros) throws DAOException {

        try {
            this.session.saveOrUpdate(parametros);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Parâmetro. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Parâmetro.");
        }
    }
}
