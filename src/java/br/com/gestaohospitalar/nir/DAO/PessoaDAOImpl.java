/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Pessoa;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class PessoaDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public boolean isCPFCadastrado(String cpf) {

        Long resultado = (Long) this.session.createCriteria(Pessoa.class)
                .setProjection(Projections.count("cpfPessoa"))
                .add(Restrictions.eq("cpfPessoa", cpf))
                .uniqueResult();

        return resultado > 0;

    }
}
