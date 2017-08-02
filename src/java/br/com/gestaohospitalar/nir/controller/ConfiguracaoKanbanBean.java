/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.ConfiguracaoKanbanDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.ConfiguracaoKanban;
import br.com.gestaohospitalar.nir.model.Log;
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
public class ConfiguracaoKanbanBean implements Serializable {

    private ConfiguracaoKanban configuracaoKanban;
    private ConfiguracaoKanbanDAOImpl daoConfiguracaoKanban;

    //Injetando o usuário
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private LogDAOImpl daoLog;
    private Log log = new Log();

    private ConfiguracaoKanban cloneConfiguracaoKanban = new ConfiguracaoKanban();

    /**
     * Creates a new instance of ConfiguracaoKanbanBean
     */
    public ConfiguracaoKanbanBean() {
        this.configuracaoKanban = new ConfiguracaoKanban();
    }

    @PostConstruct
    public void init() {
        this.daoConfiguracaoKanban = new ConfiguracaoKanbanDAOImpl();
        this.configuracaoKanban = this.daoConfiguracaoKanban.configuracaoKanbanPorId(1);
        this.cloneConfiguracaoKanban = this.configuracaoKanban.clone();
    }

    /**
     * método que envia o objeto para o método respectivo na camada dao para
     * salvar no banco de dados
     *
     */
    public void salvar() {
        this.daoConfiguracaoKanban = new ConfiguracaoKanbanDAOImpl();

        try {

            if (this.configuracaoKanban.equals(this.cloneConfiguracaoKanban) == false) {

                //somando os valores
                int soma = this.configuracaoKanban.getValorVerdeKanban()
                        + this.configuracaoKanban.getValorAmareloKanban()
                        + this.configuracaoKanban.getValorVermelhoKanban();

                //verificando se os três campos foram preenchidos
                if (this.configuracaoKanban.getValorVerdeKanban() == 0
                        || this.configuracaoKanban.getValorVermelhoKanban() == 0
                        || this.configuracaoKanban.getValorVermelhoKanban() == 0) {
                    FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Favor preencher os três campos.");

                    //verificando se a soma dos valores corresponde a 100
                } else if (soma != 100) {
                    FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "A soma dos valores nos 3 campos deve ser igual a 100, a soma atual está com " + soma + ".");

                    //gravando a configuração kanban
                } else {
                    daoConfiguracaoKanban.salvar(configuracaoKanban);

                    //salvando o log
                    salvarLog();

                    this.cloneConfiguracaoKanban = this.configuracaoKanban.clone();
                    FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Configuração do Kanban salva com sucesso!");
                }

            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    /**
     * método que monta e salva o log
     *
     */
    public void salvarLog() {
        this.daoLog = new LogDAOImpl();
        String detalhe = "";

        if (!this.configuracaoKanban.getValorVerdeKanban().equals(this.cloneConfiguracaoKanban.getValorVerdeKanban())) {
            detalhe += " valor para cor verde de "
                    + this.cloneConfiguracaoKanban.getValorVerdeKanban()
                    + " para "
                    + this.configuracaoKanban.getValorVerdeKanban() + ",";
        }

        if (!this.configuracaoKanban.getValorAmareloKanban().equals(this.cloneConfiguracaoKanban.getValorAmareloKanban())) {
            detalhe += " valor para cor amarelo de "
                    + this.cloneConfiguracaoKanban.getValorAmareloKanban()
                    + " para "
                    + this.configuracaoKanban.getValorAmareloKanban() + ",";
        }

        if (!this.configuracaoKanban.getValorVermelhoKanban().equals(this.cloneConfiguracaoKanban.getValorVermelhoKanban())) {
            detalhe += " valor para cor vermelho de "
                    + this.cloneConfiguracaoKanban.getValorVermelhoKanban()
                    + " para "
                    + this.configuracaoKanban.getValorVermelhoKanban() + ",";
        }

        if (detalhe.length() > 0) {
            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
        }

        //passando as informações 
        this.log.setTipo(TipoLog.ALTERACAO.get());
        this.log.setDetalhe(detalhe);
        this.log.setDataHora(new Date());
        this.log.setObjeto("configuracaoKanban");
        this.log.setIdObjeto(this.configuracaoKanban.getIdConfiguracaoKanban());
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
        this.log = new LogDAOImpl().ultimoLogPorObjeto("configuracaoKanban");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    /**
     * @return the configuracaoKanban
     */
    public ConfiguracaoKanban getConfiguracaoKanban() {
        return configuracaoKanban;
    }

    /**
     * @param configuracaoKanban the configuracaoKanban to set
     */
    public void setConfiguracaoKanban(ConfiguracaoKanban configuracaoKanban) {
        this.configuracaoKanban = configuracaoKanban;
    }

    //setter necessario para a anotação @ManagedProperty funcionar corretamente
    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

}
