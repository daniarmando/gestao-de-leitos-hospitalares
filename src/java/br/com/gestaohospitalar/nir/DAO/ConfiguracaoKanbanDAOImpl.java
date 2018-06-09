/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.ConfiguracaoKanban;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class ConfiguracaoKanbanDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public ConfiguracaoKanban porId(Integer id) {
        return (ConfiguracaoKanban) this.session.get(ConfiguracaoKanban.class, id);
    }

    public ConfiguracaoKanban porIdHospital(Integer idHospital) {
        return (ConfiguracaoKanban) this.session.createCriteria(ConfiguracaoKanban.class)
                .add(Restrictions.eq("hospital.idHospital", idHospital))
                .uniqueResult();
    }

    public void salvar(ConfiguracaoKanban configuracaoKanban) throws DAOException {

        try {
            this.session.saveOrUpdate(configuracaoKanban);
        } catch (Exception e) {
            System.out.println("Problemas ao salva a Configuração Kanban. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Configuração Kanban.");
        }

    }

}
