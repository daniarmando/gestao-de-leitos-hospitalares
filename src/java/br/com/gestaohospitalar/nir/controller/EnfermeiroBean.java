/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.EnfermeiroDAOImpl;
import br.com.gestaohospitalar.nir.DAO.GerenteEnfermagemDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Autorizacao;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Enfermeiro;
import br.com.gestaohospitalar.nir.model.GerenteEnfermagem;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.model.Usuario;
import br.com.gestaohospitalar.nir.util.report.GerarRelatorio;
import br.com.gestaohospitalar.nir.validator.ConsultaCPFValidator;
import br.com.gestaohospitalar.nir.validator.ConsultaLoginValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public class EnfermeiroBean implements InterfaceBean, Serializable {

    private EnfermeiroDAOImpl daoEnfermeiro = new EnfermeiroDAOImpl();
    private Enfermeiro enfermeiro;
    private List<Enfermeiro> enfermeiros = new ArrayList<>();
    private List<Enfermeiro> filtrarLista;

    private final EstadoCidadeDAOImpl daoEstadoCidade = new EstadoCidadeDAOImpl();
    private List<Estado> estados;
    private List<Cidade> cidades;
    private Estado estado;
    private Cidade cidade;

    private GerenteEnfermagemDAOImpl daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();
    private List<GerenteEnfermagem> gerentesEnfermagem;

    private final UsuarioDAOImpl daoUsuario = new UsuarioDAOImpl();
    private Usuario usuario;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    private Enfermeiro cloneEnfermeiro;
    private Usuario cloneUsuario;

    /**
     * Creates a new instance of Enfermeiro
     */
    public EnfermeiroBean() {
        enfermeiro = new Enfermeiro();
        usuario = new Usuario();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.enfermeiros = this.daoEnfermeiro.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        
        this.log = new Log();

        if (isEditar()) {
            this.cidades = this.daoEstadoCidade.listarCidades(this.enfermeiro.getEstado());
            this.usuario = this.daoUsuario.usuarioPorIdPessoa(this.enfermeiro.getIdPessoa());
            this.cloneEnfermeiro = this.enfermeiro.clone();
            this.cloneUsuario = this.usuario.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.estados = this.daoEstadoCidade.listarEstados();
        this.gerentesEnfermagem = daoGerenteEnfermagem.listar();
    }

    @Override
    public String novo() {
        this.enfermeiro = new Enfermeiro();
        this.usuario = new Usuario();
        return "cadastro-enfermeiro?faces-redirect=true";
    }

    @Override
    public void salvar() {
        //Verificando CPF e login
        if (ConsultaCPFValidator.verificar(this.enfermeiro, this.cloneEnfermeiro) && ConsultaLoginValidator.verificar(this.usuario, this.cloneUsuario)) {
            //Se a data de nascimento informada for maior que a data mínima de 18 anos
            if (enfermeiro.getDataNascimentoPessoa().after(getMaxDate())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Data de Nascimento deve ser menor que " + getMaxDate() + ", pois só pode ser cadastrado se for maior de 18 anos.", null));
            } else {

                this.enfermeiro.setStatusEnfermeiro(Status.ATIVO.get());
                this.enfermeiro.setStatusPessoa(Status.ATIVO.get());

                //salvando Enfermeiro
                this.daoEnfermeiro.salvar(this.enfermeiro);

                List<Autorizacao> autorizacoes = new ArrayList<>();
                Autorizacao autorizacao = new Autorizacao();
                autorizacao.setNome("ROLE_enf");
                autorizacoes.add(autorizacao);

                this.usuario.setAutorizacoes(autorizacoes);
                this.usuario.setStatus(true);
                this.usuario.setPessoa(this.enfermeiro);

                //salvando usuário
                this.daoUsuario.salvar(this.usuario);

                //gravando o log
                salvarLog();

                this.enfermeiro = new Enfermeiro();
                this.usuario = new Usuario();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Enfermeiro salvo com sucesso!", null));
            }
        }
    }

    @Override
    public Boolean isEditar() {
        return this.enfermeiro.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.enfermeiro.setStatusPessoa(Status.INATIVO.get());
        this.enfermeiro.setStatusEnfermeiro(Status.INATIVO.get());

        this.daoEnfermeiro.salvar(this.enfermeiro);

        //Inativando o usuário
        this.usuario = daoUsuario.usuarioPorIdPessoa(this.enfermeiro.getIdPessoa());
        this.usuario.setPessoa(enfermeiro);
        this.usuario.setStatus(false);
        this.daoUsuario.salvar(this.usuario);

        //Atualizando lista de enfermeiros
        this.enfermeiros.remove(this.enfermeiro);

        //gravando o log
        this.log.setTipo(TipoLog.INATIVACAO.get());
        salvarLog();

        this.enfermeiro = new Enfermeiro();
        this.usuario = new Usuario();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Enfermeiro Inativado com sucesso!", null));

    }

    @Override
    public void salvarLog() {
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
                detalhe += " gerente de " + this.cloneEnfermeiro.getGerenteEnfermagem().getNomePessoa() + " para " + this.enfermeiro.getGerenteEnfermagem().getNomePessoa() +  ",";
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
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.cloneEnfermeiro.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.enfermeiro.getDataNascimentoPessoa()) + ",";
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
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("enfermeiro");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("enfermeiro", this.enfermeiro.getIdPessoa());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioEnfermeiro";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.enfermeiros);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * Método que aciona o método que lista cidades passando o id do estado para
     * exibir na página
     *
     * @param event
     */
    public void listaCidades(AjaxBehaviorEvent event) {
        this.cidades = daoEstadoCidade.listarCidades(this.enfermeiro.getEstado());
    }

    /**
     * @return the enfermeiro
     */
    public Enfermeiro getEnfermeiro() {
        return enfermeiro;
    }

    /**
     * @param enfermeiro the enfermeiro to set
     */
    public void setEnfermeiro(Enfermeiro enfermeiro) {
        this.enfermeiro = enfermeiro;
    }

    /**
     * @return the filtrarLista
     */
    public List<Enfermeiro> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Enfermeiro> filtrarLista) {
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
     * @return the gerentesEnfermagem
     */
    public List<GerenteEnfermagem> getGerentesEnfermagem() {
        return gerentesEnfermagem;
    }

    /**
     * @param gerentesEnfermagem the gerentesEnfermagem to set
     */
    public void setGerentesEnfermagem(List<GerenteEnfermagem> gerentesEnfermagem) {
        this.gerentesEnfermagem = gerentesEnfermagem;
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
     * @return the enfermeiros
     */
    public List<Enfermeiro> getEnfermeiros() {
        return enfermeiros;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
