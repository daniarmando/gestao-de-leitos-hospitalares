/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Higienizacao;
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
public class HigienizacaoDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    //Busca a última chaveMesAno
    String chaveMesAno = NIRDataUtil.ultimaChaveMesAno();
    
    public Higienizacao porId(Integer id) {
        return (Higienizacao) this.session.get(Higienizacao.class, id);
    }

    public void salvar(Higienizacao higienizacao) throws DAOException {

        try {
            this.session.saveOrUpdate(higienizacao);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Higienização. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Higienização.");
        }
    }

    public List<Higienizacao> todas() {

        return (List<Higienizacao>) this.session.createCriteria(Higienizacao.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                .createAlias("internacao.procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                .createAlias("internacao.cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                .createAlias("internacao.leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                .list();
    }

    public void excluir(Higienizacao higienizacao) throws DAOException {

        try {
            this.session.delete(higienizacao);
        } catch (Exception e) {
            System.out.println("Problemas ao excluir Higienização. Erro: " + e.getMessage());
            throw new DAOException(("Problemas ao excluir Higienização."));
        }
    }

//    public void excluirFuncionarios(Integer idHigienizacao) {
//        Session session = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//            session.beginTransaction();
//
//            String sql = "DELETE FROM higienizacao_funcionario WHERE higienizacao_idHigienizacao = " + idHigienizacao ;
//            
//            session.createSQLQuery(sql).executeUpdate();
//
//            session.getTransaction().commit();
//
//        } catch (Exception e) {
//            System.out.println("Problemas ao Excluir Funcionários da Higienização. Erro: " + e.getMessage());
//            session.getTransaction().rollback();
//
//        }
//    }
}
