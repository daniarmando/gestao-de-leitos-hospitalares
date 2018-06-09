/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.AltaDAOImpl;
import br.com.gestaohospitalar.nir.DAO.AltaQualificadaDAOImpl;
import br.com.gestaohospitalar.nir.DAO.InternacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.MedicoDAOImpl;
import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Alta;
import br.com.gestaohospitalar.nir.model.Internacao;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Medico;
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
public class AltaBean implements Serializable, InterfaceBean {

    private Alta alta, cloneAlta;
    private AltaDAOImpl daoAlta;

    private List<Alta> altas, altasFiltradas;

    private List<Internacao> internacoes;

    private List<Medico> medicos;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    private Boolean habilitaCampos;

    /**
     * Creates a new instance of AltaBean
     */
    public AltaBean() {
        this.alta = new Alta();
    }

    @Override
    public void inicializarConsulta() {
        this.daoAlta = new AltaDAOImpl();
        this.altas = this.daoAlta.todas();
    }

    @Override
    public void inicializarCadastro() {

        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.log = new Log(TipoLog.REGISTRAR_ALTA.get());
            
            this.internacoes = new InternacaoDAOImpl().paraAlta();
            this.habilitaCampos = !this.internacoes.isEmpty();
            if (this.habilitaCampos) {
                this.medicos = new MedicoDAOImpl().ativos();
            }
        }
    }

    @Override
    public void inicializarEdicao() {
        this.daoAlta = new AltaDAOImpl();
        this.log = new Log(TipoLog.ALTERAR_ALTA.get());
        
        this.alta = this.daoAlta.porId(this.alta.getIdAlta());
        this.cloneAlta = this.alta.clone();
        this.medicos = new MedicoDAOImpl().ativos();
        this.habilitaCampos = true;
    }

    @Override
    public void salvar() {
        this.daoAlta = new AltaDAOImpl();

        try {

            //se a data da alta informada for igual que a data de entrada da internação
            if (this.alta.getDataHoraRealizacao().compareTo(this.alta.getInternacao().getDataEntrada()) == 0) {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "A data da Alta não pode ser igual que a data de entrada da Internação!");
                //se a data da previsão de alta informada for menor que a data de entrada da internação
            } else if (this.alta.getDataHoraRealizacao().before(this.alta.getInternacao().getDataEntrada())) {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "A data da Alta não pode ser menor que a data de entrada da Internação!");
            } else {

                //passando a alta qualificada para a alta
                this.alta.setAltaQualificada(new AltaQualificadaDAOImpl().porIdInternacao(this.alta.getInternacao().getIdInternacao()));

                //passando a data e hora atual
                this.alta.setDataHoraInformacao(new Date());

                //salvando a alta
                this.daoAlta.salvar(this.alta);

                //atualizando a lista de internações
                this.internacoes.remove(this.alta.getInternacao());
                this.habilitaCampos = !this.internacoes.isEmpty();

                //salvando o log
                this.salvarLog();

                this.alta = new Alta();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Alta salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }

    }

    @Override
    public Boolean isEditar() {
        return this.alta.getIdAlta() != null;
    }

    @Override
    public void excluir() {
        this.daoAlta = new AltaDAOImpl();

        try {

            //verificando se pode excluir 
            if (new InternacaoDAOImpl().houveSaida(this.alta.getInternacao().getIdInternacao()) == false) {

                //excluindo alta 
                this.daoAlta.excluir(this.alta);

                //salvando o log
                this.log = new Log(TipoLog.EXCLUIR_ALTA.get());
                this.salvarLog();

                this.inicializarConsulta();

                this.alta = new Alta();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Alta excluída com sucesso!");

            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Alta não pode ser excluída, pois já foi registrado Saída do Paciente do Leito!");
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
        if (this.log.getTipo().equals(TipoLog.ALTERAR_ALTA.get())) {
            detalhe = "";

            detalhe += "alta código " + this.alta.getIdAlta() + " alterada:";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.alta.getDataHoraRealizacao().equals(this.cloneAlta.getDataHoraRealizacao())) {
                detalhe += " data e hora da alta de " + NIRDataUtil.formatarDataHora(this.cloneAlta.getDataHoraRealizacao()) + " para " + NIRDataUtil.formatarDataHora(this.alta.getDataHoraRealizacao()) + ",";
            }

            if (!this.alta.getMedico().getNomePessoa().equals(this.cloneAlta.getMedico().getNomePessoa())) {
                detalhe += " médico da alta de " + this.cloneAlta.getMedico().getNomePessoa() + " para " + this.alta.getMedico().getNomePessoa() + ",";
            }
        } else {
            detalhe += "alta código " + this.alta.getIdAlta() + ",";
        }

        //removendo última vírgula e adicionando ponto final
        detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("internacao");
        this.log.setIdObjeto(this.alta.getInternacao().getIdInternacao());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("internacao");
        return this.log != null ? "Última modificação em processo de internação feita em " + NIRDataUtil.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("internacao", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioAlta";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.altas);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public Alta getAlta() {
        return alta;
    }

    public void setAlta(Alta alta) {
        this.alta = alta;
    }
    
    public List<Alta> getAltas() {
        return altas;
    }

    public List<Alta> getAltasFiltradas() {
        return altasFiltradas;
    }

    public void setAltasFiltradas(List<Alta> altasFiltradas) {
        this.altasFiltradas = altasFiltradas;
    }

    public List<Internacao> getInternacoes() {
        return internacoes;
    }

    public Boolean getHabilitaCampos() {
        return habilitaCampos;
    }

    public List<Medico> getMedicos() {
        return medicos;
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
