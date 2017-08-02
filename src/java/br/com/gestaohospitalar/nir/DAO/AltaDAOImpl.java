/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Alta;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Daniel
 */
public class AltaDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    //Busca a última chaveMesAno
    private final String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();

    public void salvar(Alta alta) throws DAOException {

        try {
            this.session.saveOrUpdate(alta);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar a Alta. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Alta.");
        }
    }

    public List<Alta> listar() {

        return (List<Alta>) this.session.createCriteria(Alta.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                .createAlias("internacao.procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                .createAlias("internacao.cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                .createAlias("internacao.leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                .list();
    }

    public void excluir(Alta alta) throws DAOException {

        try {
            this.session.delete(alta);
        } catch (Exception e) {
            System.out.println("Problemas ao excluir Alta. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao excluir Alta");
        }
    }
}
