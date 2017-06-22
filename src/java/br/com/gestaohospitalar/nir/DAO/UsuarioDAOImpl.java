/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Usuario;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class UsuarioDAOImpl {
    
    public void salvar(Usuario usuario) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(usuario);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Usuário. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }
   
     public Usuario usuarioPorLogin(String login) {
         
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Usuario) session.createCriteria(Usuario.class)
                    .add(Restrictions.eq("login", login))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }
     
     public Usuario usuarioPorIdPessoa(Integer idPessoa) {
         
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Usuario) session.createCriteria(Usuario.class)
                    .add(Restrictions.eq("pessoa.idPessoa", idPessoa))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }
     
     public Boolean verificarUsuarioPorLogin (String login){
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;
        
        try {
            Criteria crit = session.createCriteria(Usuario.class)
                    .setProjection(Projections.count("login"))
                    .add(Restrictions.eq("login", login));
            
           transaction.commit();
           
           resultado = (Long) crit.uniqueResult();
           
           session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao verificar se Login Pessoa já existe, Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
}
