/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.AltaQualificadaDAOImpl;
import br.com.gestaohospitalar.nir.DAO.InternacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.AltaQualificada;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.util.report.GerarRelatorio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public class AltaQualificadaBean implements Serializable, InterfaceBean {

    private AltaQualificada altaQualificada;
    private final AltaQualificadaDAOImpl daoAltaQualificada = new AltaQualificadaDAOImpl();

    private List<AltaQualificada> filtrarLista;
    private List<AltaQualificada> altasQualificadas = new ArrayList<>();

    private final InternacaoDAOImpl daoInternacao = new InternacaoDAOImpl();
    private List<Internacao> internacoes = new ArrayList<>();

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    private AltaQualificada cloneAltaQualificada;

    private Boolean habilitaCampos;

    /**
     * Creates a new instance of AltaQualificadaBean
     */
    public AltaQualificadaBean() {
        altaQualificada = new AltaQualificada();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.altasQualificadas = this.daoAltaQualificada.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {

        this.log = new Log();

        if (isEditar()) {
            habilitaCampos = true;
            this.cloneAltaQualificada = this.altaQualificada.clone();
            this.log.setTipo(TipoLog.ALTERAR_ALTA_QUALIFICADA.get());
        } else {
            this.internacoes = this.daoInternacao.listarParaAltaQualificada();
            habilitaCampos = !this.internacoes.isEmpty();
            this.log.setTipo(TipoLog.REGISTRAR_ALTA_QUALIFICADA.get());
        }

    }

    @Override
    public String novo() {
        this.altaQualificada = new AltaQualificada();
        return "cadastro-alta-qualificada?faces-redirect=true";
    }

    @Override
    public void salvar() {

        //se a data da previsão de alta informada for igual que a data de entrada da internação
        if (this.altaQualificada.getDataHoraPrevisao().compareTo(this.altaQualificada.getInternacao().getDataEntrada()) == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Data da Previsão de Alta não pode ser igual que a Data de Entrada da Internação!", null));
            //se a data da previsão de alta informada for menor que a data de entrada da internação
        } else if (this.altaQualificada.getDataHoraPrevisao().before(this.altaQualificada.getInternacao().getDataEntrada())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Data da Previsão de Alta não pode ser menor que a Data de Entrada da Internação!", null));
        } else {

            //passando a data e hora atual
            this.altaQualificada.setDataHoraInformacao(new Date());

            //salvando a alta qualificada
            this.daoAltaQualificada.salvar(this.altaQualificada);

            //atualizando a lista de internações
            this.internacoes.remove(this.altaQualificada.getInternacao());
            habilitaCampos = !this.internacoes.isEmpty();

            //salvando o log
            salvarLog();

            this.altaQualificada = new AltaQualificada();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Alta Qualificada salva com sucesso!"));
        }

    }

    @Override
    public Boolean isEditar() {
        return this.altaQualificada.getIdAltaQualificada() != null;
    }

    @Override
    public void excluir() {

        //verificando se pode excluir 
        if (this.daoAltaQualificada.verificarSePossuiAlta(this.altaQualificada.getIdAltaQualificada()) == false) {

            //excluindo alta qualificada
            this.daoAltaQualificada.excluir(this.altaQualificada);

            //atualizando lista de alta qualificada
            this.altasQualificadas.remove(this.altaQualificada);

            //salvando o log
            this.log.setTipo(TipoLog.EXCLUIR_ALTA_QUALIFICADA.get());
            salvarLog();

            this.altaQualificada = new AltaQualificada();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Alta Qualificada excluída com sucesso!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alta Qualificada não pode ser excluída, pois já foi registrado Alta para o Paciente!", null));
        }

    }

    @Override
    public void salvarLog() {
        String detalhe = "";

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERAR_ALTA_QUALIFICADA.get())) {
            detalhe = "";

            detalhe += "alta qualificada código " + this.altaQualificada.getIdAltaQualificada() + " alterada:";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.altaQualificada.getDataHoraPrevisao().equals(this.cloneAltaQualificada.getDataHoraPrevisao())) {
                detalhe += " data e hora da previsão de " + ConverterDataHora.formatarDataHora(this.cloneAltaQualificada.getDataHoraPrevisao()) + " para " + ConverterDataHora.formatarDataHora(this.altaQualificada.getDataHoraPrevisao()) + ",";
            }
        } else {
                detalhe += "alta qualificada código de " + this.altaQualificada.getIdAltaQualificada() + ",";
        }

        //removendo última vírgula e adicionando ponto final
        detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";        

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("internacao");
        this.log.setIdObjeto(this.altaQualificada.getInternacao().getIdInternacao());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("internacao");
        return this.log != null ? "Última modificação em processo de internação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("internacao", this.altaQualificada.getInternacao().getIdInternacao());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioAltaQualificada";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.altasQualificadas);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * @return the altaQualificada
     */
    public AltaQualificada getAltaQualificada() {
        return altaQualificada;
    }

    /**
     * @param altaQualificada the altaQualificada to set
     */
    public void setAltaQualificada(AltaQualificada altaQualificada) {
        this.altaQualificada = altaQualificada;
    }

    /**
     * @return the filtrarLista
     */
    public List<AltaQualificada> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<AltaQualificada> filtrarLista) {
        this.filtrarLista = filtrarLista;
    }

    /**
     * @return the altasQualificadas
     */
    public List<AltaQualificada> getAltasQualificadas() {
        return altasQualificadas;
    }

    /**
     * @return the internacoes
     */
    public List<Internacao> getInternacoes() {
        return internacoes;
    }

    /**
     * @return the habilitaCampos
     */
    public Boolean getHabilitaCampos() {
        return habilitaCampos;
    }

    //setter necessario para a anotação @ManagedProperty funcionar corretamente
    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    /**
     * @return the logs
     */
    public List<Log> getLogs() {
        return logs;
    }

    /**
     * @return the log
     */
    public Log getLog() {
        return log;
    }

    /**
     * @param log the log to set
     */
    public void setLog(Log log) {
        this.log = log;
    }

}
