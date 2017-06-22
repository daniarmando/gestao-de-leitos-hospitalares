/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.PacienteDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Paciente;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import br.com.gestaohospitalar.nir.util.report.GerarRelatorio;
import br.com.gestaohospitalar.nir.validator.ConsultaCPFValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public class PacienteBean implements InterfaceBean, Serializable {

    private final PacienteDAOImpl daoPaciente = new PacienteDAOImpl();
    private Paciente paciente;
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Paciente> filtrarLista;

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

    private Paciente clonePaciente;

    /**
     * Creates a new instance of PacienteBean
     */
    public PacienteBean() {
        paciente = new Paciente();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.log = new Log();
        this.pacientes = this.daoPaciente.listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        
        this.log = new Log();

        if (isEditar()) {
            this.cidades = this.daoEstadoCidade.listarCidades(this.paciente.getEstado());
            this.clonePaciente = this.paciente.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.estados = this.daoEstadoCidade.listarEstados();

    }

    @Override
    public String novo() {
        paciente = new Paciente();
        return "cadastro-paciente?faces-redirect=true";
    }

    @Override
    public void salvar() {
        //Verificando CPF
        if (ConsultaCPFValidator.verificar(this.paciente, this.clonePaciente)) {
            this.paciente.setStatusPaciente(Status.ATIVO.get());
            this.paciente.setStatusPessoa(Status.ATIVO.get());
            this.daoPaciente.salvar(this.paciente);

            //gravando o log
            salvarLog();

            this.paciente = new Paciente();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Paciente salvo com sucesso!"));
        }
    }

    @Override
    public Boolean isEditar() {
        return this.paciente.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        if (daoPaciente.verificarSePossuiInternacaoAberta(this.paciente) == false) {
            this.paciente.setStatusPessoa(Status.INATIVO.get());
            this.paciente.setStatusPaciente(Status.INATIVO.get());

            this.daoPaciente.salvar(this.paciente);

            //Atualizando a lista de pacientes
            this.pacientes.remove(this.paciente);

            //gravando o log
            this.log.setTipo(TipoLog.INATIVACAO.get());
            salvarLog();

            this.paciente = new Paciente();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Paciente Inativado com sucesso!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Paciente não pode ser Inativado, pois está Internado!", null));
        }
    }

    @Override
    public void salvarLog() {
        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.paciente.getNomePessoa().equals(this.clonePaciente.getNomePessoa())) {
                detalhe += " nome de " + this.clonePaciente.getNomePessoa() + " para " + this.paciente.getNomePessoa() + ",";
            }

            if (!this.paciente.getCodigoSusPaciente().equals(this.clonePaciente.getCodigoSusPaciente())) {
                detalhe += " código SUS de " + this.clonePaciente.getCodigoSusPaciente() + " para " + this.paciente.getCodigoSusPaciente() + ",";
            }
            
            if (!this.paciente.getObservacoesPaciente().equals(this.clonePaciente.getObservacoesPaciente())) {
                detalhe += " campo observações alterado,";
            }

            if (!this.paciente.getCpfPessoa().equals(this.clonePaciente.getCpfPessoa())) {
                detalhe += " cpf de " + this.clonePaciente.getCpfPessoa() + " para " + this.paciente.getCpfPessoa() + ",";
            }

            if (!this.paciente.getRgPessoa().equals(this.clonePaciente.getRgPessoa())) {
                detalhe += " rg de " + this.clonePaciente.getRgPessoa() + " para " + this.paciente.getRgPessoa() + ",";
            }

            if (!this.paciente.getSexoPessoa().equals(this.clonePaciente.getSexoPessoa())) {
                detalhe += " sexo de " 
                        + (this.clonePaciente.getSexoPessoa().equals("M") ? " masculino " : " feminino ") 
                        + " para " 
                        + (this.paciente.getSexoPessoa().equals("M") ? " masculino " : " feminino ") 
                        + ",";
            }

            if (!this.paciente.getDataNascimentoPessoa().equals(this.clonePaciente.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.clonePaciente.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.paciente.getDataNascimentoPessoa()) + ",";
            }

            if (!this.paciente.getTelefonePessoa().equals(this.clonePaciente.getTelefonePessoa())) {
                detalhe += " telefone de " + this.clonePaciente.getTelefonePessoa() + " para " + this.paciente.getTelefonePessoa() + ",";
            }

            if (!this.paciente.getCelularPessoa().equals(this.clonePaciente.getCelularPessoa())) {
                detalhe += " celular de " + this.clonePaciente.getCelularPessoa() + " para " + this.paciente.getCelularPessoa() + ",";
            }

            if (!this.paciente.getEnderecoPessoa().equals(this.clonePaciente.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.clonePaciente.getEnderecoPessoa() + " para " + this.paciente.getEnderecoPessoa() + ",";
            }

            if (!this.paciente.getNumeroPessoa().equals(this.clonePaciente.getNumeroPessoa())) {
                detalhe += " número de " + this.clonePaciente.getNumeroPessoa() + " para " + this.paciente.getNumeroPessoa() + ",";
            }

            if (this.paciente.getComplementoPessoa() != null) {
                if (!this.paciente.getComplementoPessoa().equals(this.clonePaciente.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.clonePaciente.getComplementoPessoa() + " para " + this.paciente.getComplementoPessoa() + ",";
                }
            }

            if (!this.paciente.getBairroPessoa().equals(this.clonePaciente.getBairroPessoa())) {
                detalhe += " bairro de " + this.clonePaciente.getBairroPessoa() + " para " + this.paciente.getBairroPessoa() + ",";
            }

            if (!this.paciente.getCepPessoa().equals(this.clonePaciente.getCepPessoa())) {
                detalhe += " cep de " + this.clonePaciente.getCepPessoa() + " para " + this.paciente.getCepPessoa() + ",";
            }

            if (!this.paciente.getEmailPessoa().equals(this.clonePaciente.getEmailPessoa())) {
                detalhe += " e-mail de " + this.clonePaciente.getEmailPessoa() + " para " + this.paciente.getEmailPessoa() + ",";
            }

            if (!this.paciente.getStatusPaciente().equals(this.clonePaciente.getStatusPaciente())) {
                detalhe += " status de " 
                        + (this.clonePaciente.getStatusPaciente().equals(Status.ATIVO.get()) ? " ativo " : " inativo ") 
                        + " para " 
                        + (this.paciente.getStatusPaciente().equals(Status.ATIVO.get()) ? " ativo " : " inativo ") 
                        + ",";
            }

            if (!this.paciente.getEstado().getNomeEstado().equals(this.clonePaciente.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.clonePaciente.getEstado().getNomeEstado() + " para " + this.paciente.getEstado().getNomeEstado() + ",";
            }

            if (!this.paciente.getCidade().getNomeCidade().equals(this.clonePaciente.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.clonePaciente.getCidade().getNomeCidade() + " para " + this.paciente.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
            
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("paciente");
        this.log.setIdObjeto(this.paciente.getIdPessoa());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.log = this.daoLog.ultimoLogPorObjeto("paciente");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = this.daoLog.listarPorIdObjeto("paciente", this.paciente.getIdPessoa());
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioPaciente";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.pacientes);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    /**
     * Método que aciona o método que lista cidades passando o id do estado para
     * exibir na página
     *
     * @param event
     */
    public void listaCidades(AjaxBehaviorEvent event) {
        this.cidades = daoEstadoCidade.listarCidades(paciente.getEstado());
    }

    /**
     * @return the paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * @return the filtrarLista
     */
    public List<Paciente> getFiltrarLista() {
        return filtrarLista;
    }

    /**
     * @param filtrarLista the filtrarLista to set
     */
    public void setFiltrarLista(List<Paciente> filtrarLista) {
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
     * @return the pacientes
     */
    public List<Paciente> getPacientes() {
        return pacientes;
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
