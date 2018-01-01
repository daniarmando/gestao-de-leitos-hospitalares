/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class LogDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public void salvar(Log log) {
        try {
            this.session.save(log);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar log. Erro: " + e.getMessage());
        }
    }

    public Log ultimoPorObjeto(String objeto) {

        return (Log) this.session.createCriteria(Log.class)
                .add(Restrictions.eq("objeto", objeto))
                .addOrder(Order.desc("dataHora"))
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<Log> porIdObjeto(String objeto, Integer idObjeto) {

        return (List<Log>) this.session.createCriteria(Log.class)
                .add(Restrictions.eq("objeto", objeto))
                .add(Restrictions.eq("idObjeto", idObjeto))
                .addOrder(Order.asc("idObjeto"))
                .addOrder(Order.desc("dataHora"))
                .list();
    }
}
