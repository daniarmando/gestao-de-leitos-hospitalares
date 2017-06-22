/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.ConfiguracaoKanban;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class ConfiguracaoKanbanDAOImpl {
    
    public void salvar(ConfiguracaoKanban configuracaoKanban) {
        Session session = null;
        
        try{
           session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           session.saveOrUpdate(configuracaoKanban);
           session.getTransaction().commit();
        }catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar a Configuração do Kanban. Erro: " + e.getMessage());
            session.getTransaction().rollback();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public ConfiguracaoKanban configuracaoKanbanPorId(Integer id) {
         
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            return (ConfiguracaoKanban) session.createCriteria(ConfiguracaoKanban.class)
                    .add(Restrictions.idEq(id))
                    .uniqueResult();
            
        }finally {
            session.close();
        }        
    }

    
}
