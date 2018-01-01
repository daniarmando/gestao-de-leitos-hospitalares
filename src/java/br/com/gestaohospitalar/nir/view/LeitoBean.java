/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.InternacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LeitoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.MedicoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.QuartoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SigtapUploadDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.sigtap.TB_TIPO_LEITO;
import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Medico;
import br.com.gestaohospitalar.nir.model.Quarto;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
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
public class LeitoBean implements InterfaceBean, Serializable {

    private LeitoDAOImpl daoLeito;
    private Leito leito, cloneLeito;

    private List<Leito> leitos, leitosFiltrados;

    private List<Quarto> quartos;

    private List<TB_TIPO_LEITO> tipo_leitos;

    private List<Internacao> historicoInternacoesPorLeito;

    private List<Medico> medicos;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    private Integer tipoPesq; //1 = pesquisa por setor (padrão) // 2 = pesquisa por médico //

    private Integer pesqSetor; //recebe o valor para consultar o leito por setor
    private Medico pesqMedico = new Medico(); //recebe o valor para consultar o leito por médico
    private Boolean exibePesqSetor = true, exibePesqMedico = false; //usado para exibir qual campo será mostrado na tela de pesquisa

    private Leito leitoSelecionado;

    private final Date dataAtual = new Date();

    /**
     * Creates a new instance of LeitoBean
     */
    public LeitoBean() {
        this.leito = new Leito();
    }

    @Override
    public void inicializarConsulta() {
        this.daoLeito = new LeitoDAOImpl();
        this.leitos = this.daoLeito.todosDisponiveis();
    }

    @Override
    public void inicializarCadastro() {

        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.log = new Log(TipoLog.INCLUSAO.get());
            this.quartos = new QuartoDAOImpl().ativos();
            this.tipo_leitos = new SigtapUploadDAOImpl().listar();
        }

    }

    @Override
    public void inicializarEdicao() {
        this.daoLeito = new LeitoDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());

        this.leito = this.daoLeito.porId(this.leito.getIdLeito());
        this.cloneLeito = this.leito.clone();
        this.quartos = new QuartoDAOImpl().ativos();
        this.tipo_leitos = new SigtapUploadDAOImpl().listar();
    }

    @Override
    public void salvar() {
        this.daoLeito = new LeitoDAOImpl();

        try {

            //passando a chaveMesAno se for incluir um novo registro
            if (this.leito.getIdLeito() == null || this.leito.getIdLeito() == 0) {
                this.leito.setChaveMesAnoTipoLeito(ConverterDataHora.ultimaChaveMesAno());
            }

            this.daoLeito.salvar(this.leito);

            //gravando o log
            salvarLog();

            this.leito = new Leito();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Leito salvo com sucesso!");

        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.leito.getIdLeito() != null;
    }

    @Override
    public void excluir() {
        this.daoLeito = new LeitoDAOImpl();

        try {

            if (daoLeito.temInternacaoAberta(this.leito) == false) {
                this.leito.setStatusLeito(Status.INDISPONIVEL.get());

                this.daoLeito.salvar(this.leito);

                //gravando o log
                this.log = new Log(TipoLog.INATIVACAO.get());
                this.salvarLog();

                this.inicializarConsulta();

                this.leito = new Leito();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Leito inativado com sucesso!");
            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Leito não pode ser inativado, pois possuí Internação em aberto!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public void salvarLog() {
        this.daoLog = new LogDAOImpl();

        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.leito.getCodigoLeito().equals(this.cloneLeito.getCodigoLeito())) {
                detalhe += " código leito controle hospital de " + this.cloneLeito.getCodigoLeito() + " para " + this.leito.getCodigoLeito() + ",";
            }

            if (!this.leito.getDescricaoLeito().equals(this.cloneLeito.getDescricaoLeito())) {
                detalhe += " descrição de " + this.cloneLeito.getDescricaoLeito() + " para " + this.leito.getDescricaoLeito() + ",";
            }

            if (!this.leito.getStatusLeito().equals(this.cloneLeito.getStatusLeito())) {
                detalhe += " status de "
                        + (this.cloneLeito.getStatusLeito().equals(Status.DISPONIVEL.get()) ? " disponível " : this.cloneLeito.getStatusLeito().equals(Status.INDISPONIVEL.get()) ? " indisponível " : " manutenção ")
                        + " para "
                        + (this.leito.getStatusLeito().equals(Status.DISPONIVEL.get()) ? " disponível " : this.cloneLeito.getStatusLeito().equals(Status.INDISPONIVEL.get()) ? " indisponível " : " manutenção ")
                        + ",";
            }

            if (!this.leito.getQuarto().getTipoQuarto().equals(this.cloneLeito.getQuarto().getTipoQuarto())) {
                detalhe += " quarto de " + this.cloneLeito.getQuarto().getTipoQuarto() + " para " + this.leito.getQuarto().getTipoQuarto() + ",";
            }

            if (!this.leito.getTipo_leito().getNO_TIPO_LEITO().equals(this.cloneLeito.getTipo_leito().getNO_TIPO_LEITO())) {
                detalhe += " tipo de " + this.cloneLeito.getTipo_leito().getNO_TIPO_LEITO() + " para " + this.leito.getTipo_leito().getNO_TIPO_LEITO() + ",";
            }

            if (!this.leito.getIdadeMinima().equals(this.cloneLeito.getIdadeMinima())) {
                detalhe += " idade mínima de " + this.cloneLeito.getIdadeMinima() + " para " + this.leito.getIdadeMinima() + ",";
            }

            if (!this.leito.getIdadeMaxima().equals(this.cloneLeito.getIdadeMaxima())) {
                detalhe += " idade máxima de " + this.cloneLeito.getIdadeMaxima() + " para " + this.leito.getIdadeMaxima() + ",";
            }

            if (!this.leito.getTipoSexo().equals(this.cloneLeito.getTipoSexo())) {
                detalhe += " tipo sexo de "
                        + (this.cloneLeito.getTipoSexo().equals("M") ? " masculino " : this.cloneLeito.getTipoSexo().equals("F") ? " feminino " : " indiferente/ambos ")
                        + " para "
                        + (this.leito.getTipoSexo().equals("M") ? " masculino " : this.leito.getTipoSexo().equals("F") ? " feminino " : " indiferente/ambos ")
                        + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("leito");
        this.log.setIdObjeto(this.leito.getIdLeito());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("leito");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora())
                + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("leito", this.leito.getIdLeito());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioLeito";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.leitos);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * método que verifica qual o tipo de pesquisa
     *
     */
    public void verificarTipoPesq() {

        //limpando os parâmetros de pesquisa
        this.pesqSetor = null;
        this.pesqMedico = new Medico();

        if (this.tipoPesq == 1) {
            this.exibePesqSetor = true;
            this.exibePesqMedico = false;
        } else {
            this.exibePesqSetor = false;
            this.exibePesqMedico = true;
            //carregando os médicos na combo
            this.medicos = new MedicoDAOImpl().ativos();
        }
    }

    /**
     * Método listar pelo tipo de pesquisa selecionado
     *
     * @return listarPorIdQuarto
     */
    public List<Leito> listarLeitos() {
        List<Leito> listaLeitos = null;
        this.daoLeito = new LeitoDAOImpl();

        if (this.pesqSetor != null || this.pesqMedico.getIdPessoa() != null) {
            if (this.exibePesqSetor) {
                listaLeitos = this.daoLeito.disponiveisPorIdSetor(this.pesqSetor);
            } else if (this.exibePesqMedico) {
                listaLeitos = this.daoLeito.emInternacaoPorIdMedico(this.pesqMedico.getIdPessoa());
            }
        } else {
            return null;
        }

        return listaLeitos;
    }

    /**
     * Método que lista o histórico de internações por leito
     *
     * @param idLeito
     * @return getHistoricoInternacoesPorLeito
     */
    public List<Internacao> listarHistoricoInternacoes(Integer idLeito) {
        InternacaoDAOImpl daoInternacao = new InternacaoDAOImpl();
        return this.historicoInternacoesPorLeito = daoInternacao.historicoInternacoesPorLeito(idLeito);
    }

    public String nomeReduzidoPaciente(String nome) {
        return this.leito.getInternacao().getPaciente().nomeReduzidoPessoa(nome);
    }

    public Leito getLeito() {
        return leito;
    }

    public void setLeito(Leito leito) {
        this.leito = leito;
    }

    public List<Leito> getLeitos() {
        return leitos;
    }

    public List<Leito> getLeitosFiltrados() {
        return leitosFiltrados;
    }

    public void setLeitosFiltrados(List<Leito> leitosFiltrados) {
        this.leitosFiltrados = leitosFiltrados;
    }

    public List<Quarto> getQuartos() {
        return quartos;
    }

    public List<TB_TIPO_LEITO> getTipo_leitos() {
        return tipo_leitos;
    }

    public void setTipo_leitos(List<TB_TIPO_LEITO> tipo_leitos) {
        this.tipo_leitos = tipo_leitos;
    }

    public void setLeitos(List<Leito> leitos) {
        this.leitos = leitos;
    }

    public Leito getLeitoSelecionado() {
        return leitoSelecionado;
    }

    public void setLeitoSelecionado(Leito leitoSelecionado) {
        this.leitoSelecionado = leitoSelecionado;
    }

    public Date getDataAtual() {
        return dataAtual;
    }

    public List<Internacao> getHistoricoInternacoesPorLeito() {
        return historicoInternacoesPorLeito;
    }

    public Integer getTipoPesq() {
        return tipoPesq;
    }

    public void setTipoPesq(Integer tipoPesq) {
        this.tipoPesq = tipoPesq;
    }

    public Integer getPesqSetor() {
        return pesqSetor;
    }

    public void setPesqSetor(Integer pesqSetor) {
        this.pesqSetor = pesqSetor;
    }

    public Boolean getExibePesqSetor() {
        return exibePesqSetor;
    }

    public void setExibePesqSetor(Boolean exibePesqSetor) {
        this.exibePesqSetor = exibePesqSetor;
    }

    public Boolean getExibePesqMedico() {
        return exibePesqMedico;
    }

    public void setExibePesqMedico(Boolean exibePesqMedico) {
        this.exibePesqMedico = exibePesqMedico;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(List<Medico> medicos) {
        this.medicos = medicos;
    }

    public Medico getPesqMedico() {
        return pesqMedico;
    }

    public void setPesqMedico(Medico pesqMedico) {
        this.pesqMedico = pesqMedico;
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
