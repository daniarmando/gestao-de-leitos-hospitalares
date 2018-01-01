/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.TerapeutaOcupacionalDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.TerapeutaOcupacional;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import br.com.gestaohospitalar.nir.util.report.GerarRelatorio;
import br.com.gestaohospitalar.nir.validator.ConsultaCPFValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Daniel
 */
@ManagedBean
@ViewScoped
public class TerapeutaOcupacionalBean implements InterfaceBean, Serializable {

    private TerapeutaOcupacionalDAOImpl daoTerapeutaOcupacional;
    private TerapeutaOcupacional terapeutaOcupacional, cloneTerapeutaOCupacional;
    private List<TerapeutaOcupacional> terapeutasOcupacionais, terapeutasOcupacionaisFiltrados;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of TerapeutaOcupacionalBean
     */
    public TerapeutaOcupacionalBean() {
        this.terapeutaOcupacional = new TerapeutaOcupacional();
    }

    @Override
    public void inicializarConsulta() {
        this.daoTerapeutaOcupacional = new TerapeutaOcupacionalDAOImpl();
        this.terapeutasOcupacionais = this.daoTerapeutaOcupacional.ativos();
    }

    @Override
    public void inicializarCadastro() {
        
        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.daoEstadoCidade = new EstadoCidadeDAOImpl();  
            this.log = new Log(TipoLog.INCLUSAO.get());
            
            this.estados = this.daoEstadoCidade.todosEstados();
        }
    }

    @Override
    public void inicializarEdicao() {
        this.daoTerapeutaOcupacional = new TerapeutaOcupacionalDAOImpl();
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());
        
        this.terapeutaOcupacional = this.daoTerapeutaOcupacional.porId(this.terapeutaOcupacional.getIdPessoa());
        this.cloneTerapeutaOCupacional = this.terapeutaOcupacional.clone();
        this.estados = this.daoEstadoCidade.todosEstados();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.terapeutaOcupacional.getEstado());
    }

    @Override
    public void salvar() {
        this.daoTerapeutaOcupacional = new TerapeutaOcupacionalDAOImpl();

        try {
            //verificando CPF
            if (ConsultaCPFValidator.verificar(this.terapeutaOcupacional, this.cloneTerapeutaOCupacional)) {
                this.terapeutaOcupacional.setStatusTerapeutaOcupacional(Status.ATIVO.get());
                this.terapeutaOcupacional.setStatusPessoa(Status.ATIVO.get());

                this.daoTerapeutaOcupacional.salvar(this.terapeutaOcupacional);

                //gravando o log
                this.salvarLog();

                this.terapeutaOcupacional = new TerapeutaOcupacional();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Terapeuta Ocupacional salvo com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.terapeutaOcupacional.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.daoTerapeutaOcupacional = new TerapeutaOcupacionalDAOImpl();

        try {

            this.terapeutaOcupacional.setStatusPessoa(Status.INATIVO.get());
            this.terapeutaOcupacional.setStatusTerapeutaOcupacional(Status.INATIVO.get());

            this.daoTerapeutaOcupacional.salvar(this.terapeutaOcupacional);

            //gravando o log
            this.log = new Log(TipoLog.INATIVACAO.get());
            this.salvarLog();
            
            this.inicializarConsulta();

            this.terapeutaOcupacional = new TerapeutaOcupacional();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Terapeuta Ocupacional inativado com sucesso!");

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
            if (!this.terapeutaOcupacional.getNomePessoa().equals(this.cloneTerapeutaOCupacional.getNomePessoa())) {
                detalhe += " nome de " + this.cloneTerapeutaOCupacional.getNomePessoa() + " para " + this.terapeutaOcupacional.getNomePessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getCrefitoTerapeutaOcupacional().equals(this.cloneTerapeutaOCupacional.getCrefitoTerapeutaOcupacional())) {
                detalhe += " crefito de " + this.cloneTerapeutaOCupacional.getCrefitoTerapeutaOcupacional() + " para " + this.terapeutaOcupacional.getCrefitoTerapeutaOcupacional() + ",";
            }

            if (!this.terapeutaOcupacional.getCpfPessoa().equals(this.cloneTerapeutaOCupacional.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneTerapeutaOCupacional.getCpfPessoa() + " para " + this.terapeutaOcupacional.getCpfPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getRgPessoa().equals(this.cloneTerapeutaOCupacional.getRgPessoa())) {
                detalhe += " rg de " + this.cloneTerapeutaOCupacional.getRgPessoa() + " para " + this.terapeutaOcupacional.getRgPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getSexoPessoa().equals(this.cloneTerapeutaOCupacional.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneTerapeutaOCupacional.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + " para "
                        + (this.terapeutaOcupacional.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + ",";
            }

            if (!this.terapeutaOcupacional.getDataNascimentoPessoa().equals(this.cloneTerapeutaOCupacional.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.cloneTerapeutaOCupacional.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.terapeutaOcupacional.getDataNascimentoPessoa()) + ",";
            }

            if (!this.terapeutaOcupacional.getTelefonePessoa().equals(this.cloneTerapeutaOCupacional.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneTerapeutaOCupacional.getTelefonePessoa() + " para " + this.terapeutaOcupacional.getTelefonePessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getCelularPessoa().equals(this.cloneTerapeutaOCupacional.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneTerapeutaOCupacional.getCelularPessoa() + " para " + this.terapeutaOcupacional.getCelularPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getEnderecoPessoa().equals(this.cloneTerapeutaOCupacional.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneTerapeutaOCupacional.getEnderecoPessoa() + " para " + this.terapeutaOcupacional.getEnderecoPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getNumeroPessoa().equals(this.cloneTerapeutaOCupacional.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneTerapeutaOCupacional.getNumeroPessoa() + " para " + this.terapeutaOcupacional.getNumeroPessoa() + ",";
            }

            if (this.terapeutaOcupacional.getComplementoPessoa() != null) {
                if (!this.terapeutaOcupacional.getComplementoPessoa().equals(this.cloneTerapeutaOCupacional.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneTerapeutaOCupacional.getComplementoPessoa() + " para " + this.terapeutaOcupacional.getComplementoPessoa() + ",";
                }
            }

            if (!this.terapeutaOcupacional.getBairroPessoa().equals(this.cloneTerapeutaOCupacional.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneTerapeutaOCupacional.getBairroPessoa() + " para " + this.terapeutaOcupacional.getBairroPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getCepPessoa().equals(this.cloneTerapeutaOCupacional.getCepPessoa())) {
                detalhe += " cep de " + this.cloneTerapeutaOCupacional.getCepPessoa() + " para " + this.terapeutaOcupacional.getCepPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getEmailPessoa().equals(this.cloneTerapeutaOCupacional.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneTerapeutaOCupacional.getEmailPessoa() + " para " + this.terapeutaOcupacional.getEmailPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getStatusTerapeutaOcupacional().equals(this.cloneTerapeutaOCupacional.getStatusTerapeutaOcupacional())) {
                detalhe += " status de "
                        + (this.cloneTerapeutaOCupacional.getStatusTerapeutaOcupacional().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + " para "
                        + (this.terapeutaOcupacional.getStatusTerapeutaOcupacional().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + ",";
            }

            if (!this.terapeutaOcupacional.getEstado().getNomeEstado().equals(this.cloneTerapeutaOCupacional.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneTerapeutaOCupacional.getEstado().getNomeEstado() + " para " + this.terapeutaOcupacional.getEstado().getNomeEstado() + ",";
            }

            if (!this.terapeutaOcupacional.getCidade().getNomeCidade().equals(this.cloneTerapeutaOCupacional.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneTerapeutaOCupacional.getCidade().getNomeCidade() + " para " + this.terapeutaOcupacional.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("terapeutaOcupacional");
        this.log.setIdObjeto(this.terapeutaOcupacional.getIdPessoa());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioTerapeutaOcupacional";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.terapeutasOcupacionais);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("terapeutaOcupacional");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("terapeutaOcupacional", idObjeto);
    }

    public void listaCidades(AjaxBehaviorEvent event) {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.terapeutaOcupacional.getEstado());
    }

    public TerapeutaOcupacional getTerapeutaOcupacional() {
        return terapeutaOcupacional;
    }

    public void setTerapeutaOcupacional(TerapeutaOcupacional terapeutaOcupacional) {
        this.terapeutaOcupacional = terapeutaOcupacional;
    }

    public List<TerapeutaOcupacional> getTerapeutasOcupacionaisFiltrados() {
        return terapeutasOcupacionaisFiltrados;
    }

    public void setTerapeutasOcupacionaisFiltrados(List<TerapeutaOcupacional> terapeutasOcupacionaisFiltrados) {
        this.terapeutasOcupacionaisFiltrados = terapeutasOcupacionaisFiltrados;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    public Date getMaxDate() {
        return ConverterDataHora.gerarDataMaiorDeIdade();
    }

    public List<TerapeutaOcupacional> getTerapeutasOcupacionais() {
        return terapeutasOcupacionais;
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
