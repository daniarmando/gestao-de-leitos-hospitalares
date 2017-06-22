/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.QuartoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SetorDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.Quarto;
import br.com.gestaohospitalar.nir.model.Setor;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.model.Usuario;
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
public class QuartoBean implements InterfaceBean, Serializable {

    private final QuartoDAOImpl daoQuarto = new QuartoDAOImpl();
    private Quarto quarto;
    private List<Quarto> quartos = new ArrayList<>();
    private List<Quarto> filtrarLista;

    private final SetorDAOImpl daoSetor = new SetorDAOImpl();
    private List<Setor> setores = new ArrayList<>();

    private final UsuarioDAOImpl daoUsuario = new UsuarioDAOImpl();
    private Usuario usuario;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private Quarto cloneQuarto;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    /**
     * Creates a new instance of QuartoBean
     */
    public QuartoBean() {
        quarto = new Quarto();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.quartos = this.daoQuarto.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        
        this.log = new Log();

        if (isEditar()) {
            this.cloneQuarto = this.quarto.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.setores = this.daoSetor.listar();
    }

    @Override
    public String novo() {
        setQuarto(new Quarto());
        return "cadastro-quarto?faces-redirect=true";
    }

    @Override
    public void salvar() {

        this.quarto.setStatusQuarto(Status.ATIVO.get());
        this.daoQuarto.salvar(this.quarto);

        //gravando o log
        salvarLog();

        this.quarto = new Quarto();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Quarto salvo com sucesso!"));

    }

    @Override
    public Boolean isEditar() {
        return this.quarto.getIdQuarto() != null;
    }

    @Override
    public void excluir() {
        if (daoQuarto.verificarSePossuiLeito(this.quarto) == false) {
            this.quarto.setStatusQuarto(Status.INATIVO.get());

            this.daoQuarto.salvar(this.quarto);

            //Atualizando quartos
            this.quartos.remove(this.quarto);

            //gravando o log
            this.log.setTipo(TipoLog.INATIVACAO.get());
            salvarLog();

            this.quarto = new Quarto();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Quarto Inativado com sucesso!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Quarto não pode ser Inativado, pois possuí Leito(s) cadastrado(s)!", null));
        }
    }

    @Override
    public void salvarLog() {
        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.quarto.getTipoQuarto().equals(this.cloneQuarto.getTipoQuarto())) {
                detalhe += " tipo de " + this.cloneQuarto.getTipoQuarto() + " para " + this.quarto.getTipoQuarto() + ",";
            }

            if (!this.quarto.getStatusQuarto().equals(this.cloneQuarto.getStatusQuarto())) {
                detalhe += " status de "
                        + (this.cloneQuarto.getStatusQuarto().equals(Status.ATIVO.get()) ? " ativo " : this.cloneQuarto.getStatusQuarto().equals(Status.INATIVO.get()) ? " inativo " : " manutenção ")
                        + " para "
                        + (this.quarto.getStatusQuarto().equals(Status.ATIVO.get()) ? " ativo " : this.cloneQuarto.getStatusQuarto().equals(Status.INATIVO.get()) ? " inativo " : " manutenção ")
                        + ",";
            }

            if (!this.quarto.getSetor().getTipoSetor().equals(this.cloneQuarto.getSetor().getTipoSetor())) {
                detalhe += " setor de " + this.cloneQuarto.getSetor().getTipoSetor()+ " para " + this.quarto.getSetor().getTipoSetor()+ ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
            
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("quarto");
        this.log.setIdObjeto(this.quarto.getIdQuarto());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("quarto");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("quarto", this.quarto.getIdQuarto());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioQuarto";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.quartos);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * @return the quarto
     */
    public Quarto getQuarto() {
        return quarto;
    }

    /**
     * @param quarto the quarto to set
     */
    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    /**
     * @return the filtrarLista
     */
    public List<Quarto> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Quarto> filtrarLista) {
        this.filtrarLista = filtrarLista;
    }

    /**
     * @return the setores
     */
    public List<Setor> getSetores() {
        return setores;
    }

    /**
     * @return the quartos
     */
    public List<Quarto> getQuartos() {
        return quartos;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
