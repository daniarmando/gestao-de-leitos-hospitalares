/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Funcionario;
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
public class FuncionarioDAOImpl {
    
     public Funcionario funcionarioPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (Funcionario) session.get(Funcionario.class, id);

    }
    
    public void salvar(Funcionario funcionario) {
        
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(funcionario);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Funcionário. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Funcionario> listar() {
        List <Funcionario> listarFuncionarios = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarFuncionarios = session.createCriteria(Funcionario.class)
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get() ))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Funcionários. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarFuncionarios;
    }
}
