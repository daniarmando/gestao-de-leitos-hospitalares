/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.SigtapUploadLog;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

/**
 *
 * @author Daniel
 */
public class SigtapUploadLogDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public void salvar(SigtapUploadLog sigtapUploadLog) {

        try {
            this.session.save(sigtapUploadLog);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Log do SigtapUpload. Erro: " + e.getMessage());
        }
    }

    public void excluir() {

        try {

            String hql0 = "DELETE FROM SigtapUploadLog WHERE chaveMesAno = '" + ultimaChaveMesAno() + "'";
            this.session.createQuery(hql0).executeUpdate();

        } catch (Exception e) {
            System.out.println("Problemas ao excluir Log do SigtapUpload. Erro: " + e.getMessage());
        }
    }

    public String ultimaChaveMesAno() {

        Criteria crit = this.session.createCriteria(SigtapUploadLog.class);
        //definindo regras para trazer apenas a coluna ChaveMesAno  
        ProjectionList projection = Projections.projectionList();
        projection.add(Projections.property("chaveMesAno"));
        crit.setProjection(projection);
        crit.addOrder(Order.desc("dataImportacao"));
        crit.setMaxResults(1);

        return (String) crit.uniqueResult();

    }

}
