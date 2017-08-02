package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.TerapeutaOcupacional;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel
 */
public class TerapeutaOcupacionalDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public void salvar(TerapeutaOcupacional terapeutaOcupacional) throws DAOException {

        try {
            this.session.saveOrUpdate(terapeutaOcupacional);
        } catch (Exception e) {
            System.out.println("Problemas ao cadastrar Terapeuta Ocupacional. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Terapeuta Ocupacional.");
        }
    }

    public List<TerapeutaOcupacional> listar() {

        return (List<TerapeutaOcupacional>) this.session.createCriteria(TerapeutaOcupacional.class)
                .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                .list();
    }

}
