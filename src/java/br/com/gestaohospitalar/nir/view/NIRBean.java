/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.NIRDAOImpl;
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Autorizacao;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.NIR;
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
public class NIRBean implements InterfaceBean, Serializable {

    private NIRDAOImpl daoNIR;
    private NIR NIR, cloneNIR;
    private List<NIR> listaNIR, listaNIRFiltrada;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;

    private UsuarioDAOImpl daoUsuarioNIR;
    private Usuario usuarioNIR, cloneUsuarioNIR;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of NIRBean
     */
    public NIRBean() {
        this.NIR = new NIR();
        this.usuarioNIR = new Usuario();
    }

    @Override
    public void inicializarConsulta() {
        this.daoNIR = new NIRDAOImpl();
        this.listaNIR = this.daoNIR.todos();
    }

    @Override
    public void inicializarCadastro() {

        if (isEditar()) {

        } else {
            this.daoEstadoCidade = new EstadoCidadeDAOImpl();
            this.log = new Log(TipoLog.INCLUSAO.get());
            
            this.estados = this.daoEstadoCidade.todosEstados();
        }        
    }

    @Override
    public void inicializarEdicao() {
        this.daoNIR = new NIRDAOImpl();
        this.daoUsuarioNIR = new UsuarioDAOImpl();
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());
            
        this.NIR = this.daoNIR.porId(this.NIR.getIdPessoa());
        this.cloneNIR = this.NIR.clone();
        this.usuarioNIR = this.daoUsuarioNIR.porIdPessoa(this.NIR.getIdPessoa());
        this.estados = this.daoEstadoCidade.todosEstados();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.NIR.getEstado());
        this.cloneUsuarioNIR = this.usuarioNIR.clone();        
    }

    @Override
    public void salvar() {
        this.daoNIR = new NIRDAOImpl();
        this.daoUsuarioNIR = new UsuarioDAOImpl();

        try {

            //verificando CPF e login
            if (ConsultaCPFValidator.verificar(this.NIR, this.cloneNIR) && ConsultaLoginValidator.verificar(this.usuarioNIR, this.cloneUsuarioNIR)) {

                this.NIR.setStatusNir(Status.ATIVO.get());
                this.NIR.setStatusPessoa(Status.ATIVO.get());

                //salvando NIR
                this.daoNIR.salvar(this.NIR);

                List<Autorizacao> autorizacoes = new ArrayList<>();
                Autorizacao autorizacao = new Autorizacao();
                autorizacao.setNome("ROLE_nir");
                autorizacoes.add(autorizacao);

                this.usuarioNIR.setAutorizacoes(autorizacoes);
                this.usuarioNIR.setTipo(TipoUsuario.NIR.get());
                this.usuarioNIR.setStatus(true);
                this.usuarioNIR.setPessoa(this.NIR);

                //salvando usuário
                this.daoUsuarioNIR.salvar(this.usuarioNIR);

                //gravando o log
                this.salvarLog();

                this.NIR = new NIR();
                this.usuarioNIR = new Usuario();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "NIR salvo com sucesso!");

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
            if (!this.NIR.getNomePessoa().equals(this.cloneNIR.getNomePessoa())) {
                detalhe += " nome de " + this.cloneNIR.getNomePessoa() + " para " + this.NIR.getNomePessoa() + ",";
            }

            if (!this.NIR.getCpfPessoa().equals(this.cloneNIR.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneNIR.getCpfPessoa() + " para " + this.NIR.getCpfPessoa() + ",";
            }

            if (!this.NIR.getRgPessoa().equals(this.cloneNIR.getRgPessoa())) {
                detalhe += " rg de " + this.cloneNIR.getRgPessoa() + " para " + this.NIR.getRgPessoa() + ",";
            }

            if (!this.NIR.getSexoPessoa().equals(this.cloneNIR.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneNIR.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + " para "
                        + (this.NIR.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + ",";
            }

            if (!this.NIR.getDataNascimentoPessoa().equals(this.cloneNIR.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + NIRDataUtil.formatarData(this.cloneNIR.getDataNascimentoPessoa()) + " para " + NIRDataUtil.formatarData(this.NIR.getDataNascimentoPessoa()) + ",";
            }

            if (!this.NIR.getTelefonePessoa().equals(this.cloneNIR.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneNIR.getTelefonePessoa() + " para " + this.NIR.getTelefonePessoa() + ",";
            }

            if (!this.NIR.getCelularPessoa().equals(this.cloneNIR.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneNIR.getCelularPessoa() + " para " + this.NIR.getCelularPessoa() + ",";
            }

            if (!this.NIR.getEnderecoPessoa().equals(this.cloneNIR.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneNIR.getEnderecoPessoa() + " para " + this.NIR.getEnderecoPessoa() + ",";
            }

            if (!this.NIR.getNumeroPessoa().equals(this.cloneNIR.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneNIR.getNumeroPessoa() + " para " + this.NIR.getNumeroPessoa() + ",";
            }

            if (this.NIR.getComplementoPessoa() != null) {
                if (!this.NIR.getComplementoPessoa().equals(this.cloneNIR.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneNIR.getComplementoPessoa() + " para " + this.NIR.getComplementoPessoa() + ",";
                }
            }

            if (!this.NIR.getBairroPessoa().equals(this.cloneNIR.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneNIR.getBairroPessoa() + " para " + this.NIR.getBairroPessoa() + ",";
            }

            if (!this.NIR.getCepPessoa().equals(this.cloneNIR.getCepPessoa())) {
                detalhe += " cep de " + this.cloneNIR.getCepPessoa() + " para " + this.NIR.getCepPessoa() + ",";
            }

            if (!this.NIR.getEmailPessoa().equals(this.cloneNIR.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneNIR.getEmailPessoa() + " para " + this.NIR.getEmailPessoa() + ",";
            }

            if (!this.NIR.getStatusNir().equals(this.cloneNIR.getStatusNir())) {
                detalhe += " status de "
                        + (this.cloneNIR.getStatusNir().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + " para "
                        + (this.NIR.getStatusNir().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + ",";
            }

            if (!this.NIR.getEstado().getNomeEstado().equals(this.cloneNIR.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneNIR.getEstado().getNomeEstado() + " para " + this.NIR.getEstado().getNomeEstado() + ",";
            }

            if (!this.NIR.getCidade().getNomeCidade().equals(this.cloneNIR.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneNIR.getCidade().getNomeCidade() + " para " + this.NIR.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";
        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("nir");
        this.log.setIdObjeto(this.NIR.getIdPessoa());
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public Boolean isEditar() {
        return this.NIR.getIdPessoa() != null;
    }

    @Override
    public void excluir() {}

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("nir");
        return this.log != null ? "Última modificação feita em " + NIRDataUtil.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("nir", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioNIR";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.listaNIR);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public void listaCidades(AjaxBehaviorEvent event) {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.NIR.getEstado());
    }

    public NIR getNIR() {
        return NIR;
    }

    public void setNIR(NIR nir) {
        this.NIR = nir;
    }

    public List<NIR> getListaNIRFiltrada() {
        return listaNIRFiltrada;
    }

    public void setListaNIRFiltrada(List<NIR> listaNIRFiltrada) {
        this.listaNIRFiltrada = listaNIRFiltrada;
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

    public List<NIR> getListaNIR() {
        return listaNIR;
    }

    public Date getMaxDate() {
       return NIRDataUtil.gerarDataMaiorDeIdade();
    }

    public Usuario getUsuarioNIR() {
        return usuarioNIR;
    }

    public void setUsuarioNIR(Usuario usuarioNIR) {
        this.usuarioNIR = usuarioNIR;
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
