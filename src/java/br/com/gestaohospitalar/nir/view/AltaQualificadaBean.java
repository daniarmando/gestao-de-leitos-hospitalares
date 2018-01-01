/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.AltaQualificadaDAOImpl;
import br.com.gestaohospitalar.nir.DAO.InternacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.AltaQualificada;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Log;
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
public class AltaQualificadaBean implements Serializable, InterfaceBean {

    private AltaQualificada altaQualificada, cloneAltaQualificada;
    private AltaQualificadaDAOImpl daoAltaQualificada;

    private List<AltaQualificada> altasQualificadas, AltasQualificadasFiltradas;

    private List<Internacao> internacoes = new ArrayList<>();

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    private Boolean habilitaCampos;

    /**
     * Creates a new instance of AltaQualificadaBean
     */
    public AltaQualificadaBean() {
        this.altaQualificada = new AltaQualificada();
    }

    @Override
    public void inicializarConsulta() {
        this.daoAltaQualificada = new AltaQualificadaDAOImpl();
        this.altasQualificadas = this.daoAltaQualificada.todas();
    }

    @Override
    public void inicializarCadastro() {

        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.log = new Log(TipoLog.REGISTRAR_ALTA_QUALIFICADA.get());
            
            this.internacoes = new InternacaoDAOImpl().paraAltaQualificada();
            this.habilitaCampos = !this.internacoes.isEmpty();            
        }
    }

    @Override
    public void inicializarEdicao() {
        this.daoAltaQualificada = new AltaQualificadaDAOImpl();
        this.log = new Log(TipoLog.ALTERAR_ALTA_QUALIFICADA.get());
        
        this.altaQualificada = this.daoAltaQualificada.porId(this.altaQualificada.getIdAltaQualificada());
        this.cloneAltaQualificada = this.altaQualificada.clone();
        this.habilitaCampos = true;
    }

    @Override
    public void salvar() {
        this.daoAltaQualificada = new AltaQualificadaDAOImpl();

        try {

            //se a data da previsão de alta informada for igual que a data de entrada da internação
            if (this.altaQualificada.getDataHoraPrevisao().compareTo(this.altaQualificada.getInternacao().getDataEntrada()) == 0) {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "A Data da Previsão de Alta não pode ser igual que a Data de Entrada da Internação!");
                //se a data da previsão de alta informada for menor que a data de entrada da internação
            } else if (this.altaQualificada.getDataHoraPrevisao().before(this.altaQualificada.getInternacao().getDataEntrada())) {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "A Data da Previsão de Alta não pode ser menor que a Data de Entrada da Internação!");
            } else {

                //passando a data e hora atual
                this.altaQualificada.setDataHoraInformacao(new Date());

                //salvando a alta qualificada
                this.daoAltaQualificada.salvar(this.altaQualificada);

                //atualizando a lista de internações
                this.internacoes.remove(this.altaQualificada.getInternacao());
                habilitaCampos = !this.internacoes.isEmpty();

                //salvando o log
                salvarLog();

                this.altaQualificada = new AltaQualificada();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Alta Qualificada salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.altaQualificada.getIdAltaQualificada() != null;
    }

    @Override
    public void excluir() {
        this.daoAltaQualificada = new AltaQualificadaDAOImpl();

        try {

            //verificando se pode excluir 
            if (this.daoAltaQualificada.temAlta(this.altaQualificada.getIdAltaQualificada()) == false) {

                //excluindo alta qualificada
                this.daoAltaQualificada.excluir(this.altaQualificada);

                //salvando o log
                this.log = new Log(TipoLog.EXCLUIR_ALTA_QUALIFICADA.get());
                this.salvarLog();

                this.inicializarConsulta();

                this.altaQualificada = new AltaQualificada();
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Alta Qualificada excluída com sucesso!");
            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Alta Qualificada não pode ser excluída, pois já foi registrado Alta para o Paciente!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public void salvarLog() {
        this.daoLog = new LogDAOImpl();
        String detalhe = "";

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERAR_ALTA_QUALIFICADA.get())) {
            detalhe = "";

            detalhe += "alta qualificada código " + this.altaQualificada.getIdAltaQualificada() + " alterada:";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.altaQualificada.getDataHoraPrevisao().equals(this.cloneAltaQualificada.getDataHoraPrevisao())) {
                detalhe += " data e hora da previsão de " + ConverterDataHora.formatarDataHora(this.cloneAltaQualificada.getDataHoraPrevisao()) + " para " + ConverterDataHora.formatarDataHora(this.altaQualificada.getDataHoraPrevisao()) + ",";
            }
        } else {
            detalhe += "alta qualificada código de " + this.altaQualificada.getIdAltaQualificada() + ",";
        }

        //removendo última vírgula e adicionando ponto final
        detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("internacao");
        this.log.setIdObjeto(this.altaQualificada.getInternacao().getIdInternacao());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("internacao");
        return this.log != null ? "Última modificação em processo de internação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("internacao", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioAltaQualificada";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.altasQualificadas);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public AltaQualificada getAltaQualificada() {
        return altaQualificada;
    }

    public void setAltaQualificada(AltaQualificada altaQualificada) {
        this.altaQualificada = altaQualificada;
    }
    
     public List<Internacao> getInternacoes() {
        return internacoes;
    }

    public List<AltaQualificada> getAltasQualificadasFiltradas() {
        return AltasQualificadasFiltradas;
    }

    public void setAltasQualificadasFiltradas(List<AltaQualificada> AltasQualificadasFiltradas) {
        this.AltasQualificadasFiltradas = AltasQualificadasFiltradas;
    }

    public List<AltaQualificada> getAltasQualificadas() {
        return altasQualificadas;
    }

    public Boolean getHabilitaCampos() {
        return habilitaCampos;
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
