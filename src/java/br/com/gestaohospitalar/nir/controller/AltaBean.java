/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.AltaDAOImpl;
import br.com.gestaohospitalar.nir.DAO.AltaQualificadaDAOImpl;
import br.com.gestaohospitalar.nir.DAO.InternacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.MedicoDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Alta;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Medico;
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
public class AltaBean implements Serializable, InterfaceBean {

    private Alta alta;
    private final AltaDAOImpl daoAlta = new AltaDAOImpl();

    private final AltaQualificadaDAOImpl daoAltaQualificada = new AltaQualificadaDAOImpl();

    private List<Alta> filtrarLista;
    private List<Alta> altas = new ArrayList<>();

    private final InternacaoDAOImpl daoInternacao = new InternacaoDAOImpl();
    private List<Internacao> internacoes = new ArrayList<>();

    private final MedicoDAOImpl daoMedico = new MedicoDAOImpl();
    private List<Medico> medicos = new ArrayList<>();

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    private Alta cloneAlta;

    private Boolean habilitaCampos;

    /**
     * Creates a new instance of AltaBean
     */
    public AltaBean() {
        this.alta = new Alta();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.altas = this.daoAlta.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {

        this.log = new Log();

        if (isEditar()) {
            this.habilitaCampos = true;
            this.cloneAlta = this.alta.clone();
            this.medicos = this.daoMedico.listar();
            this.log.setTipo(TipoLog.ALTERAR_ALTA.get());
        } else {
            this.internacoes = this.daoInternacao.listarParaAlta();
            this.habilitaCampos = !this.internacoes.isEmpty();
            this.log.setTipo(TipoLog.REGISTRAR_ALTA.get());
            if (this.habilitaCampos) {
                this.medicos = this.daoMedico.listar();
            }
        }

    }

    @Override
    public String novo() {
        this.alta = new Alta();
        return "cadastro-alta?faces-redirect=true";
    }

    @Override
    public void salvar() {

        //se a data da alta informada for igual que a data de entrada da internação
        if (this.alta.getDataHoraRealizacao().compareTo(this.alta.getInternacao().getDataEntrada()) == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data da Alta não pode ser igual que a data de entrada da Internação!", null));
            //se a data da previsão de alta informada for menor que a data de entrada da internação
        } else if (this.alta.getDataHoraRealizacao().before(this.alta.getInternacao().getDataEntrada())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data da Alta não pode ser menor que a data de entrada da Internação!", null));
        } else {

            //passando a alta qualificada para a alta
            this.alta.setAltaQualificada(this.daoAltaQualificada.altaQualificadaPorIdInternacao(this.alta.getInternacao().getIdInternacao()));

            //passando a data e hora atual
            this.alta.setDataHoraInformacao(new Date());

            //salvando a alta
            this.daoAlta.salvar(this.alta);

            //atualizando a lista de internações
            this.internacoes.remove(this.alta.getInternacao());
            this.habilitaCampos = !this.internacoes.isEmpty();

            //salvando o log
            salvarLog();

            this.alta = new Alta();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Alta salva com sucesso!"));
        }

    }

    @Override
    public Boolean isEditar() {
        return this.alta.getIdAlta() != null;
    }

    @Override
    public void excluir() {

        //verificando se pode excluir 
        if (this.daoInternacao.verificarSeHouveSaida(this.alta.getInternacao().getIdInternacao()) == false) {

            //excluindo alta 
            this.daoAlta.excluir(this.alta);

            //atualizando lista de alta 
            this.altas.remove(this.alta);

            //salvando o log
            this.log.setTipo(TipoLog.EXCLUIR_ALTA.get());
            salvarLog();

            this.alta = new Alta();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Alta excluída com sucesso!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alta não pode ser excluída, pois já foi registrado Saída do Paciente do Leito!", null));
        }

    }

    @Override
    public void salvarLog() {
        String detalhe = "";

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERAR_ALTA.get())) {
            detalhe = "";

            detalhe += "alta código " + this.alta.getIdAlta() + " alterada:";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.alta.getDataHoraRealizacao().equals(this.cloneAlta.getDataHoraRealizacao())) {
                detalhe += " data e hora da alta de " + ConverterDataHora.formatarDataHora(this.cloneAlta.getDataHoraRealizacao()) + " para " + ConverterDataHora.formatarDataHora(this.alta.getDataHoraRealizacao()) + ",";
            }

            if (!this.alta.getMedico().getNomePessoa().equals(this.cloneAlta.getMedico().getNomePessoa())) {
                detalhe += " médico da alta de " + this.cloneAlta.getMedico().getNomePessoa() + " para " + this.alta.getMedico().getNomePessoa() + ",";
            }
        } else {
                detalhe += "alta código " + this.alta.getIdAlta() + ",";
        }

        //removendo última vírgula e adicionando ponto final
        detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("internacao");
        this.log.setIdObjeto(this.alta.getInternacao().getIdInternacao());
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
        this.logs = this.daoLog.listarPorIdObjeto("internacao", this.alta.getInternacao().getIdInternacao());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioAlta";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.altas);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * @return the alta
     */
    public Alta getAlta() {
        return alta;
    }

    /**
     * @param alta the alta to set
     */
    public void setAlta(Alta alta) {
        this.alta = alta;
    }

    /**
     * @return the filtrarLista
     */
    public List<Alta> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Alta> filtrarLista) {
        this.filtrarLista = filtrarLista;
    }

    /**
     * @return the altas
     */
    public List<Alta> getAltas() {
        return altas;
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

    /**
     * @return the medicos
     */
    public List<Medico> getMedicos() {
        return medicos;
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
