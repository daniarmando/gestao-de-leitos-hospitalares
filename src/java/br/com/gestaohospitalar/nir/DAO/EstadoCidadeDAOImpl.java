/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class EstadoCidadeDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    public Estado estadoPorId(Integer id) {
        return (Estado) this.session.get(Estado.class, id);
    }

    public Cidade cidadePorId(Integer id) {
        return (Cidade) this.session.get(Cidade.class, id);
    }

    public List<Estado> todosEstados() {
        return (List<Estado>) this.session.createCriteria(Estado.class).list();
    }

    public List<Cidade> cidadesPorEstado(Estado estado) {

        return (List<Cidade>) this.session.createCriteria(Cidade.class).add(Restrictions.eq("estado", estado)).list();
    }
}
