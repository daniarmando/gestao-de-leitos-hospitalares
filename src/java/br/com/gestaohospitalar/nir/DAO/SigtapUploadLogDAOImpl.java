/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.SigtapUploadLog;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

/**
 *
 * @author Daniel
 */
public class SigtapUploadLogDAOImpl {
    
    public void salvar(SigtapUploadLog sigtapUploadLog) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.save(sigtapUploadLog);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Log do SigtapUpload. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public void excluir() {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           
           String hql0 = "DELETE FROM SigtapUploadLog WHERE chaveMesAno = '" + ultimaChaveMesAno() + "'";
           session.createQuery(hql0).executeUpdate();
           
           session.getTransaction().commit();
           
        }catch (HibernateException e) {
            System.out.println("Problemas ao excluir Log do SigtapUpload. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public String ultimaChaveMesAno() {
        String ultimaChaveMesAno = "";

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Criteria crit = session.createCriteria(SigtapUploadLog.class);
            //definindo regras para trazer apenas a coluna ChaveMesAno  
            ProjectionList projection = Projections.projectionList();
            projection.add(Projections.property("chaveMesAno"));
            crit.setProjection(projection);
            crit.addOrder(Order.desc("dataImportacao"));
            crit.setMaxResults(1);
            
            ultimaChaveMesAno = (String) crit.uniqueResult();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao buscar última chaveMesAno. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return ultimaChaveMesAno;
    }

}
