/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Enfermeiro;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class EnfermeiroDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public void salvar(Enfermeiro enfermeiro) throws DAOException {

        try {
            this.session.saveOrUpdate(enfermeiro);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Enfermeiro. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Enfermeiro.");
        }
    }

    public List<Enfermeiro> listar() {

        return (List<Enfermeiro>) this.session.createCriteria(Enfermeiro.class)
                .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                .list();
    }

    public Enfermeiro enfermeiroPorId(Integer id) {

        return (Enfermeiro) this.session.createCriteria(Enfermeiro.class)
                .add(Restrictions.idEq(id))
                .uniqueResult();
    }
}
