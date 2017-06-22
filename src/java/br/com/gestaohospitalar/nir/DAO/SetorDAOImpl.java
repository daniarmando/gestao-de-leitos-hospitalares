/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;
import br.com.gestaohospitalar.nir.model.Quarto;
import br.com.gestaohospitalar.nir.model.Setor;
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
public class SetorDAOImpl {
    
    public Setor setorPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (Setor) session.get(Setor.class, id);

    }
    
    public void salvar(Setor setor) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(setor);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Setor. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Setor> listar() {
        List <Setor> listarSetor = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        
        try {
            listarSetor = session.createCriteria(Setor.class).
                    add(Restrictions.eq("statusSetor", Status.ATIVO.get()))
                    .list();
            transaction.commit();
            session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao listar Setor. Erro: " + e.getMessage());
            transaction.rollback();
        }
        
        return listarSetor;
    }
    
    public Boolean verificarSePossuiQuarto(Setor setor) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Quarto.class)
                    .setProjection(Projections.count("idQuarto"))
                    .add(Restrictions.eq("setor", setor))
                    .add(Restrictions.eq("statusQuarto", Status.ATIVO.get()));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se o setor possuí quarto. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
}
