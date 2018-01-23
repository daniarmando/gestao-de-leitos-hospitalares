/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.HospitalDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SetorDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Hospital;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Setor;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
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
public class SetorBean implements InterfaceBean, Serializable {

    private SetorDAOImpl daoSetor;
    private Setor setor, cloneSetor;

    private List<Setor> setores, setoresFiltrados;

    private List<Hospital> hospitais;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;       

    /**
     * Creates a new instance of SetorBean
     */
    public SetorBean() {
        this.setor = new Setor();
    }

    @Override
    public void inicializarConsulta() {
        this.daoSetor = new SetorDAOImpl();
        this.setores = this.daoSetor.ativos();
    }

    @Override
    public void inicializarCadastro() {
        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.log = new Log(TipoLog.INCLUSAO.get());
        }

        HospitalDAOImpl daoHospital = new HospitalDAOImpl();
        this.hospitais = daoHospital.ativos();
    }

    @Override
    public void inicializarEdicao() {
        this.daoSetor = new SetorDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());
        
        this.setor = this.daoSetor.porId(this.setor.getIdSetor());
        this.cloneSetor = this.setor.clone();
    }

    @Override
    public void salvar() {
        this.daoSetor = new SetorDAOImpl();

        try {

            this.setor.setStatusSetor(Status.ATIVO.get());

            this.daoSetor.salvar(this.setor);

            //salvando o log
            salvarLog();

            this.setor = new Setor();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Setor salvo com sucesso!");

        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }

    }

    @Override
    public Boolean isEditar() {
        return this.setor.getIdSetor() != null;
    }
    
    @Override
    public void excluir() {
        this.daoSetor = new SetorDAOImpl();

        try {

            if (this.daoSetor.temQuarto(this.setor) == false) {
                setor.setStatusSetor(Status.INATIVO.get());

                this.daoSetor.salvar(setor);

                //salvando o log
                this.log = new Log(TipoLog.INATIVACAO.get());
                this.salvarLog();
                
                this.inicializarConsulta();
                
                this.setor = new Setor();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Setor inativado com sucesso!");

            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Setor não pode ser inativado, pois possuí Quarto(s) cadastrado(s)!");
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
                detalhe += " hospital de " + this.cloneSetor.getHospital().getNomeHospital() + " para " + this.setor.getHospital().getNomeHospital() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("setor");
        this.log.setIdObjeto(this.setor.getIdSetor());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("setor");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) +
                                  " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.logs = new LogDAOImpl().porIdObjeto("setor", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioSetor";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.setores);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }


    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public List<Setor> getSetoresFiltrados() {
        return setoresFiltrados;
    }

    public void setSetoresFiltrados(List<Setor> setoresFiltrados) {
        this.setoresFiltrados = setoresFiltrados;
    }
    
    public List<Setor> getSetores() {
        return setores;
    }

    public List<Hospital> getHospitais() {
        return hospitais;
    }

    public void setHospitais(List<Hospital> hospitais) {
        this.hospitais = hospitais;
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
