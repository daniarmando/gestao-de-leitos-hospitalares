/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Daniel
 */
public class LeitoDAOImpl {
    
    //Busca a última chaveMesAno
    private String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();
    
     public Leito leitoPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        return (Leito) session.get(Leito.class, id);

    }
    
    public void salvar(Leito leito) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(leito);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Leito. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Leito> listar() {
        List <Leito> listarLeito = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
               Criteria crit = session.createCriteria(Leito.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                    .add(Restrictions.ne("statusLeito", Status.INDISPONIVEL.get()));
                    
            listarLeito = crit.list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Leito. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarLeito;
    }
    
     public List<Leito> listarParaInternacao() {
        List <Leito> listarLeito = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarLeito = session.createCriteria(Leito.class)
                     //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                    .createAlias("internacao.procedimento", "p", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("p.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.procedimento.tb_financiamento", "ptf", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("ptf.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.procedimento.tb_rubrica", "ptr", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("ptr.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.cid", "c", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("c.chaveMesAno", chaveMesAno))
                    .createAlias("tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                    //Passando as regras do status
                    .add(Restrictions.eq("statusLeito", Status.DISPONIVEL.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Leito. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarLeito;
    }
    
    public List<Leito> listarPorIdSetor(Integer idSetor) {
        List <Leito> listarLeitoPorIdSetor = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarLeitoPorIdSetor = session.createCriteria(Leito.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("quarto.setor", "s", JoinType.INNER_JOIN , Restrictions.eq("s.idSetor", idSetor))
                    .createAlias("tipo_leito", "tl", JoinType.INNER_JOIN , Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                    .add(Restrictions.ne("statusLeito", Status.INDISPONIVEL.get()))
                    .addOrder(Order.asc("quarto.idQuarto"))
                    .addOrder(Order.asc("idLeito"))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Leito pelo id do Setor. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarLeitoPorIdSetor;
    }
    
    public List<Leito> listarPorIdQuarto(Integer idQuarto) {
        List <Leito> listarLeitoPorIdQuarto = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarLeitoPorIdQuarto = session.createCriteria(Leito.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("tipo_leito", "tl", JoinType.INNER_JOIN , Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                    .add(Restrictions.eq("quarto.idQuarto", idQuarto))
                    .addOrder(Order.asc("idLeito"))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Leito pelo id do Quarto. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarLeitoPorIdQuarto;
    }
    
    public List<Leito> listarPorIdMedico(Integer  idMedico) {
        List <Leito> listarLeitoPorIdMedico = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarLeitoPorIdMedico = session.createCriteria(Leito.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("internacao.medico", "m", JoinType.INNER_JOIN , Restrictions.eq("m.idPessoa", idMedico))
                    .createAlias("tipo_leito", "tl", JoinType.INNER_JOIN , Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                    .add(Restrictions.eq("statusLeito", Status.INTERNACAO.get()))
                    .addOrder(Order.asc("quarto.idQuarto"))
                    .addOrder(Order.asc("idLeito"))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Leito pelo id do Médico. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarLeitoPorIdMedico;
    }
    
     public Boolean verificarSePossuiInternacaoAberta(Leito leito) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("leito", leito))
                    .add(Restrictions.eq("statusInternacao", Status.ABERTA.get()));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se o Leito possuí Internação aberta. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
}
