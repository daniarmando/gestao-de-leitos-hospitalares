/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.QuartoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SetorDAOImpl;
import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.Quarto;
import br.com.gestaohospitalar.nir.model.Setor;
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
public class QuartoBean implements InterfaceBean, Serializable {

    private QuartoDAOImpl daoQuarto;
    private Quarto quarto, cloneQuarto;

    private List<Quarto> quartos, quartosFiltrados;

    private List<Setor> setores;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of QuartoBean
     */
    public QuartoBean() {
        this.quarto = new Quarto();
    }

    @Override
    public void inicializarConsulta() {
        this.daoQuarto = new QuartoDAOImpl();
        this.quartos = daoQuarto.ativos();
    }

    @Override
    public void inicializarCadastro() {
        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.log = new Log(TipoLog.INCLUSAO.get());
        }

        this.setores = new SetorDAOImpl().ativos();
    }

    @Override
    public void inicializarEdicao() {
        this.daoQuarto = new QuartoDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());
        
        this.quarto = this.daoQuarto.porId(this.quarto.getIdQuarto());
        this.cloneQuarto = this.quarto.clone();
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

                //gravando o log
                this.log = new Log(TipoLog.INATIVACAO.get());
                this.salvarLog();
                
                this.inicializarConsulta();

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
            if (!this.quarto.getDescricaoQuarto().equals(this.cloneQuarto.getDescricaoQuarto())) {
                detalhe += " descrição de " + this.cloneQuarto.getDescricaoQuarto() + " para " + this.quarto.getDescricaoQuarto() + ",";
            }

            if (!this.quarto.getStatusQuarto().equals(this.cloneQuarto.getStatusQuarto())) {
                detalhe += " status de "
                        + (this.cloneQuarto.getStatusQuarto().equals(Status.ATIVO.get()) ? " ativo " : this.cloneQuarto.getStatusQuarto().equals(Status.INATIVO.get()) ? " inativo " : " manutenção ")
                        + " para "
                        + (this.quarto.getStatusQuarto().equals(Status.ATIVO.get()) ? " ativo " : this.cloneQuarto.getStatusQuarto().equals(Status.INATIVO.get()) ? " inativo " : " manutenção ")
                        + ",";
            }

            if (!this.quarto.getSetor().getDescricaoSetor().equals(this.cloneQuarto.getSetor().getDescricaoSetor())) {
                detalhe += " setor de " + this.cloneQuarto.getSetor().getDescricaoSetor() + " para " + this.quarto.getSetor().getDescricaoSetor() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("quarto");
        this.log.setIdObjeto(this.quarto.getIdQuarto());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("quarto");
        return this.log != null ? "Última modificação feita em " + NIRDataUtil.formatarDataHora(this.log.getDataHora()) +
                                  " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("quarto", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioQuarto";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.quartos);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }
    
    public List<Quarto> getQuartos() {
        return quartos;
    }

    public List<Quarto> getQuartosFiltrados() {
        return quartosFiltrados;
    }

    public void setQuartosFiltrados(List<Quarto> quartosFiltrados) {
        this.quartosFiltrados = quartosFiltrados;
    }

    public List<Setor> getSetores() {
        return setores;
    }

    public UsuarioBean getUsuario() {
        return usuario;
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
