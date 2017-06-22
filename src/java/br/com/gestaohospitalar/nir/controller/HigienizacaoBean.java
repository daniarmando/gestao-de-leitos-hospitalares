/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.FuncionarioDAOImpl;
import br.com.gestaohospitalar.nir.DAO.HigienizacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.InternacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Funcionario;
import br.com.gestaohospitalar.nir.model.Higienizacao;
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
public class HigienizacaoBean implements InterfaceBean, Serializable {

    private Higienizacao higienizacao;
    private final HigienizacaoDAOImpl daoHigienizacao = new HigienizacaoDAOImpl();

    private final FuncionarioDAOImpl daoFuncionario = new FuncionarioDAOImpl();
    private List<Funcionario> funcionarios = new ArrayList<>();

    private final InternacaoDAOImpl daoInternacao = new InternacaoDAOImpl();
    private List<Internacao> internacoes = new ArrayList<>();

    private List<Higienizacao> filtrarLista;
    private List<Higienizacao> higienizacoes = new ArrayList<>();

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    private Higienizacao cloneHigienizacao;

    private Boolean habilitaCampos;

    /**
     * Creates a new instance of HigienizacaoBean
     */
    public HigienizacaoBean() {
        higienizacao = new Higienizacao();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.higienizacoes = this.daoHigienizacao.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {

        this.log = new Log();

        if (isEditar()) {
            this.funcionarios = this.daoFuncionario.listar();
            this.habilitaCampos = true;
            this.cloneHigienizacao = this.higienizacao.clone();
            this.log.setTipo(TipoLog.ALTERAR_HIGIENIZACAO.get());
        } else {
            this.internacoes = this.daoInternacao.listarParaHigienizacao();
            this.habilitaCampos = !this.internacoes.isEmpty();
            this.log.setTipo(TipoLog.REGISTRAR_HIGIENIZACAO.get());
            if (this.getHabilitaCampos()) {
                this.funcionarios = this.daoFuncionario.listar();
            }
        }

    }

    @Override
    public String novo() {
        this.higienizacao = new Higienizacao();
        return "cadastro-higienizacao?faces-redirect=true";
    }

    @Override
    public void salvar() {
        //se a data inicial for igual a final
        if (this.higienizacao.getDataHoraInicio().compareTo(this.higienizacao.getDataHoraFim()) == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data e hora de início não pode ser igual a data e hora final!", null));
            //se a data inicial for maior que a data final
        } else if (this.higienizacao.getDataHoraInicio().after(this.higienizacao.getDataHoraFim())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data e hora de início não pode ser maior que a data e hora final!", null));
        } else {
            
//            if (isEditar()) {
//                //se for alteração, deleta os funcionários
//                this.daoHigienizacao.excluirFuncionarios(this.higienizacao.getIdHigienizacao());
//            }
            
            //salvando a higienização
            this.higienizacao.setTempoHigienizacaoMinutos(ConverterDataHora.diferencaEmMinutos(this.higienizacao.getDataHoraInicio(), this.higienizacao.getDataHoraFim()));
            this.daoHigienizacao.salvar(this.higienizacao);

            //salvando o log
            salvarLog();

            this.higienizacao = new Higienizacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Higienização salva com sucesso!"));
        }
    }

    @Override
    public Boolean isEditar() {
        return this.higienizacao.getIdHigienizacao() != null;
    }

    @Override
    public void excluir() {

        //verificando se pode excluir 
        if (this.daoHigienizacao.verificarSeLeitoPossuiNovaInternacao(this.higienizacao.getInternacao()) == false) {

            //excluindo higienização
            this.daoHigienizacao.excluir(this.higienizacao);

            //atualizando lista de higienização
            this.higienizacoes.remove(this.higienizacao);

            //salvando o log
            this.log.setTipo(TipoLog.EXCLUIR_HIGIENIZACAO.get());
            salvarLog();

            this.higienizacao = new Higienizacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Higienização Excluída com sucesso!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Higienização não pode ser excluída, pois já foi registrado nova Internação no Leito!", null));
        }

    }

    @Override
    public void salvarLog() {
        String detalhe = "";

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERAR_HIGIENIZACAO.get())) {
            detalhe = "";

            detalhe += "higienização código " + this.higienizacao.getIdHigienizacao() + " alterada:";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.higienizacao.getDataHoraInicio().equals(this.cloneHigienizacao.getDataHoraInicio())) {
                detalhe += " data e hora de início de " + ConverterDataHora.formatarDataHora(this.cloneHigienizacao.getDataHoraInicio()) + " para " + ConverterDataHora.formatarDataHora(this.higienizacao.getDataHoraInicio()) + ",";
            }

            if (!this.higienizacao.getDataHoraFim().equals(this.cloneHigienizacao.getDataHoraFim())) {
                detalhe += " data e hora final de " + ConverterDataHora.formatarDataHora(this.cloneHigienizacao.getDataHoraFim()) + " para " + ConverterDataHora.formatarDataHora(this.higienizacao.getDataHoraFim()) + ",";
            }

            if (!this.higienizacao.getTempoHigienizacaoMinutos().equals(this.cloneHigienizacao.getTempoHigienizacaoMinutos())) {
                detalhe += " tempo em minutos de " + this.cloneHigienizacao.getTempoHigienizacaoMinutos() + " para " + this.higienizacao.getTempoHigienizacaoMinutos() + ",";
            }

            if (!this.higienizacao.getObservacoesHigienizacao().equals(this.cloneHigienizacao.getObservacoesHigienizacao())) {
                detalhe += " observações alterada,";
            }
        } else {
            detalhe += "higienização código " + this.higienizacao.getIdHigienizacao() + ",";
        }

        //removendo última vírgula e adicionando ponto final
        detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("internacao");
        this.log.setIdObjeto(this.higienizacao.getInternacao().getIdInternacao());
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
        this.logs = this.daoLog.listarPorIdObjeto("internacao", this.higienizacao.getInternacao().getIdInternacao());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioHigienizacao";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.higienizacoes);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * @return the higienizacao
     */
    public Higienizacao getHigienizacao() {
        return higienizacao;
    }

    /**
     * @param higienizacao the higienizacao to set
     */
    public void setHigienizacao(Higienizacao higienizacao) {
        this.higienizacao = higienizacao;
    }

    /**
     * @return the funcionarios
     */
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    /**
     * @param funcionarios the funcionarios to set
     */
    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    /**
     * @return the filtrarLista
     */
    public List<Higienizacao> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Higienizacao> filtrarLista) {
        this.filtrarLista = filtrarLista;
    }

    /**
     * @return the higienizacoes
     */
    public List<Higienizacao> getHigienizacoes() {
        return higienizacoes;
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
