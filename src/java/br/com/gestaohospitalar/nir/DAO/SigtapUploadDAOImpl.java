/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.sigtap.RL_PROCEDIMENTO_CID;
import br.com.gestaohospitalar.nir.model.sigtap.RL_PROCEDIMENTO_LEITO;
import br.com.gestaohospitalar.nir.model.SigtapUploadLog;
import br.com.gestaohospitalar.nir.model.sigtap.TB_CID;
import br.com.gestaohospitalar.nir.model.sigtap.TB_FINANCIAMENTO;
import br.com.gestaohospitalar.nir.model.sigtap.TB_MODALIDADE;
import br.com.gestaohospitalar.nir.model.sigtap.TB_PROCEDIMENTO;
import br.com.gestaohospitalar.nir.model.sigtap.TB_RUBRICA;
import br.com.gestaohospitalar.nir.model.sigtap.TB_TIPO_LEITO;
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
public class SigtapUploadDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");

    //Busca a última chaveMesAno
    private String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();

    public TB_TIPO_LEITO tb_tipo_leitoPorCodigo(String co_tipo_leito) {
        return (TB_TIPO_LEITO) this.session.get(TB_TIPO_LEITO.class, co_tipo_leito);
    }

    public void salvarTB_TIPO_LEITO(List<TB_TIPO_LEITO> tb_tipos_leitos) throws DAOException {

        try {

            for (TB_TIPO_LEITO tb_tipo_leito : tb_tipos_leitos) {
                this.session.save(tb_tipo_leito);
                this.session.flush();
                this.session.clear();
            }

        } catch (Exception e) {
            System.out.println("Problemas ao salvar tabela Tipo Leito (Sigtap). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar tabela Tipo Leito (Sigtap).");
        }
    }

    public void salvarTB_RUBRICA(List<TB_RUBRICA> tb_rubricas) throws DAOException {

        try {

            for (TB_RUBRICA tb_rubrica : tb_rubricas) {
                this.session.save(tb_rubrica);
                this.session.flush();
                this.session.clear();
            }

        } catch (Exception e) {
            System.out.println("Problemas ao salvar tabela Rubrica (Sigtap). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar tabela Rubrica (Sigtap).");
        }
    }

    public void salvarTB_FINANCIAMENTO(List<TB_FINANCIAMENTO> tb_financiamentos) throws DAOException {

        try {

            for (TB_FINANCIAMENTO tb_financiamento : tb_financiamentos) {
                this.session.save(tb_financiamento);
                this.session.flush();
                this.session.clear();
            }

        } catch (Exception e) {
            System.out.println("Problemas ao salvar tabela Financiamento (Sigtap). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar tabela Financiamento (Sigtap).");
        }
    }

    public void salvarTB_CID(List<TB_CID> tb_cids) throws DAOException {

        try {

            for (TB_CID tb_cid : tb_cids) {
                this.session.save(tb_cid);
                this.session.flush();
                this.session.clear();
            }

        } catch (Exception e) {
            System.out.println("Problemas ao salvar tabela CID (Sigtap). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar tabela CID (Sigtap).");
        }
    }

    public void salvarTB_MODALIDADE(List<TB_MODALIDADE> tb_modalidades) throws DAOException {

        try {

            for (TB_MODALIDADE tb_modalidade : tb_modalidades) {
                this.session.save(tb_modalidade);
                this.session.flush();
                this.session.clear();
            }

        } catch (Exception e) {
            System.out.println("Problemas ao salvar tabela Modalidade (Sigtap). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar tabela Modalidade (Sigtap).");
        }
    }

    public void salvarTB_PROCEDIMENTO(List<TB_PROCEDIMENTO> tb_procedimentos) throws DAOException {

        try {

            for (TB_PROCEDIMENTO tb_procedimento : tb_procedimentos) {
                this.session.save(tb_procedimento);
                this.session.flush();
                this.session.clear();
            }

        } catch (Exception e) {
            System.out.println("Problemas ao salvar tabela Procedimento (Sigtap). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar tabela Procedimento (Sigtap).");
        }
    }

    public void salvarRL_PROCEDIMENTO_LEITO(List<RL_PROCEDIMENTO_LEITO> rl_procedimentos_leitos) throws DAOException {

        try {

            for (RL_PROCEDIMENTO_LEITO rl_procedimento_leito : rl_procedimentos_leitos) {
                this.session.save(rl_procedimento_leito);
                this.session.flush();
                this.session.clear();
            }

        } catch (Exception e) {
            System.out.println("Problemas ao salvar tabela Relação Procedimento/Leito (Sigtap). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar tabela Relação Procedimento/Leito (Sigtap).");
        }
    }

    public void salvarRL_PROCEDIMENTO_CID(List<RL_PROCEDIMENTO_CID> rl_procedimentos_cids) throws DAOException {

        try {

            for (RL_PROCEDIMENTO_CID rl_procedimento_cid : rl_procedimentos_cids) {
                this.session.save(rl_procedimento_cid);
                this.session.flush();
                this.session.clear();
            }

        } catch (Exception e) {
            System.out.println("Problemas ao salvar tabela Relação Procedimento/CID (Sigtap). Erro: " + e.getMessage());
            throw new DAOException("Problemas ao salvar tabela Relação Procedimento/CID (Sigtap).");
        }
    }

    public List<TB_TIPO_LEITO> listar() {

        if (this.chaveMesAno == null) {
            this.chaveMesAno = ConverterDataHora.ultimaChaveMesAno();
        }
        return (List<TB_TIPO_LEITO>) this.session.createCriteria(TB_TIPO_LEITO.class)
                .add(Restrictions.eq("chaveMesAno", this.chaveMesAno))
                .list();
    }

    public List<TB_PROCEDIMENTO> listarTB_procedimento() {

        return (List<TB_PROCEDIMENTO>) this.session.createCriteria(TB_PROCEDIMENTO.class)
                .add(Restrictions.eq("chaveMesAno", this.chaveMesAno))
                .list();
    }

    public List<TB_CID> listarCidPorProcedimento(String paramPesquisa, String co_procedimento) {

        //Montando comando SQL
        String sql = "SELECT c.* FROM tb_cid c, rl_procedimento_cid pc "
                + "WHERE (c.CO_CID like :paramPesquisa OR c.NO_CID like :paramPesquisa) "
                + "AND c.CO_CID = pc.CO_CID "
                + "AND pc.CO_PROCEDIMENTO = :co_procedimento "
                + "AND c.chaveMesAno = pc.chaveMesAno "
                + "AND c.chaveMesAno = :chaveMesAno";
        //Executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
        return (List<TB_CID>) this.session.createSQLQuery(sql)
                .addEntity(TB_CID.class)
                .setParameter("paramPesquisa", "%" + paramPesquisa + "%")
                .setParameter("co_procedimento", co_procedimento)
                .setParameter("chaveMesAno", this.chaveMesAno)
                .list();
    }

    public List<TB_PROCEDIMENTO> listarProcedimentosPorTipoLeito(String paramPesquisa, String co_tipo_leito) {

        //Montando comando SQL
        String sql = "SELECT p.* FROM tb_procedimento p, rl_procedimento_leito pl "
                + "WHERE (p.CO_PROCEDIMENTO like :paramPesquisa OR p.NO_PROCEDIMENTO like :paramPesquisa) "
                + "AND p.CO_PROCEDIMENTO = pl.CO_PROCEDIMENTO "
                + "AND pl.CO_TIPO_LEITO = :co_tipo_leito "
                + "AND p.chaveMesAno = pl.chaveMesAno "
                + "AND p.chaveMesAno = :chaveMesAno";
        //Executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
        return (List<TB_PROCEDIMENTO>) this.session.createSQLQuery(sql)
                .addEntity(TB_PROCEDIMENTO.class)
                .setParameter("paramPesquisa", "%" + paramPesquisa + "%")
                .setParameter("co_tipo_leito", "%" + co_tipo_leito + "%")
                .setParameter("chaveMesAno", this.chaveMesAno)
                .list();
    }

    public List<TB_PROCEDIMENTO> listarTodosProcedimentos(String paramPesquisa) {

        //Montando comando SQL
        String sql = "SELECT * FROM tb_procedimento "
                + "WHERE (CO_PROCEDIMENTO like :paramPesquisa OR NO_PROCEDIMENTO like :paramPesquisa) "
                + "AND chaveMesAno = :chaveMesAno";
        //Executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
        return (List<TB_PROCEDIMENTO>) this.session.createSQLQuery(sql)
                .addEntity(TB_PROCEDIMENTO.class)
                .setParameter("paramPesquisa", "%" + paramPesquisa + "%")
                .setParameter("chaveMesAno", this.chaveMesAno)
                .list();
    }

    public boolean temProcedimento(String CO_PROCEDIMENTO, String CO_TIPO_LEITO) {

        Long resultado = (Long) this.session.createCriteria(RL_PROCEDIMENTO_LEITO.class)
                .setProjection(Projections.count("tb_procedimento.CO_PROCEDIMENTO"))
                .add(Restrictions.eq("tb_procedimento.CO_PROCEDIMENTO", CO_PROCEDIMENTO))
                .add(Restrictions.eq("tb_tipo_leito.CO_TIPO_LEITO", CO_TIPO_LEITO))
                .add(Restrictions.eq("chaveMesAno", this.chaveMesAno))
                .uniqueResult();

        return resultado > 0;
    }

    public void excluir() throws DAOException {

        try {

            String hql0 = "DELETE FROM RL_PROCEDIMENTO_LEITO WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql1 = "DELETE FROM RL_PROCEDIMENTO_CID WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql2 = "DELETE FROM TB_PROCEDIMENTO WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql3 = "DELETE FROM TB_MODALIDADE WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql4 = "DELETE FROM TB_CID WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql5 = "DELETE FROM TB_FINANCIAMENTO WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql6 = "DELETE FROM TB_RUBRICA WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql7 = "DELETE FROM TB_TIPO_LEITO WHERE chaveMesAno = '" + this.chaveMesAno + "'";

            this.session.createQuery(hql0).executeUpdate();
            this.session.createQuery(hql1).executeUpdate();
            this.session.createQuery(hql2).executeUpdate();
            this.session.createQuery(hql3).executeUpdate();
            this.session.createQuery(hql4).executeUpdate();
            this.session.createQuery(hql5).executeUpdate();
            this.session.createQuery(hql6).executeUpdate();
            this.session.createQuery(hql7).executeUpdate();

        } catch (Exception e) {
            System.out.println("Problemas ao excluir Tabelas Sigtap. Erro: " + e.getMessage());
            throw new DAOException("Problemas ao excluir Tabelas Sigtap.");
        }
    }

    public boolean isTabelaImportada(String chaveMesAnoGerada) {
        Criteria crit = this.session.createCriteria(SigtapUploadLog.class)
                .setProjection(Projections.count("chaveMesAno"))
                .add(Restrictions.eq("chaveMesAno", chaveMesAnoGerada));

        return (Long) crit.uniqueResult() > 0;
    }

}
