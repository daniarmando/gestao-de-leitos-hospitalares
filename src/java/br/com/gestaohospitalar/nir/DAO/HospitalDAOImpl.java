/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;
import br.com.gestaohospitalar.nir.model.Hospital;
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
public class HospitalDAOImpl {
    
    public Hospital hospitalPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (Hospital) session.get(Hospital.class, id);

    }
    
    public void salvar(Hospital hospital) {
        
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(hospital);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar hospital. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Hospital> listar() {
        List <Hospital> listarHospital = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarHospital = session.createCriteria(Hospital.class)
                    .add(Restrictions.eq("statusHospital", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar hospital. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarHospital;
    }
    
}
