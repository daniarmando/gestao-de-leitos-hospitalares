/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Medico;
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
public class MedicoDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public Medico porId(Integer id) {
        return (Medico) this.session.get(Medico.class, id);
    }

    public void salvar(Medico medico) throws DAOException {

        try {
            this.session.saveOrUpdate(medico);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Médico. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Médico.");
        }
    }

    public List<Medico> ativos() {

        return (List<Medico>) this.session.createCriteria(Medico.class)
                .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                .addOrder(Order.asc("idPessoa"))
                .list();
    }

    public boolean estaEmInternacaoAberta(Medico medico) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("medico", medico))
                    .add(Restrictions.eq("statusInternacao", Status.ABERTA.get()));

            resultado = (Long) crit.uniqueResult();

        } catch (Exception e) {
            System.out.println("Problemas ao verificar se o Médico está em Internação aberta. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }

}
