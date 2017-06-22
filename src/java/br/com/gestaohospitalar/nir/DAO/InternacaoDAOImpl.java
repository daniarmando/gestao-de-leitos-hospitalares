/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
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
public class InternacaoDAOImpl {

    //Busca a última chaveMesAno
    private String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();
    private Boolean isAltaQualificada = false;
    private Boolean isAlta = false;

    public Internacao internacaoPorId(Integer id) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            return (Internacao) session.createCriteria(Internacao.class)
                    .add(Restrictions.idEq(id))
                    .uniqueResult();

        } finally {
            session.close();
        }
    }

    public void salvar(Internacao internacao) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(internacao);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Internação. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Internacao> listar() {
        List<Internacao> listarInternacao = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            //Consulta as internações distinguindo pelo id da internação
            Criteria crit = session.createCriteria(Internacao.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            listarInternacao = crit.list();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Internações. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarInternacao;
    }

    public List<Internacao> listarParaHigienizacao() {
        //passando status da internação e da alta como critério para consulta
        return listarPorStatus(Status.HIGIENIZACAO.get(), Status.HIGIENIZACAO.get());
    }

    public List<Internacao> listarParaAltaQualificada() {
        isAltaQualificada = true;
        //passando status da internação e da alta como critério para consulta
        return listarPorStatus(Status.ABERTA.get(), Status.INTERNACAO.get());
    }

    public List<Internacao> listarParaAlta() {
        isAlta = true;
        //passando status da internação e da alta como critério para consulta
        return listarPorStatus(Status.ABERTA.get(), Status.INTERNACAO.get());
    }

    public List<Internacao> listarPorStatus(String statusInternacao, String statusLeito) {
        List<Internacao> listarInternacao = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Criteria crit = session.createCriteria(Internacao.class)
                    //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                    .createAlias("procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                    .createAlias("procedimento.tb_financiamento", "ptf", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptf.chaveMesAno", chaveMesAno))
                    .createAlias("procedimento.tb_rubrica", "ptr", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptr.chaveMesAno", chaveMesAno))
                    .createAlias("cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                    .createAlias("leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                    //Passando regras do status
                    .createAlias("leito", "l", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("l.statusLeito", statusLeito))
                    .add(Restrictions.eq("statusInternacao", statusInternacao));
            
            //se estiver listando para cadastrar alta qualificada
            if (isAltaQualificada) {
                crit.add(Restrictions.isNull("dataPrevisaoAlta"));
                crit.add(Restrictions.isNull("dataAlta"));
                isAltaQualificada = false;
            }
            
            //se estiver listando para cadastrar alta
            if (isAlta) {
                crit.add(Restrictions.isNull("dataAlta"));
                isAlta = false;
            }
          
            listarInternacao = crit.list();
            
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Internação por status. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return listarInternacao;
    }

    public List<Internacao> listarHistoricoInternacoesPorLeito(Integer idLeito) {
        List<Internacao> listarInternacao = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            listarInternacao = session.createCriteria(Internacao.class)
                    //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                    .createAlias("procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                    .createAlias("procedimento.tb_financiamento", "ptf", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptf.chaveMesAno", chaveMesAno))
                    .createAlias("procedimento.tb_rubrica", "ptr", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptr.chaveMesAno", chaveMesAno))
                    .createAlias("cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                    .createAlias("leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                    //Passando regras para consulta 
                    .add(Restrictions.eq("leito.idLeito", idLeito))
                    .add(Restrictions.ne("statusInternacao", Status.CANCELADA.get()))
                    .list();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Intenações por leito. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return listarInternacao;
    }
    
    public Boolean verificarSeHouveSaida(Integer idInternacao) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("idInternacao", idInternacao))
                    .add(Restrictions.isNotNull("dataSaidaLeito"));

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
