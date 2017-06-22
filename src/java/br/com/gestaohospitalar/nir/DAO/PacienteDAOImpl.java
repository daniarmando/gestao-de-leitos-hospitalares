/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Paciente;
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
public class PacienteDAOImpl {
    
     public Paciente pacientePorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (Paciente) session.get(Paciente.class, id);

    }

    public void salvar(Paciente paciente) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(paciente);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar paciente. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Paciente> listar() {
        List<Paciente> listarPacientes = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarPacientes = session.createCriteria(Paciente.class)
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Pacientes. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarPacientes;
    }
    
    public List<Paciente> listarPacientePorNomeOuCodigoSusOuCPF(String paramPesquisa) {
        List<Paciente> listarPacientes = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarPacientes = session.createCriteria(Paciente.class)
                    .add(Restrictions.disjunction()
                    .add(Restrictions.like("nomePessoa", paramPesquisa + "%"))
                    .add(Restrictions.like("codigoSusPaciente", paramPesquisa + "%"))
                    .add(Restrictions.like("cpfPessoa", paramPesquisa + "%"))
                    )
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .add(Restrictions.eq("statusPaciente", Status.ATIVO.get()))
                    .list();
            
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Pacientes. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarPacientes;
    }
    
    public Boolean verificarSePossuiInternacaoAberta(Paciente paciente) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("paciente", paciente))
                    .add(Restrictions.eq("statusInternacao", Status.ABERTA.get()));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se o Paciente está em Internação aberta. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
    
}
