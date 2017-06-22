/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.sigtap.RL_PROCEDIMENTO_CID;
import br.com.gestaohospitalar.nir.model.sigtap.RL_PROCEDIMENTO_LEITO;
import br.com.gestaohospitalar.nir.model.SigtapUploadLog;
import br.com.gestaohospitalar.nir.model.sigtap.TB_CID;
import br.com.gestaohospitalar.nir.model.sigtap.TB_FINANCIAMENTO;
import br.com.gestaohospitalar.nir.model.sigtap.TB_MODALIDADE;
import br.com.gestaohospitalar.nir.model.sigtap.TB_PROCEDIMENTO;
import br.com.gestaohospitalar.nir.model.sigtap.TB_RUBRICA;
import br.com.gestaohospitalar.nir.model.sigtap.TB_TIPO_LEITO;
import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel
 */
public class SigtapUploadDAOImpl {

    //Busca a última chaveMesAno
    private String chaveMesAno = ConverterDataHora.ultimaChaveMesAno();

    public TB_TIPO_LEITO tb_tipo_leitoPorCodigo(String co_tipo_leito) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        return (TB_TIPO_LEITO) session.get(TB_TIPO_LEITO.class, co_tipo_leito);

    }

    public void salvarTB_TIPO_LEITO(List<TB_TIPO_LEITO> tb_tipos_leitos) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (TB_TIPO_LEITO tb_tipo_leito : tb_tipos_leitos) {
                session.save(tb_tipo_leito);
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Tipo leito (Sigtap). Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void salvarTB_RUBRICA(List<TB_RUBRICA> tb_rubricas) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (TB_RUBRICA tb_rubrica : tb_rubricas) {
                session.save(tb_rubrica);
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Rubrica (Sigtap). Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void salvarTB_FINANCIAMENTO(List<TB_FINANCIAMENTO> tb_financiamentos) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (TB_FINANCIAMENTO tb_financiamento : tb_financiamentos) {
                session.save(tb_financiamento);
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Financiamento (Sigtap). Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void salvarTB_CID(List<TB_CID> tb_cids) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (TB_CID tb_cid : tb_cids) {
                session.save(tb_cid);
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar CID (Sigtap). Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void salvarTB_MODALIDADE(List<TB_MODALIDADE> tb_modalidades) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (TB_MODALIDADE tb_modalidade : tb_modalidades) {
                session.save(tb_modalidade);
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Modalidade (Sigtap). Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void salvarTB_PROCEDIMENTO(List<TB_PROCEDIMENTO> tb_procedimentos) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (TB_PROCEDIMENTO tb_procedimento : tb_procedimentos) {
                session.save(tb_procedimento);
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Modalidade (Sigtap). Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void salvarRL_PROCEDIMENTO_LEITO(List<RL_PROCEDIMENTO_LEITO> rl_procedimentos_leitos) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (RL_PROCEDIMENTO_LEITO rl_procedimento_leito : rl_procedimentos_leitos) {
                session.save(rl_procedimento_leito);
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Relação Procedimento/Leito (Sigtap). Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void salvarRL_PROCEDIMENTO_CID(List<RL_PROCEDIMENTO_CID> rl_procedimentos_cids) {

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (RL_PROCEDIMENTO_CID rl_procedimento_cid : rl_procedimentos_cids) {
                session.save(rl_procedimento_cid);
                session.flush();
                session.clear();
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao cadastrar Relação Procedimento/CID (Sigtap). Erro: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<TB_TIPO_LEITO> listar() {

        if (this.chaveMesAno == null) {
            this.chaveMesAno = ConverterDataHora.ultimaChaveMesAno();
        }

        List<TB_TIPO_LEITO> listarTB_tipo_leito = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            listarTB_tipo_leito = session.createCriteria(TB_TIPO_LEITO.class)
                    .add(Restrictions.eq("chaveMesAno", this.chaveMesAno))
                    .list();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Tipo Leito. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarTB_tipo_leito;
    }

    public List<TB_PROCEDIMENTO> listarTB_procedimento() {
        List<TB_PROCEDIMENTO> listarTB_procedimento = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            listarTB_procedimento = session.createCriteria(TB_PROCEDIMENTO.class)
                    .add(Restrictions.eq("chaveMesAno", this.chaveMesAno))
                    .list();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Procedimentos. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarTB_procedimento;
    }

    public List<TB_CID> listarCidPorProcedimento(String paramPesquisa, String co_procedimento) {
        List<TB_CID> listarTB_cid = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            //Montando comando SQL
            String sql = "SELECT c.* FROM tb_cid c, rl_procedimento_cid pc "
                    + "WHERE (c.CO_CID like :paramPesquisa OR c.NO_CID like :paramPesquisa) "
                    + "AND c.CO_CID = pc.CO_CID "
                    + "AND pc.CO_PROCEDIMENTO = :co_procedimento "
                    + "AND c.chaveMesAno = pc.chaveMesAno "
                    + "AND c.chaveMesAno = :chaveMesAno";
            //Executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
            listarTB_cid = session.createSQLQuery(sql)
                    .addEntity(TB_CID.class)
                    .setParameter("paramPesquisa", "%" + paramPesquisa + "%")
                    .setParameter("co_procedimento", co_procedimento)
                    .setParameter("chaveMesAno", this.chaveMesAno)
                    .list();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar CIDs. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarTB_cid;
    }

    public List<TB_PROCEDIMENTO> listarProcedimentosPorTipoLeito(String paramPesquisa, String co_tipo_leito) {
        List<TB_PROCEDIMENTO> listarTB_procedimento = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            //Montando comando SQL
            String sql = "SELECT p.* FROM tb_procedimento p, rl_procedimento_leito pl "
                    + "WHERE (p.CO_PROCEDIMENTO like :paramPesquisa OR p.NO_PROCEDIMENTO like :paramPesquisa) "
                    + "AND p.CO_PROCEDIMENTO = pl.CO_PROCEDIMENTO "
                    + "AND pl.CO_TIPO_LEITO = :co_tipo_leito "
                    + "AND p.chaveMesAno = pl.chaveMesAno "
                    + "AND p.chaveMesAno = :chaveMesAno";
            //Executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
            listarTB_procedimento = session.createSQLQuery(sql)
                    .addEntity(TB_PROCEDIMENTO.class)
                    .setParameter("paramPesquisa", "%" + paramPesquisa + "%")
                    .setParameter("co_tipo_leito", "%" + co_tipo_leito + "%")
                    .setParameter("chaveMesAno", this.chaveMesAno)
                    .list();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Procedimentos. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarTB_procedimento;
    }

    public List<TB_PROCEDIMENTO> listarTodosProcedimentos(String paramPesquisa) {
        List<TB_PROCEDIMENTO> listarTB_procedimento = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            //Montando comando SQL
            String sql = "SELECT * FROM tb_procedimento "
                    + "WHERE (CO_PROCEDIMENTO like :paramPesquisa OR NO_PROCEDIMENTO like :paramPesquisa) "
                    + "AND chaveMesAno = :chaveMesAno";
            //Executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
            listarTB_procedimento = session.createSQLQuery(sql)
                    .addEntity(TB_PROCEDIMENTO.class)
                    .setParameter("paramPesquisa", "%" + paramPesquisa + "%")
                    .setParameter("chaveMesAno", this.chaveMesAno)
                    .list();

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao listar Procedimentos. Erro: " + e.getMessage());
            transaction.rollback();
        }

        return listarTB_procedimento;
    }

    public Boolean verificarSeExisteProcedimento(String CO_PROCEDIMENTO, String CO_TIPO_LEITO) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(RL_PROCEDIMENTO_LEITO.class)
                    .setProjection(Projections.count("tb_procedimento.CO_PROCEDIMENTO"))
                    .add(Restrictions.eq("tb_procedimento.CO_PROCEDIMENTO", CO_PROCEDIMENTO))
                    .add(Restrictions.eq("tb_tipo_leito.CO_TIPO_LEITO", CO_TIPO_LEITO))
                    .add(Restrictions.eq("chaveMesAno", this.chaveMesAno));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se existe tipo de leito para o procedimento. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }

    public void excluir() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            String hql0 = "DELETE FROM RL_PROCEDIMENTO_LEITO WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql1 = "DELETE FROM RL_PROCEDIMENTO_CID WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql2 = "DELETE FROM TB_PROCEDIMENTO WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql3 = "DELETE FROM TB_MODALIDADE WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql4 = "DELETE FROM TB_CID WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql5 = "DELETE FROM TB_FINANCIAMENTO WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql6 = "DELETE FROM TB_RUBRICA WHERE chaveMesAno = '" + this.chaveMesAno + "'";
            String hql7 = "DELETE FROM TB_TIPO_LEITO WHERE chaveMesAno = '" + this.chaveMesAno + "'";

            session.createQuery(hql0).executeUpdate();
            session.createQuery(hql1).executeUpdate();
            session.createQuery(hql2).executeUpdate();
            session.createQuery(hql3).executeUpdate();
            session.createQuery(hql4).executeUpdate();
            session.createQuery(hql5).executeUpdate();
            session.createQuery(hql6).executeUpdate();
            session.createQuery(hql7).executeUpdate();

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.out.println("Problemas ao Excluir Tabela Sigtap. Erro: " + e.getMessage());
            session.getTransaction().rollback();

        }
    }

    public Boolean verificarTabela(String chaveMesAnoGerada) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(SigtapUploadLog.class)
                    .setProjection(Projections.count("chaveMesAno"))
                    .add(Restrictions.eq("chaveMesAno", chaveMesAnoGerada));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se a Tabela já foi importada para o MesAno: " + this.chaveMesAno + " Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }

    public Boolean verificarSePossuiLeito(String chaveMesAnoGerada) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Leito.class)
                    .setProjection(Projections.count("idLeito"))
                    .add(Restrictions.eq("chaveMesAnoTipoLeito", chaveMesAnoGerada));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se a Tabela Tipo Leito está cadastrada em Leitos. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }

    public Boolean verificarSePossuiInternacao(String chaveMesAnoGerada) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Long resultado = null;

        try {
            Criteria crit = session.createCriteria(Internacao.class)
                    .setProjection(Projections.count("idInternacao"))
                    .add(Restrictions.eq("chaveMesAnoProcedimento", chaveMesAnoGerada))
                    .add(Restrictions.eq("chaveMesAnoCID", chaveMesAnoGerada));

            transaction.commit();

            resultado = (Long) crit.uniqueResult();

            session.close();
        } catch (HibernateException e) {
            System.out.println("Problemas ao verificar se a Tabela Tipo Leito está cadastrada em Leitos. Erro: " + e.getMessage());
            transaction.rollback();
        }
        return resultado > 0;
    }
}
