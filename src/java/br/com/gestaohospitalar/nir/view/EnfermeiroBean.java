/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.EnfermeiroDAOImpl;
import br.com.gestaohospitalar.nir.DAO.GerenteEnfermagemDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Autorizacao;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Enfermeiro;
import br.com.gestaohospitalar.nir.model.GerenteEnfermagem;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.model.Usuario;
import br.com.gestaohospitalar.nir.model.enumerator.TipoUsuario;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import br.com.gestaohospitalar.nir.util.report.GerarRelatorio;
import br.com.gestaohospitalar.nir.validator.ConsultaCPFValidator;
import br.com.gestaohospitalar.nir.validator.ConsultaLoginValidator;
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
public class EnfermeiroBean implements InterfaceBean, Serializable {

    private EnfermeiroDAOImpl daoEnfermeiro;
    private Enfermeiro enfermeiro, cloneEnfermeiro;
    private List<Enfermeiro> enfermeiros, enfermeirosFiltrados;

    private UsuarioDAOImpl daoUsuarioEnfermeiro;
    private Usuario usuarioEnfermeiro, cloneUsuarioEnfermeiro;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;

    private List<GerenteEnfermagem> gerentesEnfermagem;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of Enfermeiro
     */
    public EnfermeiroBean() {
        this.enfermeiro = new Enfermeiro();
        this.usuarioEnfermeiro = new Usuario();
    }

    @Override
    public void inicializarConsulta() {
        this.daoEnfermeiro = new EnfermeiroDAOImpl();
        this.enfermeiros = this.daoEnfermeiro.ativos();
    }

    @Override
    public void inicializarCadastro() {

        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.daoEstadoCidade = new EstadoCidadeDAOImpl();
            this.log = new Log(TipoLog.INCLUSAO.get());

            this.gerentesEnfermagem = new GerenteEnfermagemDAOImpl().ativos();
            this.estados = this.daoEstadoCidade.todosEstados();            
        }
    }

    @Override
    public void inicializarEdicao() {
        this.daoEnfermeiro = new EnfermeiroDAOImpl();
        this.daoUsuarioEnfermeiro = new UsuarioDAOImpl();
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());

        this.enfermeiro = this.daoEnfermeiro.porId(this.enfermeiro.getIdPessoa());
        this.usuarioEnfermeiro = daoUsuarioEnfermeiro.porIdPessoa(this.enfermeiro.getIdPessoa());
        this.cloneEnfermeiro = this.enfermeiro.clone();
        this.cloneUsuarioEnfermeiro = this.usuarioEnfermeiro.clone();
        this.gerentesEnfermagem = new GerenteEnfermagemDAOImpl().ativos();
        this.estados = this.daoEstadoCidade.todosEstados();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.enfermeiro.getEstado());        
    }

    @Override
    public void salvar() {
        this.daoEnfermeiro = new EnfermeiroDAOImpl();
        this.daoUsuarioEnfermeiro = new UsuarioDAOImpl();

        try {

            //verificando CPF e login
            if (ConsultaCPFValidator.verificar(this.enfermeiro, this.cloneEnfermeiro) && ConsultaLoginValidator.verificar(this.usuarioEnfermeiro, this.cloneUsuarioEnfermeiro)) {
                //se a data de nascimento informada for maior que a data mínima de 18 anos
                if (enfermeiro.getDataNascimentoPessoa().after(getMaxDate())) {
                    FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Data de Nascimento deve ser menor que " + getMaxDate() + ", pois só pode ser cadastrado se for maior de 18 anos.");
                } else {

                    this.enfermeiro.setStatusEnfermeiro(Status.ATIVO.get());
                    this.enfermeiro.setStatusPessoa(Status.ATIVO.get());

                    //salvando Enfermeiro
                    this.daoEnfermeiro.salvar(this.enfermeiro);

                    List<Autorizacao> autorizacoes = new ArrayList<>();
                    Autorizacao autorizacao = new Autorizacao();
                    autorizacao.setNome("ROLE_enf");
                    autorizacoes.add(autorizacao);

                    this.usuarioEnfermeiro.setAutorizacoes(autorizacoes);
                    this.usuarioEnfermeiro.setTipo(TipoUsuario.ENFERMEIRO.get());
                    this.usuarioEnfermeiro.setStatus(true);
                    this.usuarioEnfermeiro.setPessoa(this.enfermeiro);

                    //salvando usuário
                    this.daoUsuarioEnfermeiro.salvar(this.usuarioEnfermeiro);

                    //gravando o log
                    this.salvarLog();

                    this.enfermeiro = new Enfermeiro();
                    this.usuarioEnfermeiro = new Usuario();

                    FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Enfermeiro salvo com sucesso!");
                }
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.enfermeiro.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.daoEnfermeiro = new EnfermeiroDAOImpl();
        this.daoUsuarioEnfermeiro = new UsuarioDAOImpl();

        try {

            this.enfermeiro.setStatusPessoa(Status.INATIVO.get());
            this.enfermeiro.setStatusEnfermeiro(Status.INATIVO.get());

            this.daoEnfermeiro.salvar(this.enfermeiro);

            //inativando o usuário
            this.usuarioEnfermeiro = this.daoUsuarioEnfermeiro.porIdPessoa(this.enfermeiro.getIdPessoa());
            this.usuarioEnfermeiro.setPessoa(enfermeiro);
            this.usuarioEnfermeiro.setStatus(false);
            this.daoUsuarioEnfermeiro.salvar(this.usuarioEnfermeiro);

            //gravando o log
            this.log = new Log(TipoLog.INATIVACAO.get());
            this.salvarLog();

            this.inicializarConsulta();

            this.enfermeiro = new Enfermeiro();
            this.usuarioEnfermeiro = new Usuario();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Enfermeiro inativado com sucesso!");

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
            if (!this.enfermeiro.getNomePessoa().equals(this.cloneEnfermeiro.getNomePessoa())) {
                detalhe += " nome de " + this.cloneEnfermeiro.getNomePessoa() + " para " + this.enfermeiro.getNomePessoa() + ",";
            }

            if (!this.enfermeiro.getCorenEnfermeiro().equals(this.cloneEnfermeiro.getCorenEnfermeiro())) {
                detalhe += " coren de " + this.cloneEnfermeiro.getCorenEnfermeiro() + " para " + this.enfermeiro.getCorenEnfermeiro() + ",";
            }

            if (!this.enfermeiro.getGerenteEnfermagem().getNomePessoa().equals(this.cloneEnfermeiro.getGerenteEnfermagem().getNomePessoa())) {
                detalhe += " gerente de " + this.cloneEnfermeiro.getGerenteEnfermagem().getNomePessoa() + " para " + this.enfermeiro.getGerenteEnfermagem().getNomePessoa() + ",";
            }

            if (!this.enfermeiro.getCpfPessoa().equals(this.cloneEnfermeiro.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneEnfermeiro.getCpfPessoa() + " para " + this.enfermeiro.getCpfPessoa() + ",";
            }

            if (!this.enfermeiro.getRgPessoa().equals(this.cloneEnfermeiro.getRgPessoa())) {
                detalhe += " rg de " + this.cloneEnfermeiro.getRgPessoa() + " para " + this.enfermeiro.getRgPessoa() + ",";
            }

            if (!this.enfermeiro.getSexoPessoa().equals(this.cloneEnfermeiro.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneEnfermeiro.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + " para "
                        + (this.enfermeiro.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + ",";
            }

            if (!this.enfermeiro.getDataNascimentoPessoa().equals(this.cloneEnfermeiro.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + NIRDataUtil.formatarData(this.cloneEnfermeiro.getDataNascimentoPessoa()) + " para " + NIRDataUtil.formatarData(this.enfermeiro.getDataNascimentoPessoa()) + ",";
            }

            if (!this.enfermeiro.getTelefonePessoa().equals(this.cloneEnfermeiro.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneEnfermeiro.getTelefonePessoa() + " para " + this.enfermeiro.getTelefonePessoa() + ",";
            }

            if (!this.enfermeiro.getCelularPessoa().equals(this.cloneEnfermeiro.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneEnfermeiro.getCelularPessoa() + " para " + this.enfermeiro.getCelularPessoa() + ",";
            }

            if (!this.enfermeiro.getEnderecoPessoa().equals(this.cloneEnfermeiro.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneEnfermeiro.getEnderecoPessoa() + " para " + this.enfermeiro.getEnderecoPessoa() + ",";
            }

            if (!this.enfermeiro.getNumeroPessoa().equals(this.cloneEnfermeiro.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneEnfermeiro.getNumeroPessoa() + " para " + this.enfermeiro.getNumeroPessoa() + ",";
            }

            if (this.enfermeiro.getComplementoPessoa() != null) {
                if (!this.enfermeiro.getComplementoPessoa().equals(this.cloneEnfermeiro.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneEnfermeiro.getComplementoPessoa() + " para " + this.enfermeiro.getComplementoPessoa() + ",";
                }
            }

            if (!this.enfermeiro.getBairroPessoa().equals(this.cloneEnfermeiro.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneEnfermeiro.getBairroPessoa() + " para " + this.enfermeiro.getBairroPessoa() + ",";
            }

            if (!this.enfermeiro.getCepPessoa().equals(this.cloneEnfermeiro.getCepPessoa())) {
                detalhe += " cep de " + this.cloneEnfermeiro.getCepPessoa() + " para " + this.enfermeiro.getCepPessoa() + ",";
            }

            if (!this.enfermeiro.getEmailPessoa().equals(this.cloneEnfermeiro.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneEnfermeiro.getEmailPessoa() + " para " + this.enfermeiro.getEmailPessoa() + ",";
            }

            if (!this.enfermeiro.getStatusEnfermeiro().equals(this.cloneEnfermeiro.getStatusEnfermeiro())) {
                detalhe += " status de "
                        + (this.cloneEnfermeiro.getStatusEnfermeiro().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + " para "
                        + (this.enfermeiro.getStatusEnfermeiro().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + ",";
            }

            if (!this.enfermeiro.getEstado().getNomeEstado().equals(this.cloneEnfermeiro.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneEnfermeiro.getEstado().getNomeEstado() + " para " + this.enfermeiro.getEstado().getNomeEstado() + ",";
            }

            if (!this.enfermeiro.getCidade().getNomeCidade().equals(this.cloneEnfermeiro.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneEnfermeiro.getCidade().getNomeCidade() + " para " + this.enfermeiro.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("enfermeiro");
        this.log.setIdObjeto(this.enfermeiro.getIdPessoa());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("enfermeiro");
        return this.log != null ? "Última modificação feita em " + NIRDataUtil.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("enfermeiro", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioEnfermeiro";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.enfermeiros);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public void listaCidades(AjaxBehaviorEvent event) {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.enfermeiro.getEstado());
    }

    public Enfermeiro getEnfermeiro() {
        return enfermeiro;
    }

    public void setEnfermeiro(Enfermeiro enfermeiro) {
        this.enfermeiro = enfermeiro;
    }

    public List<Enfermeiro> getEnfermeirosFiltrados() {
        return enfermeirosFiltrados;
    }

    public void setEnfermeirosFiltrados(List<Enfermeiro> enfermeirosFiltrados) {
        this.enfermeirosFiltrados = enfermeirosFiltrados;
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

    public List<GerenteEnfermagem> getGerentesEnfermagem() {
        return gerentesEnfermagem;
    }

    public void setGerentesEnfermagem(List<GerenteEnfermagem> gerentesEnfermagem) {
        this.gerentesEnfermagem = gerentesEnfermagem;
    }

    public Date getMaxDate() {
        return NIRDataUtil.gerarDataMaiorDeIdade();
    }

    public List<Enfermeiro> getEnfermeiros() {
        return enfermeiros;
    }

    public Usuario getUsuarioEnfermeiro() {
        return usuarioEnfermeiro;
    }

    public void setUsuarioEnfermeiro(Usuario usuarioEnfermeiro) {
        this.usuarioEnfermeiro = usuarioEnfermeiro;
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
