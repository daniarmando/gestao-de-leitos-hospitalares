/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

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
public class FisioterapeutaBean implements InterfaceBean, Serializable {

    private FisioterapeutaDAOImpl daoFisioterapeuta;
    private Fisioterapeuta fisioterapeuta, cloneFisioterapeuta;
    private List<Fisioterapeuta> fisioterapeutas, fisioterapeutasFiltrados;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuario;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs;

    /**
     * Creates a new instance of FisioterapeutaBean
     */
    public FisioterapeutaBean() {
        this.fisioterapeuta = new Fisioterapeuta();
    }

    @Override
    public void inicializarConsulta() {
        this.daoFisioterapeuta = new FisioterapeutaDAOImpl();
        this.fisioterapeutas = this.daoFisioterapeuta.ativos();
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
        this.daoFisioterapeuta = new FisioterapeutaDAOImpl();
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.log = new Log(TipoLog.ALTERACAO.get());
        
        this.fisioterapeuta = this.daoFisioterapeuta.porId(this.fisioterapeuta.getIdPessoa());
        this.cloneFisioterapeuta = this.fisioterapeuta.clone();
        this.estados = this.daoEstadoCidade.todosEstados();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.fisioterapeuta.getEstado());
    }

    @Override
    public void salvar() {
        this.daoFisioterapeuta = new FisioterapeutaDAOImpl();

        try {

            //verificando CPF
            if (ConsultaCPFValidator.verificar(this.fisioterapeuta, this.cloneFisioterapeuta)) {
                this.fisioterapeuta.setStatusFisioterapeuta(Status.ATIVO.get());
                this.fisioterapeuta.setStatusPessoa(Status.ATIVO.get());

                this.daoFisioterapeuta.salvar(this.fisioterapeuta);

                //gravando o log
                this.salvarLog();

                this.fisioterapeuta = new Fisioterapeuta();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Fisioterapeuta salvo com sucesso!");
            }

        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.fisioterapeuta.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.daoFisioterapeuta = new FisioterapeutaDAOImpl();

        try {

            this.fisioterapeuta.setStatusPessoa(Status.INATIVO.get());
            this.fisioterapeuta.setStatusFisioterapeuta(Status.INATIVO.get());

            this.daoFisioterapeuta.salvar(this.fisioterapeuta);

            //gravando o log
            this.log = new Log(TipoLog.INATIVACAO.get());
            this.salvarLog();
            
            this.inicializarConsulta();

            this.fisioterapeuta = new Fisioterapeuta();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Fisioterapeuta inativado com sucesso!");

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
        this.log.setUsuario(this.usuario.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public String ultimoLog() {
        this.daoLog = new LogDAOImpl();
        this.log = this.daoLog.ultimoPorObjeto("fisioterapeuta");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs(int idObjeto) {
        this.daoLog = new LogDAOImpl();
        this.logs = this.daoLog.porIdObjeto("fisioterapeuta", idObjeto);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioFisioterapeuta";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.fisioterapeutas);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    public void listaCidades(AjaxBehaviorEvent event) {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();
        this.cidades = this.daoEstadoCidade.cidadesPorEstado(this.fisioterapeuta.getEstado());
    }

    public Fisioterapeuta getFisioterapeuta() {
        return fisioterapeuta;
    }

    public void setFisioterapeuta(Fisioterapeuta fisioterapeuta) {
        this.fisioterapeuta = fisioterapeuta;
    }

    public List<Fisioterapeuta> getFisioterapeutasFiltrados() {
        return fisioterapeutasFiltrados;
    }

    public void setFisioterapeutasFiltrados(List<Fisioterapeuta> fisioterapeutasFiltrados) {
        this.fisioterapeutasFiltrados = fisioterapeutasFiltrados;
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

    public List<Fisioterapeuta> getFisioterapeutas() {
        return fisioterapeutas;
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
