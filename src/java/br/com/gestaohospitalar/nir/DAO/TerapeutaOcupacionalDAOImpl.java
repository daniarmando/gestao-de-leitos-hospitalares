package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.TerapeutaOcupacional;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel
 */
public class TerapeutaOcupacionalDAOImpl {
    
    public void salvar(TerapeutaOcupacional terapeutaOcupacional) {
        
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(terapeutaOcupacional);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Terapeuta Ocupacional. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<TerapeutaOcupacional> listar() {
        List <TerapeutaOcupacional> listarTerapeutasOcupacionais = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarTerapeutasOcupacionais = session.createCriteria(TerapeutaOcupacional.class)
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Terapeutas Ocupacionais. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarTerapeutasOcupacionais;
    }
}
