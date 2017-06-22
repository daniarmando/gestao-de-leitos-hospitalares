/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Higienizacao;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Daniel
 */
public class HigienizacaoDAOImpl {

    //Busca a última chaveMesAno
    String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();

    public void salvar(Higienizacao higienizacao) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(higienizacao);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Higienização. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Higienizacao> listar() {
        List<Higienizacao> listarHigienizacoes = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Criteria crit = session.createCriteria(Higienizacao.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                    .createAlias("internacao.procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno));

            listarHigienizacoes = crit.list();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Higienizações. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarHigienizacoes;
    }

    public void excluir(Higienizacao higienizacao) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(higienizacao);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Problemas ao Excluir Higienização. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
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
//        } catch (HibernateException e) {
//            System.out.println("Problemas ao Excluir Funcionários da Higienização. Erro: " + e.getMessage());
//            session.getTransaction().rollback();
//
//        }
//    }

    public Boolean verificarSeLeitoPossuiNovaInternacao(Internacao internacao) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Leito.class)
                    .setProjection(Projections.count("idLeito"))
                    .add(Restrictions.eq("idLeito", internacao.getLeito().getIdLeito()))
                    .add(Restrictions.ne("internacao.idInternacao", internacao.getIdInternacao()));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar foi registrado saída do paciente do leito. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
}
