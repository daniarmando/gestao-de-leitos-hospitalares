/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class LogDAOImpl {

    public void salvar(Log log) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(log);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Log. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Log ultimoLogPorObjeto(String objeto) {
        Log ultimoLog = new Log();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Criteria crit = session.createCriteria(Log.class)
                    .add(Restrictions.eq("objeto", objeto))
                    .addOrder(Order.desc("dataHora"))
                    .setMaxResults(1);

            ultimoLog = (Log) crit.uniqueResult();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao buscar último Log por objeto. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return ultimoLog;
    }
    
    public List<Log> listarPorIdObjeto(String objeto, Integer idObjeto) {
        List<Log> listarLog = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            listarLog = session.createCriteria(Log.class)
                    .add(Restrictions.eq("objeto", objeto))
                    .add(Restrictions.eq("idObjeto", idObjeto))
                    .addOrder(Order.asc("idObjeto"))
                    .addOrder(Order.desc("dataHora"))
                    .list();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar logs por objeto e idObjeto. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarLog;
    }

}
