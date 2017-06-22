/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Enfermeiro;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
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
public class EnfermeiroDAOImpl {
 
    public void salvar(Enfermeiro enfermeiro) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(enfermeiro);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Enfermeiro. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Enfermeiro> listar() {
        List <Enfermeiro> listarEnfermeiro = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarEnfermeiro = session.createCriteria(Enfermeiro.class)
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Enfermeiro. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarEnfermeiro;
    }
    
    public Enfermeiro listarPorId(Integer id) {
         
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Enfermeiro) session.createCriteria(Enfermeiro.class)
                    .add(Restrictions.idEq(id))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }
}
