/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.MedicoDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Medico;
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
public class MedicoBean implements InterfaceBean, Serializable {

    private MedicoDAOImpl daoMedico = new MedicoDAOImpl();
    private Medico medico;
    private List<Medico> medicos = new ArrayList<>();
    private List<Medico> filtrarLista;

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

    private Medico cloneMedico;

    /**
     * Creates a new instance of MedicoBean
     */
    public MedicoBean() {
        medico = new Medico();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.medicos = this.daoMedico.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        
        this.log = new Log();

        if (isEditar()) {
            this.cidades = this.daoEstadoCidade.listarCidades(this.medico.getEstado());
            this.cloneMedico = this.medico.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.estados = this.daoEstadoCidade.listarEstados();

    }

    @Override
    public String novo() {
        medico = new Medico();
        return "cadastro-medico?faces-redirect=true";
    }

    @Override
    public void salvar() {
        //Verificando CPF
        if (ConsultaCPFValidator.verificar(this.medico, this.cloneMedico)) {
            this.medico.setStatusMedico(Status.ATIVO.get());
            this.medico.setStatusPessoa(Status.ATIVO.get());
            this.daoMedico.salvar(this.medico);

            //gravando o log
            salvarLog();

            this.medico = new Medico();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Médico salvo com sucesso!"));
        }
    }

    @Override
    public Boolean isEditar() {
        return this.medico.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        if (daoMedico.verificarSePossuiInternacaoAberta(this.medico) == false) {
            this.medico.setStatusPessoa(Status.INATIVO.get());
            this.medico.setStatusMedico(Status.INATIVO.get());

            this.daoMedico.salvar(this.medico);

            //atualizando a lista de médicos
            this.medicos.remove(this.medico);

            //gravando o log
            this.log.setTipo(TipoLog.INATIVACAO.get());
            salvarLog();

            this.medico = new Medico();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Médico Inativado com sucesso!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Médico não pode ser Inativado, pois está em Internação aberta!", null));
        }
    }

    @Override
    public void salvarLog() {
        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.medico.getNomePessoa().equals(this.cloneMedico.getNomePessoa())) {
                detalhe += " nome de " + this.cloneMedico.getNomePessoa() + " para " + this.medico.getNomePessoa() + ",";
            }

            if (!this.medico.getCrmMedico().equals(this.cloneMedico.getCrmMedico())) {
                detalhe += " crm de " + this.cloneMedico.getCrmMedico() + " para " + this.medico.getCrmMedico()+ ",";
            }

            if (!this.medico.getCpfPessoa().equals(this.cloneMedico.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneMedico.getCpfPessoa() + " para " + this.medico.getCpfPessoa() + ",";
            }

            if (!this.medico.getRgPessoa().equals(this.cloneMedico.getRgPessoa())) {
                detalhe += " rg de " + this.cloneMedico.getRgPessoa() + " para " + this.medico.getRgPessoa() + ",";
            }

            if (!this.medico.getSexoPessoa().equals(this.cloneMedico.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneMedico.getSexoPessoa().equals("M") ? "masculino" : "feminino")
                        + " para "
                        + (this.medico.getSexoPessoa().equals("M") ? "masculino" : "feminino")
                        + ",";
            }

            if (!this.medico.getDataNascimentoPessoa().equals(this.cloneMedico.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.cloneMedico.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.medico.getDataNascimentoPessoa()) + ",";
            }

            if (!this.medico.getTelefonePessoa().equals(this.cloneMedico.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneMedico.getTelefonePessoa() + " para " + this.medico.getTelefonePessoa() + ",";
            }

            if (!this.medico.getCelularPessoa().equals(this.cloneMedico.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneMedico.getCelularPessoa() + " para " + this.medico.getCelularPessoa() + ",";
            }

            if (!this.medico.getEnderecoPessoa().equals(this.cloneMedico.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneMedico.getEnderecoPessoa() + " para " + this.medico.getEnderecoPessoa() + ",";
            }

            if (!this.medico.getNumeroPessoa().equals(this.cloneMedico.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneMedico.getNumeroPessoa() + " para " + this.medico.getNumeroPessoa() + ",";
            }

            if (this.medico.getComplementoPessoa() != null) {
                if (!this.medico.getComplementoPessoa().equals(this.cloneMedico.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneMedico.getComplementoPessoa() + " para " + this.medico.getComplementoPessoa() + ",";
                }
            }

            if (!this.medico.getBairroPessoa().equals(this.cloneMedico.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneMedico.getBairroPessoa() + " para " + this.medico.getBairroPessoa() + ",";
            }

            if (!this.medico.getCepPessoa().equals(this.cloneMedico.getCepPessoa())) {
                detalhe += " cep de " + this.cloneMedico.getCepPessoa() + " para " + this.medico.getCepPessoa() + ",";
            }

            if (!this.medico.getEmailPessoa().equals(this.cloneMedico.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneMedico.getEmailPessoa() + " para " + this.medico.getEmailPessoa() + ",";
            }

            if (!this.medico.getStatusMedico().equals(this.cloneMedico.getStatusMedico())) {
                detalhe += " status de "
                        + (this.cloneMedico.getStatusMedico().equals(Status.ATIVO.get()) ? "ativo" : "inativo")
                        + " para "
                        + (this.medico.getStatusMedico().equals(Status.ATIVO.get()) ? "ativo" : "inativo")
                        + ",";
            }

            if (!this.medico.getEstado().getNomeEstado().equals(this.cloneMedico.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneMedico.getEstado().getNomeEstado() + " para " + this.medico.getEstado().getNomeEstado() + ",";
            }

            if (!this.medico.getCidade().getNomeCidade().equals(this.cloneMedico.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneMedico.getCidade().getNomeCidade() + " para " + this.medico.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";            
            
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("medico");
        this.log.setIdObjeto(this.medico.getIdPessoa());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("medico");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("medico", this.medico.getIdPessoa());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioMedico";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.medicos);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Medico clone() {
        Medico clone = new Medico();
        clone.setIdPessoa(this.medico.getIdPessoa());
        clone.setCpfPessoa(this.medico.getCpfPessoa());

        return clone;
    }

    /**
     * Método que aciona o método que lista cidades passando o id do estado para
     * exibir na página
     *
     * @param event
     */
    public void listaCidades(AjaxBehaviorEvent event) {
        this.cidades = daoEstadoCidade.listarCidades(medico.getEstado());
    }

    /**
     * @return the medico
     */
    public Medico getMedico() {
        return medico;
    }

    /**
     * @param medico the medico to set
     */
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    /**
     * @return the filtrarLista
     */
    public List<Medico> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Medico> filtrarLista) {
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
     * @return the medicos
     */
    public List<Medico> getMedicos() {
        return medicos;
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
