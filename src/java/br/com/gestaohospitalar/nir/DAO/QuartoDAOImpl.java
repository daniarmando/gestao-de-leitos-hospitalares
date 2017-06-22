/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.Quarto;
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
public class QuartoDAOImpl {
    
    public Quarto quartoPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (Quarto) session.get(Quarto.class, id);

    }
    
    public void salvar(Quarto quarto) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(quarto);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Quarto. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Quarto> listar() {
        List <Quarto> listarQuarto = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarQuarto = session.createCriteria(Quarto.class)
                    .add(Restrictions.eq("statusQuarto", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Quarto. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarQuarto;
    }
    
    public List<Quarto> listarPorIdSetor(Integer idSetor) {
        List <Quarto> listarQuarto = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarQuarto = session.createCriteria(Quarto.class)
                    .add(Restrictions.eq("setor.idSetor", idSetor))
                    .add(Restrictions.eq("statusQuarto", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Quarto por Setor. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarQuarto;
    }
    
    public Boolean verificarSePossuiLeito(Quarto quarto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Leito.class)
                    .setProjection(Projections.count("idLeito"))
                    .add(Restrictions.eq("quarto", quarto))
                    .add(Restrictions.ne("statusLeito", Status.INDISPONIVEL.get()));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se o Quarto possuí Leitos. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
}
