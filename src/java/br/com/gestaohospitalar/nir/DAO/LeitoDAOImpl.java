/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Daniel
 */
public class LeitoDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    //Busca a última chaveMesAno
    private final String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();

    public Leito porId(Integer id) {
        return (Leito) this.session.get(Leito.class, id);
    }

    public void salvar(Leito leito) throws DAOException {
        try {
            this.session.saveOrUpdate(leito);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Leito. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Leito.");
        }
    }

    public List<Leito> todosDisponiveis() {

        return (List<Leito>) this.session.createCriteria(Leito.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .createAlias("tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                .add(Restrictions.ne("statusLeito", Status.INDISPONIVEL.get()))
                .list();
    }

    public List<Leito> disponiveisParaInternacao() {

        return (List<Leito>) this.session.createCriteria(Leito.class)
                //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                .createAlias("internacao.procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                .createAlias("internacao.procedimento.tb_financiamento", "ptf", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptf.chaveMesAno", chaveMesAno))
                .createAlias("internacao.procedimento.tb_rubrica", "ptr", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptr.chaveMesAno", chaveMesAno))
                .createAlias("internacao.cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                .createAlias("tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                //Passando as regras do status
                .add(Restrictions.eq("statusLeito", Status.DISPONIVEL.get()))
                .list();
    }

    public List<Leito> disponiveisPorIdSetor(Integer idSetor) {
        
        return (List<Leito>) this.session.createCriteria(Leito.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .createAlias("quarto.setor", "s", JoinType.INNER_JOIN, Restrictions.eq("s.idSetor", idSetor))
                .createAlias("tipo_leito", "tl", JoinType.INNER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                .add(Restrictions.ne("statusLeito", Status.INDISPONIVEL.get()))
                .addOrder(Order.asc("quarto.idQuarto"))
                .addOrder(Order.asc("idLeito"))
                .list();
    }

    public List<Leito> disponiveisPorIdQuarto(Integer idQuarto) {

        return (List<Leito>) this.session.createCriteria(Leito.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .createAlias("tipo_leito", "tl", JoinType.INNER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                .add(Restrictions.eq("quarto.idQuarto", idQuarto))
                .addOrder(Order.asc("idLeito"))
                .list();
    }

    public List<Leito> emInternacaoPorIdMedico(Integer idMedico) {

        return (List<Leito>) this.session.createCriteria(Leito.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .createAlias("internacao.medico", "m", JoinType.INNER_JOIN, Restrictions.eq("m.idPessoa", idMedico))
                .createAlias("tipo_leito", "tl", JoinType.INNER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                .add(Restrictions.eq("statusLeito", Status.INTERNACAO.get()))
                .addOrder(Order.asc("quarto.idQuarto"))
                .addOrder(Order.asc("idLeito"))
                .list();
    }

    public boolean temInternacaoAberta(Leito leito) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("leito", leito))
                    .add(Restrictions.eq("statusInternacao", Status.ABERTA.get()));

            resultado = (Long) crit.uniqueResult();

        } catch (Exception e) {
            System.out.println("Problemas ao verificar se o Leito possuí Internação aberta. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }

    public boolean temNovaInternacao(Internacao internacao) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Leito.class)
                    .setProjection(Projections.count("idLeito"))
                    .add(Restrictions.eq("idLeito", internacao.getLeito().getIdLeito()))
                    .add(Restrictions.ne("internacao.idInternacao", internacao.getIdInternacao()));

            resultado = (Long) crit.uniqueResult();

        } catch (Exception e) {
            System.out.println("Problemas ao verificar se ja foi registrado nova Internaçao no Leito. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }

    public boolean temTB_TIPO_LEITO(String chaveMesAnoGerada) {

        Long resultado = (Long) this.session.createCriteria(Leito.class)
                .setProjection(Projections.count("idLeito"))
                .add(Restrictions.eq("chaveMesAnoTipoLeito", chaveMesAnoGerada))
                .uniqueResult();

        return resultado > 0;
    }
}
