/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Daniel
 */
public class InternacaoDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    //Busca a última chaveMesAno
    private final String chaveMesAno = NIRDataUtil.ultimaChaveMesAno();
    private Boolean isAltaQualificada = false;
    private Boolean isAlta = false;

    public Internacao porId(Integer id) {

        return (Internacao) this.session.createCriteria(Internacao.class)
                .add(Restrictions.idEq(id))
                .uniqueResult();

    }

    public void salvar(Internacao internacao) throws DAOException {

        try {
            this.session.saveOrUpdate(internacao);
        } catch (Exception e) {
            System.out.println("Problemas ao salvar Internação. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar Internação.");
        }
    }

    public List<Internacao> todas() {

        //Consulta as internações distinguindo pelo id da internação
        return (List<Internacao>) this.session.createCriteria(Internacao.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }

    public List<Internacao> paraHigienizacao() {
        //passando status da internação e da alta como critério para consulta
        return porStatus(Status.HIGIENIZACAO.get(), Status.HIGIENIZACAO.get());
    }

    public List<Internacao> paraAltaQualificada() {
        isAltaQualificada = true;
        //passando status da internação e da alta como critério para consulta
        return porStatus(Status.ABERTA.get(), Status.INTERNACAO.get());
    }

    public List<Internacao> paraAlta() {
        isAlta = true;
        //passando status da internação e da alta como critério para consulta
        return porStatus(Status.ABERTA.get(), Status.INTERNACAO.get());
    }

    public List<Internacao> porStatus(String statusInternacao, String statusLeito) {

        Criteria crit = this.session.createCriteria(Internacao.class)
                //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                .createAlias("procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                .createAlias("procedimento.tb_financiamento", "ptf", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptf.chaveMesAno", chaveMesAno))
                .createAlias("procedimento.tb_rubrica", "ptr", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptr.chaveMesAno", chaveMesAno))
                .createAlias("cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                .createAlias("leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                //Passando regras do status
                .createAlias("leito", "l", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("l.statusLeito", statusLeito))
                .add(Restrictions.eq("statusInternacao", statusInternacao));

        //se estiver listando para cadastrar alta qualificada
        if (isAltaQualificada) {
            crit.add(Restrictions.isNull("dataPrevisaoAlta"));
            crit.add(Restrictions.isNull("dataAlta"));
            isAltaQualificada = false;
        }

        //se estiver listando para cadastrar alta
        if (isAlta) {
            crit.add(Restrictions.isNull("dataAlta"));
            isAlta = false;
        }

        return (List<Internacao>) crit.list();

    }

    public List<Internacao> historicoInternacoesPorLeito(Integer idLeito) {

        return (List<Internacao>) this.session.createCriteria(Internacao.class)
                //Passando a regras para trazer informações das tabelas sigtap de acordo com a chaveMesAno
                .createAlias("procedimento", "p", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("p.chaveMesAno", chaveMesAno))
                .createAlias("procedimento.tb_financiamento", "ptf", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptf.chaveMesAno", chaveMesAno))
                .createAlias("procedimento.tb_rubrica", "ptr", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("ptr.chaveMesAno", chaveMesAno))
                .createAlias("cid", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("c.chaveMesAno", chaveMesAno))
                .createAlias("leito.tipo_leito", "tl", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("tl.chaveMesAno", chaveMesAno))
                //Passando regras para consulta 
                .add(Restrictions.eq("leito.idLeito", idLeito))
                .add(Restrictions.ne("statusInternacao", Status.CANCELADA.get()))
                .list();

    }

    public boolean houveSaida(Integer idInternacao) throws DAOException {
        Long resultado = null;

        try {
            Criteria crit = this.session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("idInternacao", idInternacao))
                    .add(Restrictions.isNotNull("dataSaidaLeito"));

            resultado = (Long) crit.uniqueResult();

        } catch (Exception e) {
            System.out.println("Problemas ao verificar foi registrado saída do paciente do leito. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao validar informações no banco de dados.");
        }
        return resultado > 0;
    }

    public boolean temTB_TIPO_LEITO(String chaveMesAnoGerada) {

        Long resultado = (Long) this.session.createCriteria(Internacao.class)
                .setProjection(Projections.count("idInternacao"))
                .add(Restrictions.eq("chaveMesAnoProcedimento", chaveMesAnoGerada))
                .add(Restrictions.eq("chaveMesAnoCID", chaveMesAnoGerada)).
                uniqueResult();

        return resultado > 0;

    }

}
