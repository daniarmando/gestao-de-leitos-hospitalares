/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Alta;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Daniel
 */
public class AltaDAOImpl {
    
    //Busca a última chaveMesAno
    private String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();
    
    public void salvar(Alta alta) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(alta);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar a Alta. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public List<Alta> listar() {
        List <Alta> listarAltas = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            Criteria crit = session.createCriteria(Alta.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                    .createAlias("internacao.procedimento", "p", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("p.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.cid", "c", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("c.chaveMesAno", chaveMesAno))
                    .createAlias("internacao.leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN , Restrictions.eq("tl.chaveMesAno", chaveMesAno));
            
            listarAltas = crit.list();
            
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Altas. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarAltas;
    }
    
    public void excluir(Alta alta) {
        
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.delete(alta);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao Excluir Alta. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
