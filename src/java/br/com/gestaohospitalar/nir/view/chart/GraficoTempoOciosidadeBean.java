/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view.chart;

import br.com.gestaohospitalar.nir.DAO.EstatisticasDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SetorDAOImpl;
import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Estatisticas;
import br.com.gestaohospitalar.nir.model.Setor;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import br.com.gestaohospitalar.nir.validator.PeriodoValidator;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author Daniel
 */
@ManagedBean
@RequestScoped
public class GraficoTempoOciosidadeBean implements Serializable {

    private EstatisticasDAOImpl daoEstatisticas;
    private Estatisticas estatisticas;

    private List<Setor> setores = new ArrayList<>();
    
    private List<Estatisticas> listaEstatisticas = new ArrayList<>();

    private int modeloGrafico; //1 = gráfico de linha // 2 = gráfico de área // 3 = gráfico de barra
    private int tipoCalculo; // 1 = média // 2 = soma

    private LineChartModel modeloLinha = new LineChartModel(); //gráfico de linhas
    private BarChartModel modeloBarra = new BarChartModel(); //gráfico de barras

    private Date dataInicial;
    private Date dataFinal;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/yyyy");

    /**
     * Creates a new instance of GraficoTempoOciosidadeBean
     */
    public GraficoTempoOciosidadeBean() {
        this.estatisticas = new Estatisticas();
    }

    @PostConstruct
    public void init() {
        //listando os setores
        this.setores = new SetorDAOImpl().ativos();
    }

    /**
     * método que valida as informações para montar o gráfico
     *
     */
    public void prepararGrafico() {

        //verificando se data inicial está menor que a data final
        if (PeriodoValidator.comparaDatas(this.dataInicial, this.dataFinal) == false) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "O Mês/Ano Inicial não pode ser maior que o Mês/Ano Final.");
            //verificando se a quantidade de meses do período está dentro do limite de 12 meses
        } else if (PeriodoValidator.qtdMeses(this.dataInicial, this.dataFinal, 12) == false) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Período informado deve ser no máximo 12 meses.");
        } else {

            //se tudo estiver ok, da continuidade no processo de montar o gráfico
            graficoTempoOciosidade();

        }
    }

    /**
     * método que chama as rotinas para montar o gráfico de tempo de tempo ociosidade
     *
     */
    public void graficoTempoOciosidade() {

        this.modeloLinha = new LineChartModel();
        this.modeloBarra = new BarChartModel();

        switch (this.modeloGrafico) {
            case 1: //gráfico de linha
            case 2: //gráfico de área
                //montando o layout do gráfico de linha ou área
                layoutGraficoLinha("Tempo de Ociosidade", "", "Tempo em horas");
                break;

            case 3: //gráfico de barra
                //montando o layout do gráfico de barra
                layoutGraficoBarra("Tempo de Ociosidade", "", "Tempo em horas");
                break;
        }

        
        adicionarSerie(null, "Todos Setores", true);
        //para cada setor encontrado adiciona uma série
        //passando o idSetor e rótulo
        this.setores.forEach((setor) -> {
            adicionarSerie(setor.getIdSetor(), (setor.getIdSetor() + " - " + setor.getDescricaoSetor()), false);
        });

    }

    /**
     * método que cria o layout para o gráfico de linhas
     *
     * @param titulo
     * @param descricaoEixoX
     * @param descricaoEixoY
     */
    public void layoutGraficoLinha(String titulo, String descricaoEixoX, String descricaoEixoY) {

        this.modeloLinha.setTitle(titulo);
        this.modeloLinha.setLegendPosition("ne");
        this.modeloLinha.setAnimate(true);
        this.modeloLinha.setShowPointLabels(true);
        this.modeloLinha.getAxes().put(AxisType.X, new CategoryAxis());

        //eixo x
        Axis xAxis = this.modeloLinha.getAxis(AxisType.X);
        xAxis.setLabel(descricaoEixoX);
        xAxis.setTickAngle(-50);

        //eixo y
        Axis yAxis = this.modeloLinha.getAxis(AxisType.Y);
        yAxis.setLabel(descricaoEixoY);
        //Valor inicial ao final eixo y
        yAxis.setMin(0);
        //yAxis.setMax(60);

    }

    private void layoutGraficoBarra(String titulo, String descricaoEixoX, String descricaoEixoY) {

        this.modeloBarra.setTitle(titulo);
        this.modeloBarra.setLegendPosition("ne");
        this.modeloBarra.setAnimate(true);
        this.modeloBarra.setShowPointLabels(true);
        this.modeloBarra.getAxes().put(AxisType.X, new CategoryAxis());

        //eixo x
        Axis xAxis = this.modeloBarra.getAxis(AxisType.X);
        xAxis.setLabel(descricaoEixoX);
        xAxis.setTickAngle(-50);

        //eixo y
        Axis yAxis = this.modeloBarra.getAxis(AxisType.Y);
        yAxis.setLabel(descricaoEixoY);
        //Valor inicial ao final eixo y
        yAxis.setMin(0);
        //yAxis.setMax(60);

    }

    /**
     * método que adiciona as séries ao LineChartModel model que é o modelo de
     * gráfico, cada série representa uma informação ex. se for um gráfico de
     * leitos, cada série vai representar um leito
     *
     * @param idSetor
     * @param rotulo
     * @param isTodosSetores
     */
    public void adicionarSerie(Integer idSetor, String rotulo, boolean isTodosSetores) {

        //recebendo os valores do tempo de ociosidade por setores do período informado
        Map<Date, Double> valoresPorData = tempoOciosidade(idSetor, this.dataInicial, this.dataFinal, isTodosSetores);

        //cria uma instância para adicionar as séries
        //o rótulo e o título da série (no caso foi passado o tipo do setor)
        switch (this.modeloGrafico) {
            case 1: //gráfico de linhas
                LineChartSeries serieLinha = new LineChartSeries(rotulo);

                //para cada chave do mapa que contém as estatísticas do setor no período informado
                //adiciona os valores para a série
                valoresPorData.keySet().forEach((data) -> {
                    serieLinha.set(DATE_FORMAT.format(data), valoresPorData.get(data));
                });

                this.modeloLinha.addSeries(serieLinha);

                break;

            case 2: //gráfico de áreas
                LineChartSeries serieArea = new LineChartSeries(rotulo);
                serieArea.setFill(true); //exibir como área

                //para cada chave do mapa que contém as estatísticas do setor no período informado
                //adiciona os valores para a série
                valoresPorData.keySet().forEach((data) -> {
                    serieArea.set(DATE_FORMAT.format(data), valoresPorData.get(data));
                });

                this.modeloLinha.addSeries(serieArea);

                break;

            case 3: //gráfico de barras
                ChartSeries serieBarra = new ChartSeries(rotulo);

                //para cada chave do mapa que contém as estatísticas do leito no período informado
                //adiciona os valores para a série
                valoresPorData.keySet().forEach((data) -> {
                    serieBarra.set(DATE_FORMAT.format(data), valoresPorData.get(data));
                });

                this.modeloBarra.addSeries(serieBarra);

                break;
        }

    }

    /**
     * método que retorna os valores do tempo de ociocidade por
     * setores do período informado
     *
     * @param idSetor
     * @param dataInicial
     * @param dataFinal
     * @param isTodosSetores
     * @return resultado
     */
    public Map<Date, Double> tempoOciosidade(Integer idSetor, Date dataInicial, Date dataFinal, boolean isTodosSetores) {
        this.daoEstatisticas = new EstatisticasDAOImpl();

        //convertendo a data inicial para Calendar
        Calendar cDataInicial = NIRDataUtil.paraCalendar(dataInicial);

        //convertendo as datas inicial e final para LocalDate
        LocalDate ldDataInicial = NIRDataUtil.paraLocalDate(dataInicial);
        LocalDate ldDataFinal = NIRDataUtil.paraLocalDate(dataFinal);
        //extraindo a quantidade de meses entre as datas inicial e final
        long qtdMesesPeriodo = ChronoUnit.MONTHS.between(ldDataInicial, ldDataFinal);

        Double qtdDifTempoHoras;

        //cria um mapa inicial com as datas dentro do período informado
        Map<Date, Double> resultado = criarMapaVazio(cDataInicial, qtdMesesPeriodo);

        //buscando as estatísticas de acordo com o leito e período informado
        this.listaEstatisticas = isTodosSetores ? this.daoEstatisticas.porPeriodoETodosSetores(dataInicial, dataFinal, this.tipoCalculo) : this.daoEstatisticas.porPeriodoEIdSetor(idSetor, dataInicial, dataFinal, this.tipoCalculo);

        //para cada estatistica encontrada adiciona os valores 
        //de dataHoraFimHigienizacao da higienização (eixo x)
        //e tempo em minutos da ociosidade (eixo y)
        for (Estatisticas estatistica : this.listaEstatisticas) {

            if (estatistica.getHigienizacao() != null && estatistica.getTempoOciosidade() != null) {
                //calculando o tempo ociosidade em horas
                qtdDifTempoHoras = estatistica.getTempoOciosidade() / 3600.0;

                //adicionando as informações ao map
                resultado.put(estatistica.getHigienizacao().getDataHoraFim(), qtdDifTempoHoras);
            }

        }

        return resultado;
    }

    /**
     * método que cria um mapa inicial com as datas do período informado com os
     * valores zerados
     *
     * @param dataInicial
     * @param qtdMesesPeriodo
     * @return mapaInicial
     */
    public Map<Date, Double> criarMapaVazio(Calendar dataInicial, long qtdMesesPeriodo) {

        dataInicial = (Calendar) dataInicial.clone();

        Map<Date, Double> mapaInicial = new TreeMap<>();

        for (int i = 0; i <= qtdMesesPeriodo; i++) {
            mapaInicial.put(dataInicial.getTime(), 0.0);
            dataInicial.add(Calendar.MONTH, 1);
        }

        return mapaInicial;
    }

    /**
     * @return the modeloLinha
     */
    public LineChartModel getModeloLinha() {
        if (this.modeloLinha.getSeries().isEmpty()) {
            return null;
        } else {
            return modeloLinha;
        }
    }

    /**
     * @return the modeloBarra
     */
    public BarChartModel getModeloBarra() {
        if (this.modeloBarra.getSeries().isEmpty()) {
            return null;
        } else {
            return modeloBarra;
        }
    }

    /**
     * @return the estatisticas
     */
    public Estatisticas getEstatisticas() {
        return estatisticas;
    }

    /**
     * @param estatisticas the estatisticas to set
     */
    public void setEstatisticas(Estatisticas estatisticas) {
        this.estatisticas = estatisticas;
    }

    /**
     * @return the setores
     */
    public List<Setor> getSetores() {
        return setores;
    }

    /**
     * @return the dataInicial
     */
    public Date getDataInicial() {
        return dataInicial;
    }

    /**
     * @param dataInicial the dataInicial to set
     */
    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    /**
     * @return the dataFinal
     */
    public Date getDataFinal() {
        return dataFinal;
    }

    /**
     * @param dataFinal the dataFinal to set
     */
    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    /**
     * @return the modeloGrafico
     */
    public int getModeloGrafico() {
        return modeloGrafico;
    }

    /**
     * @param modeloGrafico the modeloGrafico to set
     */
    public void setModeloGrafico(int modeloGrafico) {
        this.modeloGrafico = modeloGrafico;
    }
    
     /**
     * @return the tipoCalculo
     */
    public int getTipoCalculo() {
        return tipoCalculo;
    }

    /**
     * @param tipoCalculo the tipoCalculo to set
     */
    public void setTipoCalculo(int tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }

}
