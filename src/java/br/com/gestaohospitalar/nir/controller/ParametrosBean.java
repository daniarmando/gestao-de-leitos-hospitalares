/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.ParametrosDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Parametros;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Daniel
 */
@ManagedBean
@RequestScoped
public class ParametrosBean implements Serializable {

    private ParametrosDAOImpl daoParametros = new ParametrosDAOImpl();
    private Parametros parametros;

    //Injetando o usuário
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;

    /**
     * Creates a new instance of ParamProcedimentoBean
     */
    public ParametrosBean() {
        parametros = new Parametros();
    }

    @PostConstruct
    public void init() {
        this.parametros = this.daoParametros.parametrosPorId(1); //passando o id manualmente
    }

    /**
     * método que envia o objeto para o método respectivo na camada dao para
     * salvar no banco de dados
     *
     * @param tipoParametro
     */
    public void salvar(String tipoParametro) {
        String msg = "";

        //salvando o parâmetro
        this.daoParametros.salvar(this.getParametros());

        this.log = new Log();

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

        salvarLog();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    /**
     * método que monta e salva o log
     *
     */
    public void salvarLog() {
        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setObjeto("parametros");
        this.log.setIdObjeto(this.parametros.getIdParametros());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    /**
     * método que exibe o último log na página
     *
     * @return
     */
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("parametros");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    /**
     * @return the parametros
     */
    public Parametros getParametros() {
        return parametros;
    }

    /**
     * @param parametros the parametros to set
     */
    public void setParametros(Parametros parametros) {
        this.parametros = parametros;
    }

    //setter necessario para a anotação @ManagedProperty funcionar corretamente
    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

}
