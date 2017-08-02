/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.QuartoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SetorDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.Quarto;
import br.com.gestaohospitalar.nir.model.Setor;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.model.Usuario;
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
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public class QuartoBean implements InterfaceBean, Serializable {

    private QuartoDAOImpl daoQuarto;
    private Quarto quarto;
    private List<Quarto> quartos = new ArrayList<>();
    private List<Quarto> filtrarLista;

    private List<Setor> setores = new ArrayList<>();

    private Usuario usuario;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private Quarto cloneQuarto;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs = new ArrayList<>();

    /**
     * Creates a new instance of QuartoBean
     */
    public QuartoBean() {
        this.quarto = new Quarto();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.quartos = new QuartoDAOImpl().listar();
    }

    @Override
    public void inicializarPaginaCadastro() {

        if (isEditar()) {
            this.cloneQuarto = this.quarto.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.setores = new SetorDAOImpl().listar();
    }

    @Override
    public String novo() {
        this.quarto = new Quarto();
        return "cadastro-quarto?faces-redirect=true";
    }

    @Override
    public void salvar() {
        this.daoQuarto = new QuartoDAOImpl();

        try {

            this.quarto.setStatusQuarto(Status.ATIVO.get());
            this.daoQuarto.salvar(this.quarto);

            //gravando o log
            salvarLog();

            this.quarto = new Quarto();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Quarto salvo com sucesso!");

        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.quarto.getIdQuarto() != null;
    }

    @Override
    public void excluir() {
        this.daoQuarto = new QuartoDAOImpl();

        try {

            if (this.daoQuarto.temLeito(this.quarto) == false) {

                this.quarto.setStatusQuarto(Status.INATIVO.get());

                this.daoQuarto.salvar(this.quarto);

                //atualizando lista de quartos
                this.quartos.remove(this.quarto);

                //gravando o log
                this.log.setTipo(TipoLog.INATIVACAO.get());
                salvarLog();

                this.quarto = new Quarto();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Quarto inativado com sucesso!");
            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Quarto não pode ser inativado, pois possuí Leito(s) cadastrado(s)!");
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
                detalhe += " setor de " + this.cloneQuarto.getSetor().getTipoSetor() + " para " + this.quarto.getSetor().getTipoSetor() + ",";
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
        this.log = new LogDAOImpl().ultimoLogPorObjeto("quarto");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = new LogDAOImpl().listarPorIdObjeto("quarto", this.quarto.getIdQuarto());
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
