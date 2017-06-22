/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Fisioterapeuta;
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
public class FisioterapeutaDAOImpl {
    
    public void salvar(Fisioterapeuta fisioterapeuta) {
        
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(fisioterapeuta);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Fisioterapeuta. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Fisioterapeuta> listar() {
        List <Fisioterapeuta> listarFisioterapeutas = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarFisioterapeutas = session.createCriteria(Fisioterapeuta.class)
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Fisioterapeuta. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarFisioterapeutas;
    }
}
