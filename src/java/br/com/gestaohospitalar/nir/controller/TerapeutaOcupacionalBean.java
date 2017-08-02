/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.EstadoCidadeDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.TerapeutaOcupacionalDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Cidade;
import br.com.gestaohospitalar.nir.model.Estado;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.enumerator.Status;
import br.com.gestaohospitalar.nir.model.TerapeutaOcupacional;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
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
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public class TerapeutaOcupacionalBean implements InterfaceBean, Serializable {

    private TerapeutaOcupacionalDAOImpl daoTerapeutaOcupacional;
    private TerapeutaOcupacional terapeutaOcupacional;
    private List<TerapeutaOcupacional> terapeutasOcupacionais = new ArrayList<>();
    private List<TerapeutaOcupacional> filtrarLista;

    private EstadoCidadeDAOImpl daoEstadoCidade;
    private List<Estado> estados;
    private List<Cidade> cidades;
    private Estado estado;
    private Cidade cidade;

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private LogDAOImpl daoLog;
    private Log log;
    private List<Log> logs = new ArrayList<>();

    private TerapeutaOcupacional cloneTerapeutaOCupacional;

    /**
     * Creates a new instance of TerapeutaOcupacionalBean
     */
    public TerapeutaOcupacionalBean() {
        terapeutaOcupacional = new TerapeutaOcupacional();
    }

    @Override
    public void inicializarPaginaPesquisa() {
        this.terapeutasOcupacionais = new TerapeutaOcupacionalDAOImpl().listar();
    }

    @Override
    public void inicializarPaginaCadastro() {
        this.daoEstadoCidade = new EstadoCidadeDAOImpl();

        if (isEditar()) {
            this.cidades = this.daoEstadoCidade.listarCidades(this.terapeutaOcupacional.getEstado());
            this.cloneTerapeutaOCupacional = this.terapeutaOcupacional.clone();
            this.log.setTipo(TipoLog.ALTERACAO.get());
        } else {
            this.log.setTipo(TipoLog.INCLUSAO.get());
        }

        this.estados = this.daoEstadoCidade.listarEstados();
    }

    @Override
    public String novo() {
        this.terapeutaOcupacional = new TerapeutaOcupacional();
        return "cadastro-terapeuta-ocupacional?faces-redirect=true";
    }

    @Override
    public void salvar() {
        this.daoTerapeutaOcupacional = new TerapeutaOcupacionalDAOImpl();

        try {
            //verificando CPF
            if (ConsultaCPFValidator.verificar(this.terapeutaOcupacional, this.cloneTerapeutaOCupacional)) {
                this.terapeutaOcupacional.setStatusTerapeutaOcupacional(Status.ATIVO.get());
                this.terapeutaOcupacional.setStatusPessoa(Status.ATIVO.get());

                this.daoTerapeutaOcupacional.salvar(this.terapeutaOcupacional);

                //gravando o log
                salvarLog();

                this.terapeutaOcupacional = new TerapeutaOcupacional();

                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Terapeuta Ocupacional salvo com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean isEditar() {
        return this.terapeutaOcupacional.getIdPessoa() != null;
    }

    @Override
    public void excluir() {
        this.daoTerapeutaOcupacional = new TerapeutaOcupacionalDAOImpl();

        try {

            this.terapeutaOcupacional.setStatusPessoa(Status.INATIVO.get());
            this.terapeutaOcupacional.setStatusTerapeutaOcupacional(Status.INATIVO.get());

            this.daoTerapeutaOcupacional.salvar(this.terapeutaOcupacional);

            //atualizando a lista de Terapeutas Ocupacionais
            this.terapeutasOcupacionais.remove(this.terapeutaOcupacional);

            //gravando o log
            this.log.setTipo(TipoLog.INATIVACAO.get());
            salvarLog();

            this.terapeutaOcupacional = new TerapeutaOcupacional();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Terapeuta Ocupacional inativado com sucesso!");

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
            if (!this.terapeutaOcupacional.getNomePessoa().equals(this.cloneTerapeutaOCupacional.getNomePessoa())) {
                detalhe += " nome de " + this.cloneTerapeutaOCupacional.getNomePessoa() + " para " + this.terapeutaOcupacional.getNomePessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getCrefitoTerapeutaOcupacional().equals(this.cloneTerapeutaOCupacional.getCrefitoTerapeutaOcupacional())) {
                detalhe += " crefito de " + this.cloneTerapeutaOCupacional.getCrefitoTerapeutaOcupacional() + " para " + this.terapeutaOcupacional.getCrefitoTerapeutaOcupacional() + ",";
            }

            if (!this.terapeutaOcupacional.getCpfPessoa().equals(this.cloneTerapeutaOCupacional.getCpfPessoa())) {
                detalhe += " cpf de " + this.cloneTerapeutaOCupacional.getCpfPessoa() + " para " + this.terapeutaOcupacional.getCpfPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getRgPessoa().equals(this.cloneTerapeutaOCupacional.getRgPessoa())) {
                detalhe += " rg de " + this.cloneTerapeutaOCupacional.getRgPessoa() + " para " + this.terapeutaOcupacional.getRgPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getSexoPessoa().equals(this.cloneTerapeutaOCupacional.getSexoPessoa())) {
                detalhe += " sexo de "
                        + (this.cloneTerapeutaOCupacional.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + " para "
                        + (this.terapeutaOcupacional.getSexoPessoa().equals("M") ? " masculino " : " feminino ")
                        + ",";
            }

            if (!this.terapeutaOcupacional.getDataNascimentoPessoa().equals(this.cloneTerapeutaOCupacional.getDataNascimentoPessoa())) {
                detalhe += " data de nascimento de " + ConverterDataHora.formatarData(this.cloneTerapeutaOCupacional.getDataNascimentoPessoa()) + " para " + ConverterDataHora.formatarData(this.terapeutaOcupacional.getDataNascimentoPessoa()) + ",";
            }

            if (!this.terapeutaOcupacional.getTelefonePessoa().equals(this.cloneTerapeutaOCupacional.getTelefonePessoa())) {
                detalhe += " telefone de " + this.cloneTerapeutaOCupacional.getTelefonePessoa() + " para " + this.terapeutaOcupacional.getTelefonePessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getCelularPessoa().equals(this.cloneTerapeutaOCupacional.getCelularPessoa())) {
                detalhe += " celular de " + this.cloneTerapeutaOCupacional.getCelularPessoa() + " para " + this.terapeutaOcupacional.getCelularPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getEnderecoPessoa().equals(this.cloneTerapeutaOCupacional.getEnderecoPessoa())) {
                detalhe += " endereço de " + this.cloneTerapeutaOCupacional.getEnderecoPessoa() + " para " + this.terapeutaOcupacional.getEnderecoPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getNumeroPessoa().equals(this.cloneTerapeutaOCupacional.getNumeroPessoa())) {
                detalhe += " número de " + this.cloneTerapeutaOCupacional.getNumeroPessoa() + " para " + this.terapeutaOcupacional.getNumeroPessoa() + ",";
            }

            if (this.terapeutaOcupacional.getComplementoPessoa() != null) {
                if (!this.terapeutaOcupacional.getComplementoPessoa().equals(this.cloneTerapeutaOCupacional.getComplementoPessoa())) {
                    detalhe += " complemento de " + this.cloneTerapeutaOCupacional.getComplementoPessoa() + " para " + this.terapeutaOcupacional.getComplementoPessoa() + ",";
                }
            }

            if (!this.terapeutaOcupacional.getBairroPessoa().equals(this.cloneTerapeutaOCupacional.getBairroPessoa())) {
                detalhe += " bairro de " + this.cloneTerapeutaOCupacional.getBairroPessoa() + " para " + this.terapeutaOcupacional.getBairroPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getCepPessoa().equals(this.cloneTerapeutaOCupacional.getCepPessoa())) {
                detalhe += " cep de " + this.cloneTerapeutaOCupacional.getCepPessoa() + " para " + this.terapeutaOcupacional.getCepPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getEmailPessoa().equals(this.cloneTerapeutaOCupacional.getEmailPessoa())) {
                detalhe += " e-mail de " + this.cloneTerapeutaOCupacional.getEmailPessoa() + " para " + this.terapeutaOcupacional.getEmailPessoa() + ",";
            }

            if (!this.terapeutaOcupacional.getStatusTerapeutaOcupacional().equals(this.cloneTerapeutaOCupacional.getStatusTerapeutaOcupacional())) {
                detalhe += " status de "
                        + (this.cloneTerapeutaOCupacional.getStatusTerapeutaOcupacional().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + " para "
                        + (this.terapeutaOcupacional.getStatusTerapeutaOcupacional().equals(Status.ATIVO.get()) ? " ativo " : " inativo ")
                        + ",";
            }

            if (!this.terapeutaOcupacional.getEstado().getNomeEstado().equals(this.cloneTerapeutaOCupacional.getEstado().getNomeEstado())) {
                detalhe += " estado de " + this.cloneTerapeutaOCupacional.getEstado().getNomeEstado() + " para " + this.terapeutaOcupacional.getEstado().getNomeEstado() + ",";
            }

            if (!this.terapeutaOcupacional.getCidade().getNomeCidade().equals(this.cloneTerapeutaOCupacional.getCidade().getNomeCidade())) {
                detalhe += " cidade de " + this.cloneTerapeutaOCupacional.getCidade().getNomeCidade() + " para " + this.terapeutaOcupacional.getCidade().getNomeCidade() + ",";
            }

            //removendo última vírgula e adicionando ponto final
            detalhe = detalhe.substring(0, detalhe.length() - 1).trim() + ".";

        }

        //passando as demais informações 
        this.log.setDataHora(new Date());
        this.log.setDetalhe(detalhe);
        this.log.setObjeto("terapeutaOcupacional");
        this.log.setIdObjeto(this.terapeutaOcupacional.getIdPessoa());
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    @Override
    public void gerarRelatorio() {
        String nomeRelatorio = "relatorioTerapeutaOcupacional";
        GerarRelatorio relatorio = new GerarRelatorio();

        List<Object> listaObjetos = new ArrayList<>(this.terapeutasOcupacionais);
        relatorio.getRelatorio(listaObjetos, nomeRelatorio);
    }

    @Override
    public String ultimoLog() {
        this.log = new LogDAOImpl().ultimoLogPorObjeto("terapeutaOcupacional");
        return this.log != null ? "Última modificação feita em " + ConverterDataHora.formatarDataHora(this.getLog().getDataHora()) + " por " + this.getLog().getUsuario().getLogin() + "." : "";
    }

    @Override
    public void gerarLogs() {
        this.logs = new LogDAOImpl().listarPorIdObjeto("terapeutaOcupacional", this.terapeutaOcupacional.getIdPessoa());
    }

    /**
     * método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public TerapeutaOcupacional clone() {
        TerapeutaOcupacional clone = new TerapeutaOcupacional();
        clone.setIdPessoa(this.terapeutaOcupacional.getIdPessoa());
        clone.setCpfPessoa(this.terapeutaOcupacional.getCpfPessoa());

        return clone;
    }

    /**
     * Método que aciona o método que lista cidades passando o id do estado para
     * exibir na página
     *
     * @param event
     */
    public void listaCidades(AjaxBehaviorEvent event) {
        this.cidades = new EstadoCidadeDAOImpl().listarCidades(terapeutaOcupacional.getEstado());
    }

    /**
     * @return the terapeutaOcupacional
     */
    public TerapeutaOcupacional getTerapeutaOcupacional() {
        return terapeutaOcupacional;
    }

    /**
     * @param terapeutaOcupacional the terapeutaOcupacional to set
     */
    public void setTerapeutaOcupacional(TerapeutaOcupacional terapeutaOcupacional) {
        this.terapeutaOcupacional = terapeutaOcupacional;
    }

    public List<TerapeutaOcupacional> getFiltrarLista() {
        return filtrarLista;
    }

    public void setFiltrarLista(List<TerapeutaOcupacional> filtrarLista) {
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
     * @return the terapeutasOcupacionais
     */
    public List<TerapeutaOcupacional> getTerapeutasOcupacionais() {
        return terapeutasOcupacionais;
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
