/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.NIRDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Autorizacao;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.NIR;
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
public class NIRBean implements InterfaceBean, Serializable {

    private final NIRDAOImpl daoNIR = new NIRDAOImpl();
    private NIR nir;
    private List<NIR> listaNir = new ArrayList<>();
    private List<NIR> filtrarLista;

    private final EstadoCidadeDAOImpl daoEstadoCidade = new EstadoCidadeDAOImpl();
    private List<Estado> estados;
    private List<Cidade> cidades;
    private Estado estado;
    private Cidade cidade;

    private final UsuarioDAOImpl daoUsuario = new UsuarioDAOImpl();
    private Usuario usuario;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    private NIR cloneNir;
    private Usuario cloneUsuario;

    /**
     * Creates a new instance of NIRBean
     */
    public NIRBean() {
        nir = new NIR();
        usuario = new Usuario();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.listaNir = this.daoNIR.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        
        this.log = new Log();

        if (isEditar()) {
            this.cidades = this.daoEstadoCidade.listarCidades(this.nir.getEstado());
            this.usuario = this.daoUsuario.usuarioPorIdPessoa(this.nir.getIdPessoa());
            this.cloneNir = this.nir.clone();
            this.cloneUsuario = this.usuario.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.estados = this.daoEstadoCidade.listarEstados();
    }

    @Override
    public String novo() {
        this.nir = new NIR();
        this.usuario = new Usuario();
        return "cadastro-nir?faces-redirect=true";
    }

    @Override
    public void salvar() {
        //Verificando CPF e login
        if (ConsultaCPFValidator.verificar(this.nir, this.cloneNir) && ConsultaLoginValidator.verificar(this.usuario, this.cloneUsuario)) {
            //Se a data de nascimento informada for maior que a data mínima de 18 anos

            this.nir.setStatusNir(Status.ATIVO.get());
            this.nir.setStatusPessoa(Status.ATIVO.get());

            //Salvando NIR
            daoNIR.salvar(this.nir);

            List<Autorizacao> autorizacoes = new ArrayList<>();
            Autorizacao autorizacao = new Autorizacao();
            autorizacao.setNome("ROLE_nir");
            autorizacoes.add(autorizacao);

            this.usuario.setAutorizacoes(autorizacoes);
            this.usuario.setStatus(true);
            this.usuario.setPessoa(this.nir);

            //salvando usuário
            this.daoUsuario.salvar(this.usuario);
            
            //gravando o log
            salvarLog();

            this.nir = new NIR();
            this.usuario = new Usuario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "NIR salvo com sucesso!", null));

        }
    }
    
    @Override
    public void salvarLog() {
        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.nir.getNomePessoa().equals(this.cloneNir.getNomePessoa())) {
                detalhe += " nome de " + this.cloneNir.getNomePessoa() + " para " + this.nir.getNomePessoa() + ",";
            }

            if (!this.nir.getCpfPessoa().equals(this.cloneNir.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneNir.getCpfPessoa() + " para " + this.nir.getCpfPessoa() + ",";
            }

            if (!this.nir.getRgPessoa().equals(this.cloneNir.getRgPessoa())) {
                detalhe += " rg de " + this.cloneNir.getRgPessoa() + " para " + this.nir.getRgPessoa() + ",";
            }

            if (!this.nir.getSexoPessoa().equals(this.cloneNir.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneNir.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + " para "
                        + (this.nir.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + ",";
            }

            if (!this.nir.getDataNascimentoPessoa().equals(this.cloneNir.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.cloneNir.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.nir.getDataNascimentoPessoa()) + ",";
            }

            if (!this.nir.getTelefonePessoa().equals(this.cloneNir.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneNir.getTelefonePessoa() + " para " + this.nir.getTelefonePessoa() + ",";
            }

            if (!this.nir.getCelularPessoa().equals(this.cloneNir.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneNir.getCelularPessoa() + " para " + this.nir.getCelularPessoa() + ",";
            }

            if (!this.nir.getEnderecoPessoa().equals(this.cloneNir.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneNir.getEnderecoPessoa() + " para " + this.nir.getEnderecoPessoa() + ",";
            }

            if (!this.nir.getNumeroPessoa().equals(this.cloneNir.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneNir.getNumeroPessoa() + " para " + this.nir.getNumeroPessoa() + ",";
            }

            if (this.nir.getComplementoPessoa() != null) {
                if (!this.nir.getComplementoPessoa().equals(this.cloneNir.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneNir.getComplementoPessoa() + " para " + this.nir.getComplementoPessoa() + ",";
                }
            }

            if (!this.nir.getBairroPessoa().equals(this.cloneNir.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneNir.getBairroPessoa() + " para " + this.nir.getBairroPessoa() + ",";
            }

            if (!this.nir.getCepPessoa().equals(this.cloneNir.getCepPessoa())) {
                detalhe += " cep de " + this.cloneNir.getCepPessoa() + " para " + this.nir.getCepPessoa() + ",";
            }

            if (!this.nir.getEmailPessoa().equals(this.cloneNir.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneNir.getEmailPessoa() + " para " + this.nir.getEmailPessoa() + ",";
            }

            if (!this.nir.getStatusNir().equals(this.cloneNir.getStatusNir())) {
                detalhe += " status de "
                        + (this.cloneNir.getStatusNir().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + " para "
                        + (this.nir.getStatusNir().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + ",";
            }

            if (!this.nir.getEstado().getNomeEstado().equals(this.cloneNir.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneNir.getEstado().getNomeEstado() + " para " + this.nir.getEstado().getNomeEstado() + ",";
            }

            if (!this.nir.getCidade().getNomeCidade().equals(this.cloneNir.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneNir.getCidade().getNomeCidade() + " para " + this.nir.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("nir");
        this.log.setIdObjeto(this.nir.getIdPessoa());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public Boolean isEditar() {
        return this.nir.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("nir");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("nir", this.nir.getIdPessoa());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioNIR";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.listaNir);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * Método que aciona o método que lista cidades passando o id do estado para
     * exibir na página
     *
     * @param event
     */
    public void listaCidades(AjaxBehaviorEvent event) {
        this.cidades = daoEstadoCidade.listarCidades(this.nir.getEstado());
    }

    /**
     * @return the nir
     */
    public NIR getNir() {
        return nir;
    }

    /**
     * @param nir the nir to set
     */
    public void setNir(NIR nir) {
        this.nir = nir;
    }

    /**
     * @return the filtrarLista
     */
    public List<NIR> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<NIR> filtrarLista) {
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
     * @return the listaNir
     */
    public List<NIR> getListaNir() {
        return listaNir;
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
