/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public class FuncionarioBean implements InterfaceBean, Serializable {

    private FuncionarioDAOImpl daoFuncionario;
    private Funcionario funcionario;
    private List<Funcionario> funcionarios = new ArrayList<>();
    private List<Funcionario> filtrarLista;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;
    private Estado estado;
    private Cidade cidade;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs = new ArrayList<>();

    private Funcionario cloneFuncionario;

    /**
     * Creates a new instance of FuncionarioBean
     */
    public FuncionarioBean() {
        funcionario = new Funcionario();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.funcionarios = new FuncionarioDAOImpl().listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();

        if (isEditar()) {
            this.cidades = this.daoEstadoCidade.listarCidades(this.funcionario.getEstado());
            this.cloneFuncionario = this.funcionario.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.estados = this.daoEstadoCidade.listarEstados();

    }

    @Override
    public String novo() {
        this.funcionario = new Funcionario();
        return "cadastro-funcionario?faces-redirect=true";
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
                salvarLog();

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

            //atualizando a lista de Funicionários
            this.funcionarios.remove(this.funcionario);

            //salvando o log
            this.log.setTipo(TipoLog.INATIVACAO.get());
            salvarLog();

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
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = new LogDAOImpl().ultimoLogPorObjeto("funcionario");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = new LogDAOImpl().listarPorIdObjeto("funcionario", this.funcionario.getIdPessoa());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioFuncionario";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.funcionarios);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Funcionario clone() {
        Funcionario clone = new Funcionario();
        clone.setIdPessoa(this.funcionario.getIdPessoa());
        clone.setCpfPessoa(this.funcionario.getCpfPessoa());

        return clone;
    }

    /**
     * Método que aciona o método que lista cidades passando o id do estado para
     * exibir na página
     *
     * @param event
     */
    public void listaCidades(AjaxBehaviorEvent event) {
        this.cidades = new EstadoCidadeDAOImpl().listarCidades(this.funcionario.getEstado());
    }

    /**
     * @return the funcionario
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the filtrarLista
     */
    public List<Funcionario> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Funcionario> filtrarLista) {
        this.filtrarLista = filtrarLista;
    }

    /**
     * @return the estados
     */
    public List<Estado> getEstados() {
        return estados;
    }

    /**
     * @param estados the estados to set
     */
    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    /**
     * @return the cidades
     */
    public List<Cidade> getCidades() {
        return cidades;
    }

    /**
     * @param cidades the cidades to set
     */
    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    /**
     * @return the estado
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * @return the cidade
     */
    public Cidade getCidade() {
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    /**
     * método pega a data atual e subtrai 18 anos mostrar no calendário a data
     * máxima para escolher a data de nascimento
     *
     * @return now.getTime()
     */
    public Date getMaxDate() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, - 18);

        return now.getTime();
    }

    /**
     * @return the funcionarios
     */
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
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
