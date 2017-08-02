/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.DAO;

import br.com.gestaohospitalar.nir.model.Estatisticas;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Daniel
 */
public class EstatisticasDAOImpl {

    private final Session session = (Session) FacesUtil.getRequestAttribute("session");
    private final DateFormat DATE_FORMAT_MES = new SimpleDateFormat("MM");
    private final DateFormat DATE_FORMAT_ANO = new SimpleDateFormat("yyyy");
    private String calculo = "";

    public List<Estatisticas> listarPorPeriodoIdLeito(Integer idLeito, Date dataInicial, Date dataFinal) {

        //montando comando SQL
        String sql = "SELECT e.* "
                + "FROM estatisticas e, internacao i, leito l "
                + "WHERE i.idInternacao = e.idInternacao "
                + "AND l.idLeito = e.idLeito "
                + "AND i.dataEntrada BETWEEN :data_inicial AND :data_final "
                + "AND l.idLeito = :id_leito "
                + "AND i.statusInternacao = :status";
        //executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
        return (List<Estatisticas>) this.session.createSQLQuery(sql)
                .addEntity(Estatisticas.class)
                .setParameter("data_inicial", dataInicial)
                .setParameter("data_final", dataFinal)
                .setParameter("id_leito", idLeito)
                .setParameter("status", Status.ENCERRADA.get())
                .list();
    }

    public List<Estatisticas> listarPorPeriodoIdSetor(Integer idSetor, Date dataInicial, Date dataFinal, int tipoCalculo) {

        // tipoCalculo 1 = média, 2 = soma
        this.calculo = tipoCalculo == 1 ? "AVG(e.tempoOciosidade) AS tempoOciosidade" : "SUM(e.tempoOciosidade) AS tempoOciosidade";

        //montando comando SQL
        String sql = "SELECT s.idSetor, s.tipoSetor, " + this.calculo + ", h.dataHoraFim, e.* "
                + "FROM estatisticas e, internacao i, leito l, quarto q, setor s, higienizacao h "
                + "WHERE i.idInternacao = e.idInternacao "
                + "AND l.idLeito = e.idLeito "
                + "AND l.idQuarto = q.idQuarto "
                + "AND q.idSetor = s.idSetor "
                + "AND s.idSetor = :id_setor "
                + "AND h.idInternacao = e.idInternacao "
                + "AND MONTH(h.dataHoraFim) BETWEEN :mes_inicial AND :mes_final "
                + "AND YEAR(h.dataHoraFim) BETWEEN :ano_inicial AND :ano_final "
                + "AND i.statusInternacao = :status "
                + "GROUP BY s.idSetor, MONTH(h.dataHoraFim), YEAR(h.dataHoraFim)";
        //executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
        return (List<Estatisticas>) this.session.createSQLQuery(sql)
                .addEntity(Estatisticas.class)
                .setParameter("id_setor", idSetor)
                .setParameter("mes_inicial", DATE_FORMAT_MES.format(dataInicial))
                .setParameter("mes_final", DATE_FORMAT_MES.format(dataFinal))
                .setParameter("ano_inicial", DATE_FORMAT_ANO.format(dataInicial))
                .setParameter("ano_final", DATE_FORMAT_ANO.format(dataFinal))
                .setParameter("status", Status.ENCERRADA.get())
                .list();
    }

    public List<Estatisticas> listarPorPeriodoTodosSetores(Date dataInicial, Date dataFinal, int tipoCalculo) {

        // tipoCalculo 1 = média, 2 = soma
        this.calculo = tipoCalculo == 1 ? "AVG(e.tempoOciosidade) AS tempoOciosidade" : "SUM(e.tempoOciosidade) AS tempoOciosidade";

        //montando comando SQL
        String sql = "SELECT " + this.calculo + ", h.dataHoraFim, e.* "
                + "FROM estatisticas e, internacao i, leito l, quarto q, setor s, hospital ho, higienizacao h  "
                + "WHERE i.idInternacao = e.idInternacao  "
                + "AND l.idLeito = e.idLeito "
                + "AND l.idQuarto = q.idQuarto "
                + "AND q.idSetor = s.idSetor "
                + "AND s.idHospital = ho.idHospital "
                + "AND h.idInternacao = e.idInternacao "
                + "AND MONTH(h.dataHoraFim) BETWEEN :mes_inicial AND :mes_final "
                + "AND YEAR(h.dataHoraFim) BETWEEN :ano_inicial AND :ano_final "
                + "AND i.statusInternacao = :status "
                + "GROUP BY ho.idHospital, MONTH(h.dataHoraFim), YEAR(h.dataHoraFim)";
        //executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
        return (List<Estatisticas>) this.session.createSQLQuery(sql)
                .addEntity(Estatisticas.class)
                .setParameter("mes_inicial", DATE_FORMAT_MES.format(dataInicial))
                .setParameter("mes_final", DATE_FORMAT_MES.format(dataFinal))
                .setParameter("ano_inicial", DATE_FORMAT_ANO.format(dataInicial))
                .setParameter("ano_final", DATE_FORMAT_ANO.format(dataFinal))
                .setParameter("status", Status.ENCERRADA.get())
                .list();
    }

    public Estatisticas estatisticasParaDetalhes(String dataEntrada, String descricaoLeito) {

        //montando comando SQL
        String sql = "SELECT e.* "
                + "FROM estatisticas e, internacao i, leito l "
                + "WHERE i.idInternacao = e.idInternacao "
                + "AND l.idLeito = e.idLeito "
                + "AND DATE_FORMAT(i.dataEntrada, '%d/%m') = :data_entrada "
                + "AND CONCAT(l.idLeito, ' - ', l.descricaoLeito) = :descricao_leito";
        //executando comando e passando a entidade retornada pela consulta e os parâmetros para consulta
        return (Estatisticas) this.session.createSQLQuery(sql)
                .addEntity(Estatisticas.class)
                .setParameter("data_entrada", dataEntrada)
                .setParameter("descricao_leito", descricaoLeito)
                .uniqueResult();
    }
}
