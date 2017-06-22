/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
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
public class EstadoCidadeDAOImpl {
    
    public Estado estadoPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (Estado) session.get(Estado.class, id);

    }

    public Cidade cidadePorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (Cidade) session.get(Cidade.class, id);

    }

    public List<Estado> listarEstados() {
        List<Estado> listarEstados = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            listarEstados = session.createCriteria(Estado.class).list();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Estados. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarEstados;
    }

    public List<Cidade> listarCidades(Estado estado) {
        List<Cidade> listarCidades = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            listarCidades = session.createCriteria(Cidade.class).add(Restrictions.eq("estado", estado)).list();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Cidades. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarCidades;
    }
}
