/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Alta;
import br.com.gestaohospitalar.nir.model.AltaQualificada;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Daniel
 */
public class AltaQualificadaDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    //Busca a última chaveMesAno
    private final String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();
    
    public AltaQualificada porId(Integer id) {
        return (AltaQualificada) this.session.get(AltaQualificada.class, id);
    }

    public void salvar(AltaQualificada altaQualificada) throws DAOException {

        try {
            this.session.saveOrUpdate(altaQualificada);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Alta Qualificada. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Alta Qualificada.");
        }
    }

    public List<AltaQualificada> todas() {

        return (List<AltaQualificada>) this.session.createCriteria(AltaQualificada.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                //passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                .createAlias("internacao.procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                .createAlias("internacao.cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                .createAlias("internacao.leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                .list();
    }

    public AltaQualificada porIdInternacao(Integer idInternacao) {

        return (AltaQualificada) this.session.createCriteria(AltaQualificada.class)
                .add(Restrictions.eq("internacao.idInternacao", idInternacao))
                .uniqueResult();

    }

    public void excluir(AltaQualificada altaQualificada) throws DAOException {

        try {
            this.session.delete(altaQualificada);
        } catch (Exception e) {
            System.out.println("Problemas ao excluir Alta Qualificada. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao excluir Alta Qualificada.");
        }
    }

    public boolean temAlta(Integer idAltaQualificada) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Alta.class)
                    .setProjection(Projections.count("idAlta"))
                    .add(Restrictions.eq("altaQualificada.idAltaQualificada", idAltaQualificada));

            resultado = (Long) crit.uniqueResult();

        } catch (Exception e) {
            System.out.println("Problemas ao verificar se a Alta Qualificada está registrada em alguma Alta. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }

}
