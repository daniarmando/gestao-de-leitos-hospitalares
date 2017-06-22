/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.FisioterapeutaDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Fisioterapeuta;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
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
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public class FisioterapeutaBean implements InterfaceBean, Serializable {

    private FisioterapeutaDAOImpl daoFisioterapeuta = new FisioterapeutaDAOImpl();
    private Fisioterapeuta fisioterapeuta;
    private List<Fisioterapeuta> fisioterapeutas = new ArrayList<>();
    private List<Fisioterapeuta> filtrarLista;

    private final EstadoCidadeDAOImpl daoEstadoCidade = new EstadoCidadeDAOImpl();
    private List<Estado> estados;
    private List<Cidade> cidades;
    private Estado estado;
    private Cidade cidade;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private final LogDAOImpl daoLog = new LogDAOImpl();
    private Log log;
    private List<Log> logs = new ArrayList<>();

    private Fisioterapeuta cloneFisioterapeuta;

    /**
     * Creates a new instance of FisioterapeutaBean
     */
    public FisioterapeutaBean() {
        fisioterapeuta = new Fisioterapeuta();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.fisioterapeutas = daoFisioterapeuta.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        
        this.log = new Log();

        if (isEditar()) {
            this.cidades = this.daoEstadoCidade.listarCidades(this.fisioterapeuta.getEstado());
            this.cloneFisioterapeuta = this.fisioterapeuta.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.estados = this.daoEstadoCidade.listarEstados();
    }

    @Override
    public String novo() {
        fisioterapeuta = new Fisioterapeuta();
        //fisioterapeuta.setStatusCadastroFisioterapeuta(Boolean.TRUE);
        return "cadastro-fisioterapeuta?faces-redirect=true";
    }

    @Override
    public void salvar() {
        //Verificando CPF
        if (ConsultaCPFValidator.verificar(this.fisioterapeuta, this.cloneFisioterapeuta)) {
            this.fisioterapeuta.setStatusFisioterapeuta(Status.ATIVO.get());
            this.fisioterapeuta.setStatusPessoa(Status.ATIVO.get());

            this.daoFisioterapeuta.salvar(this.fisioterapeuta);

            //gravando o log
            salvarLog();

            this.fisioterapeuta = new Fisioterapeuta();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fisioterapeuta salvo com sucesso!"));
        }
    }

    @Override
    public Boolean isEditar() {
        return this.fisioterapeuta.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.fisioterapeuta.setStatusPessoa(Status.INATIVO.get());
        this.fisioterapeuta.setStatusFisioterapeuta(Status.INATIVO.get());

        this.daoFisioterapeuta.salvar(this.fisioterapeuta);

        //Atualizando a lista de Fisioterapeutas
        this.fisioterapeutas.remove(this.fisioterapeuta);

        //gravando o log
        this.log.setTipo(TipoLog.INATIVACAO.get());
        salvarLog();

        this.fisioterapeuta = new Fisioterapeuta();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fisioterapeuta Inativado com sucesso!"));
    }

    @Override
    public void salvarLog() {
        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.fisioterapeuta.getNomePessoa().equals(this.cloneFisioterapeuta.getNomePessoa())) {
                detalhe += " nome de " + this.cloneFisioterapeuta.getNomePessoa() + " para " + this.fisioterapeuta.getNomePessoa() + ",";
            }
            
            if (!this.fisioterapeuta.getCrefitoFisioterapeuta().equals(this.cloneFisioterapeuta.getCrefitoFisioterapeuta())) {
                detalhe += " crefito de " + this.cloneFisioterapeuta.getCrefitoFisioterapeuta() + " para " + this.fisioterapeuta.getCrefitoFisioterapeuta() + ",";
            }

            if (!this.fisioterapeuta.getCpfPessoa().equals(this.cloneFisioterapeuta.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneFisioterapeuta.getCpfPessoa() + " para " + this.fisioterapeuta.getCpfPessoa() + ",";
            }

            if (!this.fisioterapeuta.getRgPessoa().equals(this.cloneFisioterapeuta.getRgPessoa())) {
                detalhe += " rg de " + this.cloneFisioterapeuta.getRgPessoa() + " para " + this.fisioterapeuta.getRgPessoa() + ",";
            }

            if (!this.fisioterapeuta.getSexoPessoa().equals(this.cloneFisioterapeuta.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneFisioterapeuta.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + " para "
                        + (this.fisioterapeuta.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + ",";
            }

            if (!this.fisioterapeuta.getDataNascimentoPessoa().equals(this.cloneFisioterapeuta.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.cloneFisioterapeuta.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.fisioterapeuta.getDataNascimentoPessoa()) + ",";
            }

            if (!this.fisioterapeuta.getTelefonePessoa().equals(this.cloneFisioterapeuta.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneFisioterapeuta.getTelefonePessoa() + " para " + this.fisioterapeuta.getTelefonePessoa() + ",";
            }

            if (!this.fisioterapeuta.getCelularPessoa().equals(this.cloneFisioterapeuta.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneFisioterapeuta.getCelularPessoa() + " para " + this.fisioterapeuta.getCelularPessoa() + ",";
            }

            if (!this.fisioterapeuta.getEnderecoPessoa().equals(this.cloneFisioterapeuta.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneFisioterapeuta.getEnderecoPessoa() + " para " + this.fisioterapeuta.getEnderecoPessoa() + ",";
            }

            if (!this.fisioterapeuta.getNumeroPessoa().equals(this.cloneFisioterapeuta.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneFisioterapeuta.getNumeroPessoa() + " para " + this.fisioterapeuta.getNumeroPessoa() + ",";
            }

            if (this.fisioterapeuta.getComplementoPessoa() != null) {
                if (!this.fisioterapeuta.getComplementoPessoa().equals(this.cloneFisioterapeuta.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneFisioterapeuta.getComplementoPessoa() + " para " + this.fisioterapeuta.getComplementoPessoa() + ",";
                }
            }

            if (!this.fisioterapeuta.getBairroPessoa().equals(this.cloneFisioterapeuta.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneFisioterapeuta.getBairroPessoa() + " para " + this.fisioterapeuta.getBairroPessoa() + ",";
            }

            if (!this.fisioterapeuta.getCepPessoa().equals(this.cloneFisioterapeuta.getCepPessoa())) {
                detalhe += " cep de " + this.cloneFisioterapeuta.getCepPessoa() + " para " + this.fisioterapeuta.getCepPessoa() + ",";
            }

            if (!this.fisioterapeuta.getEmailPessoa().equals(this.cloneFisioterapeuta.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneFisioterapeuta.getEmailPessoa() + " para " + this.fisioterapeuta.getEmailPessoa() + ",";
            }

            if (!this.fisioterapeuta.getStatusFisioterapeuta().equals(this.cloneFisioterapeuta.getStatusFisioterapeuta())) {
                detalhe += " status de "
                        + (this.cloneFisioterapeuta.getStatusFisioterapeuta().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + " para "
                        + (this.fisioterapeuta.getStatusFisioterapeuta().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + ",";
            }

            if (!this.fisioterapeuta.getEstado().getNomeEstado().equals(this.cloneFisioterapeuta.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneFisioterapeuta.getEstado().getNomeEstado() + " para " + this.fisioterapeuta.getEstado().getNomeEstado() + ",";
            }

            if (!this.fisioterapeuta.getCidade().getNomeCidade().equals(this.cloneFisioterapeuta.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneFisioterapeuta.getCidade().getNomeCidade() + " para " + this.fisioterapeuta.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
            
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("fisioterapeuta");
        this.log.setIdObjeto(this.fisioterapeuta.getIdPessoa());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("fisioterapeuta");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("fisioterapeuta", this.fisioterapeuta.getIdPessoa());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioFisioterapeuta";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.fisioterapeutas);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Fisioterapeuta clone() {
        Fisioterapeuta clone = new Fisioterapeuta();
        clone.setIdPessoa(this.fisioterapeuta.getIdPessoa());
        clone.setCpfPessoa(this.fisioterapeuta.getCpfPessoa());

        return clone;
    }

    /**
     * Método que aciona o método que lista cidades passando o id do estado para
     * exibir na página
     *
     * @param event
     */
    public void listaCidades(AjaxBehaviorEvent event) {
        setCidades(daoEstadoCidade.listarCidades(fisioterapeuta.getEstado()));
    }

    /**
     * @return the fisioterapeuta
     */
    public Fisioterapeuta getFisioterapeuta() {
        return fisioterapeuta;
    }

    /**
     * @param fisioterapeuta the fisioterapeuta to set
     */
    public void setFisioterapeuta(Fisioterapeuta fisioterapeuta) {
        this.fisioterapeuta = fisioterapeuta;
    }

    /**
     * @return the filtrarLista
     */
    public List<Fisioterapeuta> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Fisioterapeuta> filtrarLista) {
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
     * @return the fisioterapeutas
     */
    public List<Fisioterapeuta> getFisioterapeutas() {
        return fisioterapeutas;
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
