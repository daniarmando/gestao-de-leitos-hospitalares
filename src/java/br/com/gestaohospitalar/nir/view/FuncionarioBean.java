/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.FuncionarioDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Funcionario;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
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
public class FuncionarioBean implements InterfaceBean, Serializable {

    private FuncionarioDAOImpl daoFuncionario;
    private Funcionario funcionario, cloneFuncionario;
    private List<Funcionario> funcionarios, funcionariosFiltrados;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of FuncionarioBean
     */
    public FuncionarioBean() {
        this.funcionario = new Funcionario();
    }

    @Override
    public void inicializarConsulta() {
        this.daoFuncionario = new FuncionarioDAOImpl();
        this.funcionarios = this.daoFuncionario.ativos();
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
        this.daoFuncionario = new FuncionarioDAOImpl();
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());

        this.funcionario = this.daoFuncionario.porId(this.funcionario.getIdPessoa());
        this.cloneFuncionario = this.funcionario.clone();
        this.estados = this.daoEstadoCidade.todosEstados();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.funcionario.getEstado());               
    }

    @Override
    public void salvar() {
        this.daoFuncionario = new FuncionarioDAOImpl();

        try {

            //verificando CPF
            if (ConsultaCPFValidator.verificar(this.funcionario, this.cloneFuncionario)) {
                this.funcionario.setStatusFuncionario(Status.ATIVO.get());
                this.funcionario.setStatusPessoa(Status.ATIVO.get());

                this.daoFuncionario.salvar(this.funcionario);

                //gravando o log
                this.salvarLog();

                this.funcionario = new Funcionario();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Funcionário salvo com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.funcionario.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.daoFuncionario = new FuncionarioDAOImpl();

        try {

            this.funcionario.setStatusPessoa(Status.INATIVO.get());
            this.funcionario.setStatusFuncionario(Status.INATIVO.get());

            this.daoFuncionario.salvar(this.funcionario);

            //salvando o log
            this.log = new Log(TipoLog.INATIVACAO.get());
            this.salvarLog();
            
            this.inicializarConsulta();

            this.funcionario = new Funcionario();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Funcionário inativado com sucesso!");

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
            if (!this.funcionario.getNomePessoa().equals(this.cloneFuncionario.getNomePessoa())) {
                detalhe += " nome de " + this.cloneFuncionario.getNomePessoa() + " para " + this.funcionario.getNomePessoa() + ",";
            }

            if (!this.funcionario.getCpfPessoa().equals(this.cloneFuncionario.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneFuncionario.getCpfPessoa() + " para " + this.funcionario.getCpfPessoa() + ",";
            }

            if (!this.funcionario.getRgPessoa().equals(this.cloneFuncionario.getRgPessoa())) {
                detalhe += " rg de " + this.cloneFuncionario.getRgPessoa() + " para " + this.funcionario.getRgPessoa() + ",";
            }

            if (!this.funcionario.getSexoPessoa().equals(this.cloneFuncionario.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneFuncionario.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + " para "
                        + (this.funcionario.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + ",";
            }

            if (!this.funcionario.getDataNascimentoPessoa().equals(this.cloneFuncionario.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.cloneFuncionario.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.funcionario.getDataNascimentoPessoa()) + ",";
            }

            if (!this.funcionario.getTelefonePessoa().equals(this.cloneFuncionario.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneFuncionario.getTelefonePessoa() + " para " + this.funcionario.getTelefonePessoa() + ",";
            }

            if (!this.funcionario.getCelularPessoa().equals(this.cloneFuncionario.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneFuncionario.getCelularPessoa() + " para " + this.funcionario.getCelularPessoa() + ",";
            }

            if (!this.funcionario.getEnderecoPessoa().equals(this.cloneFuncionario.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneFuncionario.getEnderecoPessoa() + " para " + this.funcionario.getEnderecoPessoa() + ",";
            }

            if (!this.funcionario.getNumeroPessoa().equals(this.cloneFuncionario.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneFuncionario.getNumeroPessoa() + " para " + this.funcionario.getNumeroPessoa() + ",";
            }

            if (this.funcionario.getComplementoPessoa() != null) {
                if (!this.funcionario.getComplementoPessoa().equals(this.cloneFuncionario.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneFuncionario.getComplementoPessoa() + " para " + this.funcionario.getComplementoPessoa() + ",";
                }
            }

            if (!this.funcionario.getBairroPessoa().equals(this.cloneFuncionario.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneFuncionario.getBairroPessoa() + " para " + this.funcionario.getBairroPessoa() + ",";
            }

            if (!this.funcionario.getCepPessoa().equals(this.cloneFuncionario.getCepPessoa())) {
                detalhe += " cep de " + this.cloneFuncionario.getCepPessoa() + " para " + this.funcionario.getCepPessoa() + ",";
            }

            if (!this.funcionario.getEmailPessoa().equals(this.cloneFuncionario.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneFuncionario.getEmailPessoa() + " para " + this.funcionario.getEmailPessoa() + ",";
            }

            if (!this.funcionario.getStatusFuncionario().equals(this.cloneFuncionario.getStatusFuncionario())) {
                detalhe += " status de "
                        + (this.cloneFuncionario.getStatusFuncionario().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + " para "
                        + (this.funcionario.getStatusFuncionario().equals(Status.ATIVO.get()) ? " ativo " : "  inativo ")
                        + ",";
            }

            if (!this.funcionario.getEstado().getNomeEstado().equals(this.cloneFuncionario.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneFuncionario.getEstado().getNomeEstado() + " para " + this.funcionario.getEstado().getNomeEstado() + ",";
            }

            if (!this.funcionario.getCidade().getNomeCidade().equals(this.cloneFuncionario.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneFuncionario.getCidade().getNomeCidade() + " para " + this.funcionario.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("funcionario");
        this.log.setIdObjeto(this.funcionario.getIdPessoa());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("funcionario");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("funcionario", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioFuncionario";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.funcionarios);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public void listaCidades(AjaxBehaviorEvent event) {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.funcionario.getEstado());
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public List<Funcionario> getFuncionariosFiltrados() {
        return funcionariosFiltrados;
    }

    public void setFuncionariosFiltrados(List<Funcionario> funcionariosFiltrados) {
        this.funcionariosFiltrados = funcionariosFiltrados;
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

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
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
