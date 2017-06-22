/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Enfermeiro;
import br.com.gestaohospitalar.nir.model.GerenteEnfermagem;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.util.List;
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
public class GerenteEnfermagemDAOImpl {
    
    public GerenteEnfermagem gerenteEnfermagemPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (GerenteEnfermagem) session.get(GerenteEnfermagem.class, id);

    }
    
    public void salvar(GerenteEnfermagem gerenteEnfermagem) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(gerenteEnfermagem);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Gerente Enfermagem. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<GerenteEnfermagem> listar() {
        List <GerenteEnfermagem> listarGerenteEnfermagem = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarGerenteEnfermagem = session.createCriteria(GerenteEnfermagem.class)
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Gerente Enfermagem. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarGerenteEnfermagem;
    }
    
    public GerenteEnfermagem listarPorId(Integer id) {
         
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (GerenteEnfermagem) session.createCriteria(GerenteEnfermagem.class)
                    .add(Restrictions.idEq(id))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }
    
    public Boolean verificarSePossuiEnfermeiro(GerenteEnfermagem gerenteEnfermagem) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Enfermeiro.class)
                    .setProjection(Projections.count("idPessoa"))
                    .add(Restrictions.eq("gerenteEnfermagem", gerenteEnfermagem))
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se o Gerente possuí Enfermeiro(s). Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
}
