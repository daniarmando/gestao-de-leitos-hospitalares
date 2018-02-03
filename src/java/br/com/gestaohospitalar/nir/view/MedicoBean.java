/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.MedicoDAOImpl;
import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.Medico;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import br.com.gestaohospitalar.nir.util.report.GerarRelatorio;
import br.com.gestaohospitalar.nir.validator.ConsultaCPFValidator;
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
public class MedicoBean implements InterfaceBean, Serializable {

    private MedicoDAOImpl daoMedico;
    private Medico medico, cloneMedico;
    private List<Medico> medicos, medicosFiltrados;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of MedicoBean
     */
    public MedicoBean() {
        this.medico = new Medico();
    }

    @Override
    public void inicializarConsulta() {
        this.daoMedico = new MedicoDAOImpl();
        this.medicos = this.daoMedico.ativos();
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
        this.daoMedico = new MedicoDAOImpl();
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());
        
        this.medico = this.daoMedico.porId(this.medico.getIdPessoa());
        this.cloneMedico = this.medico.clone();
        this.estados = this.daoEstadoCidade.todosEstados();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.medico.getEstado());        
    }

    @Override
    public void salvar() {
        this.daoMedico = new MedicoDAOImpl();

        try {

            //verificando CPF
            if (ConsultaCPFValidator.verificar(this.medico, this.cloneMedico)) {
                this.medico.setStatusMedico(Status.ATIVO.get());
                this.medico.setStatusPessoa(Status.ATIVO.get());
                this.daoMedico.salvar(this.medico);

                //gravando o log
                this.salvarLog();

                this.medico = new Medico();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Médico salvo com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.medico.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.daoMedico = new MedicoDAOImpl();

        try {

            if (this.daoMedico.estaEmInternacaoAberta(this.medico) == false) {
                this.medico.setStatusPessoa(Status.INATIVO.get());
                this.medico.setStatusMedico(Status.INATIVO.get());

                this.daoMedico.salvar(this.medico);

                //atualizando a lista de médicos
                this.medicos.remove(this.medico);

                //gravando o log
                this.log = new Log(TipoLog.INATIVACAO.get());
                this.salvarLog();
                
                this.inicializarConsulta();

                this.medico = new Medico();
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Médico inativado com sucesso!");

            } else {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Médico não pode ser Inativado, pois está em Internação aberta!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

//    public void buscarEnderecoPorCEP() {
//        //consulta o WebService da via CEP
//        
//        String CEP = this.medico.getCepPessoa().replace("-", "");
//        
//        WSViaCEP ws = new WSViaCEP(CEP);
//        Map<String, String> mapa = ws.consultarPorCEP();
//        
//        String estado = mapa.get("uf");
//
//        //mapa.keySet().forEach((data) -> {
//
//        //});
//        
//        //this.medico.setEnderecoPessoa(enderecoPessoa);
//        //this.medico.setNumeroPessoa(numeroPessoa);
//        //this.medico.setBairroPessoa(bairroPessoa);
//        //this.medico.setCidade(cidade);
//        //this.medico.setEstado(estado);
//        //this.medico.setComplementoPessoa(complementoPessoa);
//        
//    }
    @Override
    public void salvarLog() {
        this.daoLog = new LogDAOImpl();
        String detalhe = null;

        //se for alteração
        if (this.log.getTipo().equals(TipoLog.ALTERACAO.get())) {
            detalhe = "";

            //compara os atributos para verificar quais foram as alterações feitas para salvar 
            if (!this.medico.getNomePessoa().equals(this.cloneMedico.getNomePessoa())) {
                detalhe += " nome de " + this.cloneMedico.getNomePessoa() + " para " + this.medico.getNomePessoa() + ",";
            }

            if (!this.medico.getCrmMedico().equals(this.cloneMedico.getCrmMedico())) {
                detalhe += " crm de " + this.cloneMedico.getCrmMedico() + " para " + this.medico.getCrmMedico() + ",";
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
                detalhe += " data de nascimento de " + NIRDataUtil.formatarData(this.cloneMedico.getDataNascimentoPessoa()) + " para " + NIRDataUtil.formatarData(this.medico.getDataNascimentoPessoa()) + ",";
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
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("medico");
        return this.log != null ? "Última modificação feita em " + NIRDataUtil.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("medico", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioMedico";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.medicos);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public void listaCidades(AjaxBehaviorEvent event) {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.medico.getEstado());
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public List<Medico> getMedicosFiltrados() {
        return medicosFiltrados;
    }

    public void setMedicosFiltrados(List<Medico> medicosFiltrados) {
        this.medicosFiltrados = medicosFiltrados;
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
        return NIRDataUtil.gerarDataMaiorDeIdade();
    }

    public List<Medico> getMedicos() {
        return medicos;
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
