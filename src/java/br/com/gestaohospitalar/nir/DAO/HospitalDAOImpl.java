/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Hospital;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class HospitalDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public Hospital porId(Integer id) {
        return (Hospital) this.session.get(Hospital.class, id);
    }

    public void salvar(Hospital hospital) throws DAOException {
        
        try {
            this.session.saveOrUpdate(hospital);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Hospital. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Hospital.");
        }
    }

    public List<Hospital> ativos() {

        return (List<Hospital>) this.session.createCriteria(Hospital.class)
                .add(Restrictions.eq("statusHospital", Status.ATIVO.get()))
                .addOrder(Order.asc("idHospital"))
                .list();
    }
}
