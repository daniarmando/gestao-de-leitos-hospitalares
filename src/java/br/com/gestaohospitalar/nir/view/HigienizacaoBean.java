/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.FuncionarioDAOImpl;
import br.com.gestaohospitalar.nir.DAO.HigienizacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.InternacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LeitoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Funcionario;
import br.com.gestaohospitalar.nir.model.Higienizacao;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import br.com.gestaohospitalar.nir.util.report.GerarRelatorio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Daniel
 */
@ManagedBean
@ViewScoped
public class HigienizacaoBean implements InterfaceBean, Serializable {

    private Higienizacao higienizacao;
    private HigienizacaoDAOImpl daoHigienizacao;

    private List<Higienizacao> higienizacoes, higienizacoesFiltradas;

    private List<Funcionario> funcionarios;

    private List<Internacao> internacoes;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    private Higienizacao cloneHigienizacao;

    private Boolean habilitaCampos;

    /**
     * Creates a new instance of HigienizacaoBean
     */
    public HigienizacaoBean() {
        this.higienizacao = new Higienizacao();
    }

    @Override
    public void inicializarConsulta() {
        this.daoHigienizacao = new HigienizacaoDAOImpl();
        this.higienizacoes = this.daoHigienizacao.todas();
    }

    @Override
    public void inicializarCadastro() {

        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.log = new Log(TipoLog.REGISTRAR_HIGIENIZACAO.get());
            
            this.internacoes = new InternacaoDAOImpl().paraHigienizacao();
            this.habilitaCampos = !this.internacoes.isEmpty();
            if (this.habilitaCampos) {
                this.funcionarios = new FuncionarioDAOImpl().ativos();
            }
        }
    }

    @Override
    public void inicializarEdicao() {
        this.daoHigienizacao = new HigienizacaoDAOImpl();
        this.log = new Log(TipoLog.ALTERAR_HIGIENIZACAO.get());
        
        this.higienizacao = this.daoHigienizacao.porId(this.higienizacao.getIdHigienizacao());
        this.cloneHigienizacao = this.higienizacao.clone();
        this.funcionarios = new FuncionarioDAOImpl().ativos();
        this.habilitaCampos = true;
    }

    @Override
    public void salvar() {
        this.daoHigienizacao = new HigienizacaoDAOImpl();

        try {

            //se a data inicial for igual a final
            if (this.higienizacao.getDataHoraInicio().compareTo(this.higienizacao.getDataHoraFim()) == 0) {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "A data e hora de início não pode ser igual a data e hora final!");
                //se a data inicial for maior que a data final
            } else if (this.higienizacao.getDataHoraInicio().after(this.higienizacao.getDataHoraFim())) {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "A data e hora de início não pode ser maior que a data e hora final!");
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

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Higienização salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.higienizacao.getIdHigienizacao() != null;
    }

    @Override
    public void excluir() {
        this.daoHigienizacao = new HigienizacaoDAOImpl();

        try {

            //verificando se pode excluir 
            if (new LeitoDAOImpl().temNovaInternacao(this.higienizacao.getInternacao()) == false) {

                //excluindo higienização
                this.daoHigienizacao.excluir(this.higienizacao);                

                //salvando o log
                this.log = new Log(TipoLog.EXCLUIR_HIGIENIZACAO.get());
                this.salvarLog();
                
                this.inicializarConsulta();

                this.higienizacao = new Higienizacao();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Higienização excluída com sucesso!");

            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Higienização não pode ser excluída, pois já foi registrado nova Internação no Leito!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }

    }

    @Override
    public void salvarLog() {
        this.daoLog = new LogDAOImpl();
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
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("internacao");
        return this.log != null ? "Última modificação em processo de internação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("internacao", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioHigienizacao";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.higienizacoes);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public Higienizacao getHigienizacao() {
        return higienizacao;
    }

    public void setHigienizacao(Higienizacao higienizacao) {
        this.higienizacao = higienizacao;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }
    
     public List<Higienizacao> getHigienizacoes() {
        return higienizacoes;
    }

    public List<Higienizacao> getHigienizacoesFiltradas() {
        return higienizacoesFiltradas;
    }

    public void setHigienizacoesFiltradas(List<Higienizacao> higienizacoesFiltradas) {
        this.higienizacoesFiltradas = higienizacoesFiltradas;
    }

    public List<Internacao> getInternacoes() {
        return internacoes;
    }

    public Boolean getHabilitaCampos() {
        return habilitaCampos;
    }
    
    public UsuarioBean getUsuario() {
        return this.usuario;
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }
}
