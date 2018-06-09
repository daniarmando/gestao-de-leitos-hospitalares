/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.HospitalDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Hospital;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.io.Serializable;
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
public class HospitalBean implements InterfaceBean, Serializable {

    private HospitalDAOImpl daoHospital;
    private Hospital hospital, cloneHospital;    

    private List<Hospital> hospitais, hospitaisFiltrados;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of HospitalBean
     */
    public HospitalBean() {
        this.hospital = new Hospital();
    }

    @Override
    public void inicializarConsulta() {
        this.daoHospital = new HospitalDAOImpl();
        this.hospitais = daoHospital.ativos();
    }

    @Override
    public void inicializarCadastro() {
        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.log = new Log(TipoLog.INCLUSAO.get());
        }
    }

    @Override
    public void inicializarEdicao() {
        this.daoHospital = new HospitalDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());

        this.hospital = daoHospital.porId(this.hospital.getIdHospital());
        this.cloneHospital = this.hospital.clone();        
    }

    @Override
    public void salvar() {
        this.daoHospital = new HospitalDAOImpl();

        try {

            this.hospital.setStatusHospital(Status.ATIVO.get());

            this.daoHospital.salvar(this.hospital);

            //grava o log
            salvarLog();

            this.hospital = new Hospital();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Hospital salvo com sucesso!");

        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.hospital.getIdHospital() != null;
    }

    @Override
    public void excluir() {}

    @Override
    public void salvarLog() {
        this.daoLog = new LogDAOImpl();

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
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = daoLog.ultimoPorObjeto("hospital");
        return this.log != null ? "Última modificação feita em " + NIRDataUtil.formatarDataHora(this.getLog().getDataHora()) +
                                  " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.logs = new LogDAOImpl().porIdObjeto("hospital", idObjeto);
    }

    @Override
    public void gerarRelatorio() {}

    
    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
    
    public List<Hospital> getHospitais() {
        return hospitais;
    }

    public List<Hospital> getHospitaisFiltrados() {
        return hospitaisFiltrados;
    }

    public void setHospitaisFiltrados(List<Hospital> hospitaisFiltrados) {
        this.hospitaisFiltrados = hospitaisFiltrados;
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
