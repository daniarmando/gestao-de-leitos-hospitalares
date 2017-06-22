/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.HospitalDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Hospital;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.model.Usuario;
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
public class HospitalBean implements InterfaceBean, Serializable {

    private HospitalDAOImpl daoHospital = new HospitalDAOImpl();
    private Hospital hospital;
    private List<Hospital> listaHospital = new ArrayList<>();
    private List<Hospital> filtrarLista;

    private final UsuarioDAOImpl daoUsuario = new UsuarioDAOImpl();
    private Usuario usuario;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private Hospital cloneHospital;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    /**
     * Creates a new instance of HospitalBean
     */
    public HospitalBean() {
        hospital = new Hospital();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.listaHospital = this.daoHospital.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {

        this.log = new Log();

        if (isEditar()) {
            this.cloneHospital = this.hospital.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

    }

    @Override
    public String novo() {
        hospital = new Hospital();
        return "cadastro-hospital?faces-redirect=true";
    }

    @Override
    public void salvar() {
        this.hospital.setStatusHospital(Status.ATIVO.get());

        this.daoHospital.salvar(this.hospital);

        //gravando o log
        salvarLog();

        this.hospital = new Hospital();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Hospital salvo com sucesso!"));

    }

    @Override
    public Boolean isEditar() {
        return this.hospital.getIdHospital() != null;
    }

    @Override
    public void excluir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void salvarLog() {
        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.hospital.getNomeHospital().equals(this.cloneHospital.getNomeHospital())) {
                detalhe += " nome de " + this.cloneHospital.getNomeHospital() + " para " + this.hospital.getNomeHospital() + ",";
            }

            if (!this.hospital.getEmailHospital().equals(this.cloneHospital.getEmailHospital())) {
                detalhe += " e-mail de " + this.cloneHospital.getEmailHospital() + " para " + this.hospital.getEmailHospital() + ",";
            }

            if (!this.hospital.getTelefoneHospital().equals(this.cloneHospital.getTelefoneHospital())) {
                detalhe += " telefone de " + this.cloneHospital.getTelefoneHospital() + " para " + this.hospital.getTelefoneHospital() + ",";
            }

            if (!this.hospital.getSiteHospital().equals(this.cloneHospital.getSiteHospital())) {
                detalhe += " site de " + this.cloneHospital.getSiteHospital() + " para " + this.hospital.getSiteHospital() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("hospital");
        this.log.setIdObjeto(this.hospital.getIdHospital());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("hospital");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("hospital", this.hospital.getIdHospital());
    }

    @Override
    public void gerarRelatorio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.  
    }

    /**
     * @return the hospital
     */
    public Hospital getHospital() {
        return hospital;
    }

    /**
     * @param hospital the hospital to set
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public List<Hospital> getFiltrarLista() {
        return filtrarLista;
    }

    public void setFiltrarLista(List<Hospital> filtrarLista) {
        this.filtrarLista = filtrarLista;
    }

    /**
     * @return the listaHospital
     */
    public List<Hospital> getListaHospital() {
        return listaHospital;
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
