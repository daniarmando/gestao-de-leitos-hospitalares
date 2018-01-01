/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Enfermeiro;
import br.com.gestaohospitalar.nir.model.GerenteEnfermagem;
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
public class GerenteEnfermagemDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public GerenteEnfermagem porId(Integer id) {
        return (GerenteEnfermagem) this.session.get(GerenteEnfermagem.class, id);
    }

    public void salvar(GerenteEnfermagem gerenteEnfermagem) throws DAOException {

        try {
            this.session.saveOrUpdate(gerenteEnfermagem);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Gerente de Enfermagem. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Gerente de Enfermagem.");
        }
    }

    public List<GerenteEnfermagem> ativos() {

        return (List<GerenteEnfermagem>) this.session.createCriteria(GerenteEnfermagem.class)
                .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                .addOrder(Order.asc("idPessoa"))
                .list();
    }

    public boolean temEnfermeiro(GerenteEnfermagem gerenteEnfermagem) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Enfermeiro.class)
                    .setProjection(Projections.count("idPessoa"))
                    .add(Restrictions.eq("gerenteEnfermagem", gerenteEnfermagem))
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()));

            resultado = (Long) crit.uniqueResult();

        } catch (Exception e) {
            System.out.println("Problemas ao verificar se o Gerente possuí Enfermeiro(s). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }
}
