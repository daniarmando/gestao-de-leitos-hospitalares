/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.ParametrosDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Parametros;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Daniel
 */
@ManagedBean
@RequestScoped
public class ParametrosBean implements Serializable {

    private ParametrosDAOImpl daoParametros;
    private Parametros parametros;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;

    /**
     * Creates a new instance of ParamProcedimentoBean
     */
    public ParametrosBean() {
        this.parametros = new Parametros();
    }

    @PostConstruct
    public void init() {
        this.daoParametros = new ParametrosDAOImpl();
        this.parametros = this.daoParametros.porId(this.usuario.getHospital().getIdHospital());
    }

    public void salvar(String tipoParametro) {
        this.daoParametros = new ParametrosDAOImpl();
        this.log = new Log();
        String msg = "";

        try {

            //salvando o parâmetro
            this.daoParametros.salvar(this.parametros);

            switch (tipoParametro) {
                case "leitoPaciente":

                    this.log.setDetalhe("parâmetro: liberar internação em leito incompatível com idade e/ou sexo do paciente.");

                    if (this.parametros.getLeitoIncompativelPaciente()) {
                        msg = "Parâmetro: liberar internação em leito incompatível com idade e/ou sexo do paciente ativado.";
                        this.log.setTipo(TipoLog.ATIVACAO.get());
                    } else {
                        msg = "Parâmetro: liberar internação em leito incompatível com idade e/ou sexo do paciente desativado.";
                        this.log.setTipo(TipoLog.INATIVACAO.get());
                    }
                    break;

                case "procedimentoLeito":

                    this.log.setDetalhe("parâmetro: liberar internação com procedimento incompatível com leito.");

                    if (this.parametros.getProcedimentoIncompativelLeito()) {
                        msg = "Parâmetro: liberar internação com procedimento incompatível com leito ativado.";
                        this.log.setTipo(TipoLog.ATIVACAO.get());
                    } else {
                        msg = "Parâmetro: liberar internação com procedimento incompatível com leito desativado.";
                        this.log.setTipo(TipoLog.INATIVACAO.get());
                    }
                    break;

                case "procedimentoPaciente":

                    this.log.setDetalhe("parâmetro: liberar internação com procedimento incompatível com idade e/ou sexo do paciente.");

                    if (this.parametros.getProcedimentoIncompativelPaciente()) {
                        msg = "Parâmetro: liberar internação com procedimento incompatível com idade e/ou sexo do paciente ativado.";
                        this.log.setTipo(TipoLog.ATIVACAO.get());
                    } else {
                        msg = "Parâmetro: liberar internação com procedimento incompatível com idade e/ou sexo do paciente desativado.";
                        this.log.setTipo(TipoLog.INATIVACAO.get());
                    }
                    break;

                case "cidLeito":

                    this.log.setDetalhe("parâmetro: liberar internação com cid incompatível com tipo sexo do leito.");

                    if (this.parametros.getProcedimentoIncompativelPaciente()) {
                        msg = "Parâmetro: liberar internação com cid incompatível com tipo sexo do leito ativado.";
                        this.log.setTipo(TipoLog.ATIVACAO.get());
                    } else {
                        msg = "Parâmetro: liberar internação com cid incompatível com tipo sexo do leito desativado.";
                        this.log.setTipo(TipoLog.INATIVACAO.get());
                    }
                    break;

                case "cidPaciente":

                    this.log.setDetalhe("parâmetro: liberar internação com cid incompatível com sexo do paciente.");

                    if (this.parametros.getProcedimentoIncompativelPaciente()) {
                        msg = "Parâmetro: liberar internação com cid incompatível com sexo do paciente ativado.";
                        this.log.setTipo(TipoLog.ATIVACAO.get());
                    } else {
                        msg = "Parâmetro: liberar internação com cid incompatível com sexo do paciente desativado.";
                        this.log.setTipo(TipoLog.INATIVACAO.get());
                    }
                    break;
            }

            this.salvarLog();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, msg);

        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void salvarLog() {
        this.daoLog = new LogDAOImpl();

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setObjeto("parametros");
        this.log.setIdObjeto(this.parametros.getIdParametros());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("parametros");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    public Parametros getParametros() {
        return parametros;
    }

    public void setParametros(Parametros parametros) {
        this.parametros = parametros;
    }

    public UsuarioBean getUsuario() {
        return this.usuario;
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }

}
