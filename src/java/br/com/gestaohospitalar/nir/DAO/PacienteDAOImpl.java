/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Paciente;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class PacienteDAOImpl {
    
    private final Session session = (Session) FacesUtil.getRequestAttribute("session");
    
     public Paciente pacientePorId(Integer id) {
        return (Paciente) this.session.get(Paciente.class, id);
    }

    public void salvar(Paciente paciente) throws DAOException {
        
        try{
           this.session.saveOrUpdate(paciente);           
        }catch (Exception e) {
            System.out.println("Problemas ao salvar paciente. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Paciente.");
        }
    }

    public List<Paciente> listar() {

            return (List<Paciente>) this.session.createCriteria(Paciente.class)
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .list();
    }
    
    public List<Paciente> listarPorNomeOuCodigoSusOuCPF(String paramPesquisa) {
 
            return (List<Paciente>) this.session.createCriteria(Paciente.class)
                    .add(Restrictions.disjunction()
                    .add(Restrictions.like("nomePessoa", paramPesquisa + "%"))
                    .add(Restrictions.like("codigoSusPaciente", paramPesquisa + "%"))
                    .add(Restrictions.like("cpfPessoa", paramPesquisa + "%"))
                    )
                    .add(Restrictions.eq("statusPessoa", Status.ATIVO.get()))
                    .add(Restrictions.eq("statusPaciente", Status.ATIVO.get()))
                    .list();
    }
    
    public boolean estaEmInternacaoAberta(Paciente paciente) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("paciente", paciente))
                    .add(Restrictions.eq("statusInternacao", Status.ABERTA.get()));

            resultado = (Long) crit.uniqueResult();
            
        } catch (Exception e) {
            System.out.println("Problemas ao verificar se o Paciente está em Internação aberta. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }
    
}
