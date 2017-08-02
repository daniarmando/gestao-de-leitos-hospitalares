/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.Quarto;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class QuartoDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public Quarto quartoPorId(Integer id) {
        return (Quarto) this.session.get(Quarto.class, id);
    }

    public void salvar(Quarto quarto) throws DAOException {

        try {
            this.session.saveOrUpdate(quarto);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Quarto. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Quarto.");
        }

    }

    public List<Quarto> listar() {

        return (List<Quarto>) this.session.createCriteria(Quarto.class)
                .add(Restrictions.eq("statusQuarto", Status.ATIVO.get()))
                .list();
    }

    public List<Quarto> listarPorIdSetor(Integer idSetor) {

        return (List<Quarto>) this.session.createCriteria(Quarto.class)
                .add(Restrictions.eq("setor.idSetor", idSetor))
                .add(Restrictions.eq("statusQuarto", Status.ATIVO.get()))
                .list();
    }

    public boolean temLeito(Quarto quarto) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Leito.class)
                    .setProjection(Projections.count("idLeito"))
                    .add(Restrictions.eq("quarto", quarto))
                    .add(Restrictions.ne("statusLeito", Status.INDISPONIVEL.get()));

            resultado = (Long) crit.uniqueResult();

        } catch (Exception e) {
            System.out.println("Problemas ao verificar se o Quarto possuí Leitos. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }
}
