/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Parametros;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class ParametrosDAOImpl {
    
    public Parametros parametrosPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Parametros) session.createCriteria(Parametros.class)
                    .add(Restrictions.idEq(id))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }
    
    public Parametros parametrosPorIdHospital(Integer idHospital) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (Parametros) session.createCriteria(Parametros.class)
                    .add(Restrictions.eq("hospital.idHospital", idHospital))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }
    
    public void salvar(Parametros parametros) {
        
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(parametros);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Parâmetro. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
