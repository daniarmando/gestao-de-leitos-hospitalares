/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.HospitalDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SetorDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Hospital;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Setor;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
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
public class SetorBean implements InterfaceBean, Serializable {

    private SetorDAOImpl daoSetor = new SetorDAOImpl();
    private Setor setor;
    private List<Setor> setores = new ArrayList<>();
    private List<Setor> filtrarLista;

    private HospitalDAOImpl daoHospital = new HospitalDAOImpl();
    private List<Hospital> hospitais = new ArrayList<>();

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private Setor cloneSetor;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    /**
     * Creates a new instance of Enfermeiro
     */
    public SetorBean() {
        setor = new Setor();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.setores = this.daoSetor.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        
        this.log = new Log();

        if (isEditar()) {
            this.cloneSetor = this.setor.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.hospitais = this.daoHospital.listar();

    }

    @Override
    public String novo() {
        setSetor(new Setor());
        return "cadastro-setor?faces-redirect=true";
    }

    @Override
    public void salvar() {
        this.setor.setStatusSetor(Status.ATIVO.get());

        this.daoSetor.salvar(this.getSetor());

        //salvando o log
        salvarLog();

        this.setor = new Setor();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Setor salvo com sucesso!"));

    }

    @Override
    public Boolean isEditar() {
        return this.setor.getIdSetor() != null;
    }

    @Override
    public void excluir() {
        if (daoSetor.verificarSePossuiQuarto(this.setor) == false) {
            this.setor.setStatusSetor(Status.INATIVO.get());

            this.daoSetor.salvar(this.setor);

            //atualizando lista de setores
            this.setores.remove(this.setor);

            //salvando o log
            this.log.setTipo(TipoLog.INATIVACAO.get());
            salvarLog();

            this.setor = new Setor();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Setor Inativado com sucesso!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Setor não pode ser Inativado, pois possuí Quarto(s) cadastrado(s)!", null));
        }

    }

    @Override
    public void salvarLog() {
        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.setor.getTipoSetor().equals(this.cloneSetor.getTipoSetor())) {
                detalhe += " tipo de " + this.cloneSetor.getTipoSetor() + " para " + this.setor.getTipoSetor() + ",";
            }

            if (!this.setor.getStatusSetor().equals(this.cloneSetor.getStatusSetor())) {
                detalhe += " status de "
                        + (this.cloneSetor.getStatusSetor().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + " para "
                        + (this.setor.getStatusSetor().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + ",";
            }

            if (!this.setor.getHospital().getNomeHospital().equals(this.cloneSetor.getHospital().getNomeHospital())) {
                detalhe += " hospital de " + this.cloneSetor.getHospital().getNomeHospital()+ " para " + this.setor.getHospital().getNomeHospital()+ ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("setor");
        this.log.setIdObjeto(this.setor.getIdSetor());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("setor");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("setor", this.setor.getIdSetor());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioSetor";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.setores);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * @return the setor
     */
    public Setor getSetor() {
        return setor;
    }

    /**
     * @param setor the setor to set
     */
    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    /**
     * @return the filtrarLista
     */
    public List<Setor> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Setor> filtrarLista) {
        this.filtrarLista = filtrarLista;
    }

    /**
     * @return the hospitais
     */
    public List<Hospital> getHospitais() {
        return hospitais;
    }

    /**
     * @param hospitais the hospitais to set
     */
    public void setHospitais(List<Hospital> hospitais) {
        this.hospitais = hospitais;
    }

    /**
     * @return the setores
     */
    public List<Setor> getSetores() {
        return setores;
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
