/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

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
public class GerenteEnfermagemBean implements InterfaceBean, Serializable {

    private GerenteEnfermagemDAOImpl daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();
    private GerenteEnfermagem gerenteEnfermagem;
    private List<GerenteEnfermagem> gerentesEnfermagem = new ArrayList<>();
    private List<GerenteEnfermagem> filtrarLista;

    private final EstadoCidadeDAOImpl daoEstadoCidade = new EstadoCidadeDAOImpl();
    private List<Estado> estados;
    private List<Cidade> cidades;
    private Estado estado;
    private Cidade cidade;

    private GerenteEnfermagem cloneGerenteEnfermagem;
    private Usuario cloneUsuario;

    private final UsuarioDAOImpl daoUsuario = new UsuarioDAOImpl();
    private Usuario usuario;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    /**
     * Creates a new instance of GerenteEnfermagemBean
     */
    public GerenteEnfermagemBean() {
        gerenteEnfermagem = new GerenteEnfermagem();
        usuario = new Usuario();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.gerentesEnfermagem = daoGerenteEnfermagem.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        
        this.log = new Log();

        if (isEditar()) {
            this.cidades = this.daoEstadoCidade.listarCidades(this.gerenteEnfermagem.getEstado());
            this.usuario = this.daoUsuario.usuarioPorIdPessoa(this.gerenteEnfermagem.getIdPessoa());
            this.cloneGerenteEnfermagem = this.gerenteEnfermagem.clone();
            this.cloneUsuario = this.usuario.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.estados = this.daoEstadoCidade.listarEstados();
    }

    @Override
    public String novo() {
        this.gerenteEnfermagem = new GerenteEnfermagem();
        this.usuario = new Usuario();
        return "cadastro-gerente-enfermagem?faces-redirect=true";
    }

    @Override
    public void salvar() {
        //verificando CPF e login
        if (ConsultaCPFValidator.verificar(this.gerenteEnfermagem, this.cloneGerenteEnfermagem) && ConsultaLoginValidator.verificar(this.getUsuario(), this.cloneUsuario)) {
            //ve a data de nascimento informada for maior que a data mínima de 18 anos
            if (this.gerenteEnfermagem.getDataNascimentoPessoa().after(getMaxDate())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Data de Nascimento deve ser menor que " + getMaxDate() + ", pois só pode ser cadastrado se for maior de 18 anos.", null));
            } else {

                this.gerenteEnfermagem.setStatusGerenteEnfermagem(Status.ATIVO.get());
                this.gerenteEnfermagem.setStatusPessoa(Status.ATIVO.get());

                //Salvando Gerente de Enfermagem
                this.daoGerenteEnfermagem.salvar(this.gerenteEnfermagem);

                List<Autorizacao> autorizacoes = new ArrayList<>();
                Autorizacao autorizacao = new Autorizacao();
                autorizacao.setNome("ROLE_gen");
                autorizacoes.add(autorizacao);

                this.getUsuario().setAutorizacoes(autorizacoes);
                this.getUsuario().setStatus(true);
                this.getUsuario().setPessoa(this.gerenteEnfermagem);

                //salvando Usuário
                this.daoUsuario.salvar(this.usuario);

                //gravando o log
                salvarLog();

                this.gerenteEnfermagem = new GerenteEnfermagem();
                this.usuario = new Usuario();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Gerente de Enfermagem salvo com sucesso!", null));
            }
        }
    }

    @Override
    public Boolean isEditar() {
        return this.gerenteEnfermagem.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        if (daoGerenteEnfermagem.verificarSePossuiEnfermeiro(this.gerenteEnfermagem) == false) {
            this.gerenteEnfermagem.setStatusPessoa(Status.INATIVO.get());
            this.gerenteEnfermagem.setStatusGerenteEnfermagem(Status.INATIVO.get());

            this.daoGerenteEnfermagem.salvar(this.gerenteEnfermagem);

            //inativando o usuário
            this.usuario = daoUsuario.usuarioPorIdPessoa(this.gerenteEnfermagem.getIdPessoa());
            this.usuario.setPessoa(gerenteEnfermagem);
            this.usuario.setStatus(false);
            this.daoUsuario.salvar(this.usuario);

            //atualizando a lista de Gerentes de Enfermagem
            this.gerentesEnfermagem.remove(this.gerenteEnfermagem);

            //gravando o log
            this.log.setTipo(TipoLog.INATIVACAO.get());
            salvarLog();

            this.gerenteEnfermagem = new GerenteEnfermagem();
            this.usuario = new Usuario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Gerente de Enfermagem Inativado com sucesso!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gerente de Enfermagem não pode ser Inativado, pois possuí Enfermeiro(s) vinculado(s)!", null));
        }
    }

    @Override
    public void salvarLog() {
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
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("gerenteEnfermagem");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("gerenteEnfermagem", this.gerenteEnfermagem.getIdPessoa());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioGerenteEnfermagem";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.gerentesEnfermagem);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * método que aciona o método que lista cidades passando o id do estado para
     * exibir na página
     *
     * @param event
     */
    public void listaCidades(AjaxBehaviorEvent event) {
        this.cidades = this.daoEstadoCidade.listarCidades(this.gerenteEnfermagem.getEstado());
    }

    /**
     * @return the gerenteEnfermagem
     */
    public GerenteEnfermagem getGerenteEnfermagem() {
        return gerenteEnfermagem;
    }

    /**
     * @param gerenteEnfermagem the gerenteEnfermagem to set
     */
    public void setGerenteEnfermagem(GerenteEnfermagem gerenteEnfermagem) {
        this.gerenteEnfermagem = gerenteEnfermagem;
    }

    /**
     * @return the filtrarLista
     */
    public List<GerenteEnfermagem> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<GerenteEnfermagem> filtrarLista) {
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
     * @return the gerentesEnfermagem
     */
    public List<GerenteEnfermagem> getGerentesEnfermagem() {
        return gerentesEnfermagem;
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
