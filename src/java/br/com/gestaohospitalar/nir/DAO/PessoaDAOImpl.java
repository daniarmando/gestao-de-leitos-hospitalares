/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Pessoa;
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
public class PessoaDAOImpl {
    
    public Boolean verificarPessoaPorCPF (String cpf){
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;
        
        try {
            Criteria crit = session.createCriteria(Pessoa.class)
                    .setProjection(Projections.count("cpfPessoa"))
                    .add(Restrictions.eq("cpfPessoa", cpf));
            
           transaction.commit();
           
           resultado = (Long) crit.uniqueResult();
           
           session.close();
        }catch (HibernateException e) {
            System.out.println("Problemas ao verificar se CPF Pessoa já existe, Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
    
}
