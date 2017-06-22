/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.NIR;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class NIRDAOImpl {
    
    public void salvar(NIR nir) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(nir);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar NIR. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<NIR> listar() {
        List <NIR> listarNIR = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarNIR = session.createCriteria(NIR.class).list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar NIR. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarNIR;
    }
    
    public NIR listarPorId(Integer id) {
         
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (NIR) session.createCriteria(NIR.class)
                    .add(Restrictions.idEq(id))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }
}
