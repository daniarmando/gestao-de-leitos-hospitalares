/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Fisioterapeuta;
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
public class FisioterapeutaDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");
    
    public Fisioterapeuta porId(Integer id) {
        return (Fisioterapeuta) this.session.get(Fisioterapeuta.class, id);
    }

    public void salvar(Fisioterapeuta fisioterapeuta) throws DAOException {

        try {
            this.session.saveOrUpdate(fisioterapeuta);
        } catch (Exception e) {
            System.out.println("Problemas ao cadastrar Fisioterapeuta. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Fisioterapeuta.");
        }
    }

    public List<Fisioterapeuta> ativos() {

        return (List<Fisioterapeuta>) this.session.createCriteria(Fisioterapeuta.class)
                .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                .addOrder(Order.asc("idPessoa"))
                .list();
    }
}
