/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Funcionario;
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
public class FuncionarioDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public Funcionario porId(Integer id) {
        return (Funcionario) this.session.get(Funcionario.class, id);
    }

    public void salvar(Funcionario funcionario) throws DAOException {

        try {
            this.session.saveOrUpdate(funcionario);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Funcionário. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Funcionário.");
        }
    }

    public List<Funcionario> ativos() {

        return (List<Funcionario>) this.session.createCriteria(Funcionario.class)
                .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                .addOrder(Order.asc("idPessoa"))
                .list();
    }
}
