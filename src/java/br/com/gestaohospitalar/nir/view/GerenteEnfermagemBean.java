/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.GerenteEnfermagemDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Autorizacao;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
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
public class GerenteEnfermagemBean implements InterfaceBean, Serializable {

    private GerenteEnfermagemDAOImpl daoGerenteEnfermagem;
    private GerenteEnfermagem gerenteEnfermagem, cloneGerenteEnfermagem;
    private List<GerenteEnfermagem> gerentesEnfermagem, gerentesEnfermagemFiltrados;

    private UsuarioDAOImpl daoUsuarioGerenteEnfermagem;
    private Usuario usuarioGerenteEnfermagem, cloneUsuarioGerenteEnfermagem;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of GerenteEnfermagemBean
     */
    public GerenteEnfermagemBean() {
        this.gerenteEnfermagem = new GerenteEnfermagem();
        this.usuarioGerenteEnfermagem = new Usuario();
    }

    @Override
    public void inicializarConsulta() {
        this.daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();
        this.gerentesEnfermagem = this.daoGerenteEnfermagem.ativos();
    }

    @Override
    public void inicializarCadastro() {

        if (isEditar()) {
            this.inicializarEdicao();
        } else {
            this.daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();
            this.daoEstadoCidade = new EstadoCidadeDAOImpl();
            this.log = new Log(TipoLog.INCLUSAO.get());
            
            this.estados = this.daoEstadoCidade.todosEstados();
        }
    }

    @Override
    public void inicializarEdicao() {
        this.daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();
        this.daoUsuarioGerenteEnfermagem = new UsuarioDAOImpl();
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());
        
        this.gerenteEnfermagem = this.daoGerenteEnfermagem.porId(this.gerenteEnfermagem.getIdPessoa());
        this.usuarioGerenteEnfermagem = this.daoUsuarioGerenteEnfermagem.porIdPessoa(this.gerenteEnfermagem.getIdPessoa());
        this.cloneGerenteEnfermagem = this.gerenteEnfermagem.clone();
        this.cloneUsuarioGerenteEnfermagem = this.usuarioGerenteEnfermagem.clone();
        this.estados = this.daoEstadoCidade.todosEstados();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.gerenteEnfermagem.getEstado());
    }

    @Override
    public void salvar() {
        this.daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();
        this.daoUsuarioGerenteEnfermagem = new UsuarioDAOImpl();

        try {
            //verificando CPF e login
            if (ConsultaCPFValidator.verificar(this.gerenteEnfermagem, this.cloneGerenteEnfermagem) && ConsultaLoginValidator.verificar(this.usuarioGerenteEnfermagem, this.cloneUsuarioGerenteEnfermagem)) {
                //se a data de nascimento informada for maior que a data mínima de 18 anos
                if (this.gerenteEnfermagem.getDataNascimentoPessoa().after(getMaxDate())) {
                    FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Data de Nascimento deve ser menor que " + getMaxDate() + ", pois só pode ser cadastrado se for maior de 18 anos.");
                } else {

                    this.gerenteEnfermagem.setStatusGerenteEnfermagem(Status.ATIVO.get());
                    this.gerenteEnfermagem.setStatusPessoa(Status.ATIVO.get());

                    //salvando Gerente de Enfermagem
                    this.daoGerenteEnfermagem.salvar(this.gerenteEnfermagem);

                    List<Autorizacao> autorizacoes = new ArrayList<>();
                    Autorizacao autorizacao = new Autorizacao();
                    autorizacao.setNome("ROLE_gen");
                    autorizacoes.add(autorizacao);

                    this.usuarioGerenteEnfermagem.setAutorizacoes(autorizacoes);
                    this.usuarioGerenteEnfermagem.setTipo(TipoUsuario.GERENTE_ENFERMAGEM.get());
                    this.usuarioGerenteEnfermagem.setStatus(true);
                    this.usuarioGerenteEnfermagem.setPessoa(this.gerenteEnfermagem);

                    //salvando Usuário
                    this.daoUsuarioGerenteEnfermagem.salvar(this.usuarioGerenteEnfermagem);

                    //gravando o log
                    this.salvarLog();

                    this.gerenteEnfermagem = new GerenteEnfermagem();
                    this.usuarioGerenteEnfermagem = new Usuario();

                    FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Gerente de Enfermagem  salvo com sucesso!");
                }
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.gerenteEnfermagem.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();
        this.daoUsuarioGerenteEnfermagem = new UsuarioDAOImpl();

        try {

            if (this.daoGerenteEnfermagem.temEnfermeiro(this.gerenteEnfermagem) == false) {
                this.gerenteEnfermagem.setStatusPessoa(Status.INATIVO.get());
                this.gerenteEnfermagem.setStatusGerenteEnfermagem(Status.INATIVO.get());

                this.daoGerenteEnfermagem.salvar(this.gerenteEnfermagem);

                //inativando o usuário
                this.usuarioGerenteEnfermagem = this.daoUsuarioGerenteEnfermagem.porIdPessoa(this.gerenteEnfermagem.getIdPessoa());
                this.usuarioGerenteEnfermagem.setPessoa(gerenteEnfermagem);
                this.usuarioGerenteEnfermagem.setStatus(false);
                this.daoUsuarioGerenteEnfermagem.salvar(this.usuarioGerenteEnfermagem);

                //gravando o log
                this.log = new Log(TipoLog.INATIVACAO.get());
                this.salvarLog();

                this.gerenteEnfermagem = new GerenteEnfermagem();
                this.usuarioGerenteEnfermagem = new Usuario();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Gerente de Enfermagem inativado com sucesso!");

            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Gerente de Enfermagem não pode ser inativado, pois possuí Enfermeiro(s) vinculado(s)!");
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
            if (!this.gerenteEnfermagem.getNomePessoa().equals(this.cloneGerenteEnfermagem.getNomePessoa())) {
                detalhe += " nome de " + this.cloneGerenteEnfermagem.getNomePessoa() + " para " + this.gerenteEnfermagem.getNomePessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getCorenGerenteEnfermagem().equals(this.cloneGerenteEnfermagem.getCorenGerenteEnfermagem())) {
                detalhe += " coren de " + this.cloneGerenteEnfermagem.getCorenGerenteEnfermagem() + " para " + this.gerenteEnfermagem.getCorenGerenteEnfermagem() + ",";
            }

            if (!this.gerenteEnfermagem.getCpfPessoa().equals(this.cloneGerenteEnfermagem.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneGerenteEnfermagem.getCpfPessoa() + " para " + this.gerenteEnfermagem.getCpfPessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getRgPessoa().equals(this.cloneGerenteEnfermagem.getRgPessoa())) {
                detalhe += " rg de " + this.cloneGerenteEnfermagem.getRgPessoa() + " para " + this.gerenteEnfermagem.getRgPessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getSexoPessoa().equals(this.cloneGerenteEnfermagem.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneGerenteEnfermagem.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + " para "
                        + (this.gerenteEnfermagem.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + ",";
            }

            if (!this.gerenteEnfermagem.getDataNascimentoPessoa().equals(this.cloneGerenteEnfermagem.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.cloneGerenteEnfermagem.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.gerenteEnfermagem.getDataNascimentoPessoa()) + ",";
            }

            if (!this.gerenteEnfermagem.getTelefonePessoa().equals(this.cloneGerenteEnfermagem.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneGerenteEnfermagem.getTelefonePessoa() + " para " + this.gerenteEnfermagem.getTelefonePessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getCelularPessoa().equals(this.cloneGerenteEnfermagem.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneGerenteEnfermagem.getCelularPessoa() + " para " + this.gerenteEnfermagem.getCelularPessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getEnderecoPessoa().equals(this.cloneGerenteEnfermagem.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneGerenteEnfermagem.getEnderecoPessoa() + " para " + this.gerenteEnfermagem.getEnderecoPessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getNumeroPessoa().equals(this.cloneGerenteEnfermagem.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneGerenteEnfermagem.getNumeroPessoa() + " para " + this.gerenteEnfermagem.getNumeroPessoa() + ",";
            }

            if (this.gerenteEnfermagem.getComplementoPessoa() != null) {
                if (!this.gerenteEnfermagem.getComplementoPessoa().equals(this.cloneGerenteEnfermagem.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneGerenteEnfermagem.getComplementoPessoa() + " para " + this.gerenteEnfermagem.getComplementoPessoa() + ",";
                }
            }

            if (!this.gerenteEnfermagem.getBairroPessoa().equals(this.cloneGerenteEnfermagem.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneGerenteEnfermagem.getBairroPessoa() + " para " + this.gerenteEnfermagem.getBairroPessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getCepPessoa().equals(this.cloneGerenteEnfermagem.getCepPessoa())) {
                detalhe += " cep de " + this.cloneGerenteEnfermagem.getCepPessoa() + " para " + this.gerenteEnfermagem.getCepPessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getEmailPessoa().equals(this.cloneGerenteEnfermagem.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneGerenteEnfermagem.getEmailPessoa() + " para " + this.gerenteEnfermagem.getEmailPessoa() + ",";
            }

            if (!this.gerenteEnfermagem.getStatusGerenteEnfermagem().equals(this.cloneGerenteEnfermagem.getStatusGerenteEnfermagem())) {
                detalhe += " status de "
                        + (this.cloneGerenteEnfermagem.getStatusGerenteEnfermagem().equals(Status.ATIVO.get()) ? "ativo" : "inativo")
                        + " para "
                        + (this.gerenteEnfermagem.getStatusGerenteEnfermagem().equals(Status.ATIVO.get()) ? "ativo" : "inativo")
                        + ",";
            }

            if (!this.gerenteEnfermagem.getEstado().getNomeEstado().equals(this.cloneGerenteEnfermagem.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneGerenteEnfermagem.getEstado().getNomeEstado() + " para " + this.gerenteEnfermagem.getEstado().getNomeEstado() + ",";
            }

            if (!this.gerenteEnfermagem.getCidade().getNomeCidade().equals(this.cloneGerenteEnfermagem.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneGerenteEnfermagem.getCidade().getNomeCidade() + " para " + this.gerenteEnfermagem.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("gerenteEnfermagem");
        this.log.setIdObjeto(this.gerenteEnfermagem.getIdPessoa());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("gerenteEnfermagem");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("gerenteEnfermagem", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioGerenteEnfermagem";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.gerentesEnfermagem);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public void listaCidades(AjaxBehaviorEvent event) {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.gerenteEnfermagem.getEstado());
    }

    public GerenteEnfermagem getGerenteEnfermagem() {
        return gerenteEnfermagem;
    }

    public void setGerenteEnfermagem(GerenteEnfermagem gerenteEnfermagem) {
        this.gerenteEnfermagem = gerenteEnfermagem;
    }

    public List<GerenteEnfermagem> getGerentesEnfermagemFiltrados() {
        return gerentesEnfermagemFiltrados;
    }

    public void setGerentesEnfermagemFiltrados(List<GerenteEnfermagem> gerentesEnfermagemFiltrados) {
        this.gerentesEnfermagemFiltrados = gerentesEnfermagemFiltrados;
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

    public List<GerenteEnfermagem> getGerentesEnfermagem() {
        return gerentesEnfermagem;
    }

    public Usuario getUsuarioGerenteEnfermagem() {
        return usuarioGerenteEnfermagem;
    }

    public void setUsuarioGerenteEnfermagem(Usuario usuarioGerenteEnfermagem) {
        this.usuarioGerenteEnfermagem = usuarioGerenteEnfermagem;
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
