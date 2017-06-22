/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Alta;
import br.com.gestaohospitalar.nir.model.AltaQualificada;
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
public class AltaQualificadaDAOImpl {
    
    //Busca a última chaveMesAno
    private String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();
    
    public void salvar(AltaQualificada altaQualificada) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(altaQualificada);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar a Alta Qualificada. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public List<AltaQualificada> listar() {
        List <AltaQualificada> listarAltasQualificadas = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            Criteria crit = session.createCriteria(AltaQualificada.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                    .createAlias("internacao.procedimento", "p", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("p.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.cid", "c", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("c.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("tl.chaveMesAno", chaveMesAno));
            
            listarAltasQualificadas = crit.list();
            
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Altas Qualificadas. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarAltasQualificadas;
    }
    
    public AltaQualificada altaQualificadaPorIdInternacao(Integer idInternacao) {
         
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (AltaQualificada) session.createCriteria(AltaQualificada.class)
                    .add(Restrictions.eq("internacao.idInternacao", idInternacao))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }
      
      public void excluir(AltaQualificada altaQualificada) {
        
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.delete(altaQualificada);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao Excluir Alta Qualificada. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }
      
      public Boolean verificarSePossuiAlta(Integer idAltaQualificada) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Alta.class)
                    .setProjection(Projections.count("idAlta"))
                    .add(Restrictions.eq("altaQualificada.idAltaQualificada", idAltaQualificada));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se a Alta Qualificada está registrada em alguma Alta. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
    
}
