/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Quarto;
import br.com.gestaohospitalar.nir.model.Setor;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class SetorDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public Setor porId(Integer id) {
        return (Setor) this.session.get(Setor.class, id);
    }

    public void salvar(Setor setor) throws DAOException {
        try {
            this.session.saveOrUpdate(setor);
        } catch (Exception e) {
            System.out.println("Problemas ao cadastrar Setor. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Setor.");
        }
    }

    public List<Setor> ativos() {

        return (List<Setor>) this.session.createCriteria(Setor.class)
                .add(Restrictions.eq("statusSetor", Status.ATIVO.get()))
                .addOrder(Order.asc("idSetor"))
                .list();
    }

    public boolean temQuarto(Setor setor) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Quarto.class)
                    .setProjection(Projections.count("idQuarto"))
                    .add(Restrictions.eq("setor", setor))
                    .add(Restrictions.eq("statusQuarto", Status.ATIVO.get()));

            resultado = (Long) crit.uniqueResult();
            
        } catch (Exception e) {
            System.out.println("Problemas ao verificar se o setor possuí quarto. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }
}
