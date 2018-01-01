/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

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
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
@ViewScoped
public class PacienteBean implements InterfaceBean, Serializable {

    private PacienteDAOImpl daoPaciente;
    private Paciente paciente, clonePaciente;
    private List<Paciente> pacientes, pacientesFilstrados;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of PacienteBean
     */
    public PacienteBean() {
        this.paciente = new Paciente();
    }

    @Override
    public void inicializarConsulta() {
        this.daoPaciente = new PacienteDAOImpl();
        this.pacientes = this.daoPaciente.ativos();
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
        this.daoPaciente = new PacienteDAOImpl();
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());
        
        this.paciente = this.daoPaciente.porId(this.paciente.getIdPessoa());
        this.clonePaciente = this.paciente.clone();
        this.estados = this.daoEstadoCidade.todosEstados();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.paciente.getEstado());        
    }

    @Override
    public void salvar() {
        this.daoPaciente = new PacienteDAOImpl();

        try {

            //verificando CPF
            if (ConsultaCPFValidator.verificar(this.paciente, this.clonePaciente)) {
                this.paciente.setStatusPaciente(Status.ATIVO.get());
                this.paciente.setStatusPessoa(Status.ATIVO.get());
                this.daoPaciente.salvar(this.paciente);

                //gravando o log
                this.salvarLog();

                this.paciente = new Paciente();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Paciente salvo com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.paciente.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.daoPaciente = new PacienteDAOImpl();

        try {

            if (this.daoPaciente.estaEmInternacaoAberta(this.paciente) == false) {
                this.paciente.setStatusPessoa(Status.INATIVO.get());
                this.paciente.setStatusPaciente(Status.INATIVO.get());

                this.daoPaciente.salvar(this.paciente);

                //gravando o log
                this.log = new Log(TipoLog.INATIVACAO.get());
                this.salvarLog();
                
                this.inicializarConsulta();

                this.paciente = new Paciente();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Paciente inativado com sucesso!");

            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Paciente não pode ser inativado, pois está Internado!");
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
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("paciente");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("paciente", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioPaciente";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.pacientes);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public void listaCidades(AjaxBehaviorEvent event) {
        this.cidades = new EstadoCidadeDAOImpl().cidadesPorEstado(paciente.getEstado());
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Paciente> getPacientesFilstrados() {
        return pacientesFilstrados;
    }

    public void setPacientesFilstrados(List<Paciente> pacientesFilstrados) {
        this.pacientesFilstrados = pacientesFilstrados;
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

    public List<Paciente> getPacientes() {
        return pacientes;
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
