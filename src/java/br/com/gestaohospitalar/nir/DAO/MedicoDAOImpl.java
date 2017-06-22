/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Medico;
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
public class MedicoDAOImpl {
    
     public Medico medicoPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (Medico) session.get(Medico.class, id);

    }
    
    public void salvar(Medico medico) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(medico);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Médico. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Medico> listar() {
        List <Medico> listarMedicos = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
             listarMedicos = session.createCriteria(Medico.class)
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Médicos. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarMedicos;
    }
    
    public Boolean verificarSePossuiInternacaoAberta(Medico medico) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("medico", medico))
                    .add(Restrictions.eq("statusInternacao", Status.ABERTA.get()));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se o Médico está em Internação aberta. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
    
}
