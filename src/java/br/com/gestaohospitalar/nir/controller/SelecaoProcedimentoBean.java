/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.ParametrosDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SigtapUploadDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.Paciente;
import br.com.gestaohospitalar.nir.model.Parametros;
import br.com.gestaohospitalar.nir.model.sigtap.TB_PROCEDIMENTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Daniel
 */
@ManagedBean
@SessionScoped
public class SelecaoProcedimentoBean implements Serializable {

    private String paramPesquisa;
    private List<TB_PROCEDIMENTO> procedimentos;

    private final SigtapUploadDAOImpl daoSU = new SigtapUploadDAOImpl();

    private TB_PROCEDIMENTO procedimentoSelecionado;
    private Leito leitoSelecionado = new Leito();
    private Paciente pacienteSelecionado = new Paciente();

    private ParametrosDAOImpl daoParametros = new ParametrosDAOImpl();

    private Integer tipoPesq = 0;

    private String msgValidacao = "";
    private Boolean permiteLiberar = false;

    /**
     * Creates a new instance of SelecaoProcedimentoBean
     */
    public SelecaoProcedimentoBean() {
        this.procedimentoSelecionado = new TB_PROCEDIMENTO();
    }

    /**
     * método que abre a janela modal exibindo a página selecaoCid.xhtml
     */
    public void abrirDialogo() {
        this.procedimentos = new ArrayList<>();
        this.paramPesquisa = "";
        Map<String, Object> opcoes = new HashMap<>();
        opcoes.put("modal", true);
        opcoes.put("resizable", false);
        opcoes.put("height", 470);
        opcoes.put("contentHeight", 470);

        RequestContext.getCurrentInstance().openDialog("selecaoProcedimento", opcoes, null);
    }

    /**
     * método que recebe o objeto selecionado e verifica com relação a validação
     * do objeto
     *
     * @param procedimento
     */
    public void selecionar(TB_PROCEDIMENTO procedimento) {
        //passando o procedimento selecionado 
        this.procedimentoSelecionado = procedimento;

        //se passar na validação
        if (validar()) {
            liberar(); //libera o paciente para a internação
        } else {
            //senão passar na validação, exibe o dialog com a mensagem da validação
            RequestContext.getCurrentInstance().execute("PF('dlgmsgValidacao').show()");
        }
    }

    /**
     * método que fecha a página aberta em janela modal e devolve o objeto
     * selecionado
     *
     */
    public void liberar() {
        
        //cria um map com o procedimento selecionado e com a mensagem da validação para o log
        Map<String, TB_PROCEDIMENTO> procedimento = new TreeMap<>();
        
        if (this.msgValidacao.length() > 0){
           this.msgValidacao = "mensagem emitida pelo sistema: " + this.msgValidacao + " [usuário selecionou opção sim]";
        }
        
        procedimento.put(this.msgValidacao, this.procedimentoSelecionado);
        
        //fecha o dialog e devolve o objeto selecionado
        RequestContext.getCurrentInstance().closeDialog(procedimento);

        //limpando variáveis
        this.procedimentos = new ArrayList<>();
        this.paramPesquisa = "";
        this.msgValidacao = "";
    }

    /**
     * método que faz as validações para o objeto selecionado
     *
     * @return
     */
    public boolean validar() {

        int validacao = 0; //passa na validação se for igual a 0 
        String msg = "";

        //calculando a idade do paciente
        int idade = ConverterDataHora.calcularIdade(this.pacienteSelecionado.getDataNascimentoPessoa());

        //calculando a idade mínima e máxima da tabela sigtap, pois o valor é em meses
        int idadeMinProc = (this.procedimentoSelecionado.getVL_IDADE_MINIMA() > 0 && this.procedimentoSelecionado.getVL_IDADE_MINIMA() < 9999 ? this.procedimentoSelecionado.getVL_IDADE_MINIMA() / 12 : this.procedimentoSelecionado.getVL_IDADE_MINIMA());
        int idadeMaxProc = (this.procedimentoSelecionado.getVL_IDADE_MAXIMA() > 0 && this.procedimentoSelecionado.getVL_IDADE_MAXIMA() < 9999 ? this.procedimentoSelecionado.getVL_IDADE_MAXIMA() / 12 : this.procedimentoSelecionado.getVL_IDADE_MAXIMA());

        //buscando os parâmetros
        Parametros parametros = this.daoParametros.parametrosPorIdHospital(1); //passando o id do hospital manualmente

        //se o parametro de liberar procedimento incompatível com tipo leito estiver como true
        if (parametros.getProcedimentoIncompativelLeito()) {
            //verificando se o tipo do leito está incompatível com o tipo de leito aceito pelo procedimento
            if (this.daoSU.verificarSeExisteProcedimento(this.procedimentoSelecionado.getCO_PROCEDIMENTO(), this.leitoSelecionado.getTipo_leito().getCO_TIPO_LEITO()) == false) {
                //montando a mensagem
                msg += "Tipo de leito aceito pelo procedimento incompatível com o leito. ";
                validacao++;
            }
        }

        //verificando se a faixa de idade do leito está incompatível com a faixa de idade aceitas para o procedimento selecionado
        if (this.procedimentoSelecionado.getVL_IDADE_MINIMA() != 9999) {
            if (idadeMinProc < this.leitoSelecionado.getIdadeMinima()
                    && idadeMaxProc < this.leitoSelecionado.getIdadeMaxima()) {

                //montando a mensagem
                msg += "Faixa de idade aceita pelo procedimento de "
                        + this.procedimentoSelecionado.getVL_IDADE_MINIMA()
                        + " a "
                        + this.procedimentoSelecionado.getVL_IDADE_MAXIMA()
                        + " ano(s)"
                        + " não está dentro da faixa de idade aceita pelo leito de "
                        + this.leitoSelecionado.getIdadeMaxima()
                        + " a "
                        + this.leitoSelecionado.getIdadeMaxima()
                        + " ano(s). ";

                validacao++;
            }
        }

        //verificando se o tipo de sexo aceito pelo leito está incompatível com o tipo de sexo aceito para o procedimento selecionado
        if (!this.procedimentoSelecionado.getTP_SEXO().equals("N")) {
            if (!this.procedimentoSelecionado.getTP_SEXO().equals("I") && !this.leitoSelecionado.getTipoSexo().equals(this.procedimentoSelecionado.getTP_SEXO())) {

                //montando a mensagem
                msg += "Tipo de sexo do procedimento (" + this.procedimentoSelecionado.getTP_SEXO() + ")"
                        + " está incompatível com o tipo de sexo aceito pelo leito ("
                        + this.leitoSelecionado.getTipoSexo() + "). ";

                validacao++;
            }
        }

        //verificando se a idade do paciente está incompatível com a idade mínima e máxima aceitas para o procedimento selecionado
        if (this.procedimentoSelecionado.getVL_IDADE_MINIMA() != 9999) {
            if (idade < idadeMinProc || idade > idadeMaxProc) {

                //montando a mensagem
                msg += "Idade do paciente de " + idade + " ano(s)"
                        + " não está dentro da faixa de idade aceita pelo procedimento de "
                        + this.procedimentoSelecionado.getVL_IDADE_MINIMA()
                        + " a "
                        + this.procedimentoSelecionado.getVL_IDADE_MAXIMA()
                        + " ano(s). ";

                validacao++;
            }
        }

        //verificando se o sexo do paciente está incompatível com o tipo de sexo aceito para o procedimento selecionado
        if (!this.procedimentoSelecionado.getTP_SEXO().equals("N")) {
            if (!this.procedimentoSelecionado.getTP_SEXO().equals("I") && !this.pacienteSelecionado.getSexoPessoa().equals(this.procedimentoSelecionado.getTP_SEXO())) {

                //montando a mensagem
                msg += "Sexo do paciente (" + this.pacienteSelecionado.getSexoPessoa() + ")"
                        + " está incompatível com o tipo de sexo aceito pelo procedimento ("
                        + this.procedimentoSelecionado.getTP_SEXO() + "). ";

                validacao++;
            }
        }

        if (validacao == 0) {
            //se passou na validação apenas retorna true
            return true;
        } else {
            //se não passou na validação
            //checando os parâmetros            
            this.permiteLiberar = parametros.getProcedimentoIncompativelLeito() && parametros.getProcedimentoIncompativelPaciente();
            this.msgValidacao = this.permiteLiberar == true ? (msg + " Deseja continuar?") : this.permiteLiberar == false ? msg : "";
            return false;
        }
    }
    
    /**
     * método que calcula idade mínima e máxima aceitas pelo procedimento
     * é feito calculo divido por 12 pois o valor que vem do procedimento
     * está em meses, 9999 = não se aplica
     * 
     * 
     * @param idadeMin
     * @param idadeMax
     * @return idadeMin/idadeMax
     */
    public String idadeMinMax(int idadeMin, int idadeMax) {
        return (idadeMin > 0 && idadeMin < 9999 ? Math.round(idadeMin / 12) : idadeMin) + "/" +  (idadeMax > 0 && idadeMax < 9999 ? Math.round(idadeMax / 12) : idadeMax);
    }

    /**
     * @return this.procedimentos
     */
    public List<TB_PROCEDIMENTO> listarProcedimentoPorLeito() {
        this.procedimentos = this.daoSU.listarProcedimentosPorTipoLeito(this.paramPesquisa, this.leitoSelecionado.getTipo_leito().getCO_TIPO_LEITO());
        this.tipoPesq = 1;
        return this.procedimentos;
    }

    /**
     * @return this.procedimentos
     */
    public List<TB_PROCEDIMENTO> listarTodosProcedimentos() {
        this.procedimentos = this.daoSU.listarTodosProcedimentos(this.paramPesquisa);
        this.tipoPesq = 2;
        return this.procedimentos;
    }

    /**
     * método para verificar o parâmetro que libera ou não procedimentos fora do
     * padrão
     *
     * @return true or false
     */
    public Boolean isParamProcedimento() {
        Parametros param = this.daoParametros.parametrosPorIdHospital(1); //passando o id hospital 1 manualmente
        return param.getProcedimentoIncompativelLeito();
    }

    /**
     * Método que mostra qual o tipo de pesquisa esta sendo feito
     *
     * @return msg
     */
    public String msgTipoPesq() {
        String msg = "";
        switch (this.tipoPesq) {
            case 0:
                return null;
            case 1:
                msg = "*Pesquisando por Procedimentos para Leitos do Tipo" + this.leitoSelecionado.getTipo_leito().getNO_TIPO_LEITO();
                break;
            case 2:
                msg = "*Pesquisando por Todos Procedimentos";
                break;
        }
        this.tipoPesq = 0;
        return msg;
    }

    /**
     * @param paramPesquisa the paramPesquisa to set
     */
    public void setParamPesquisa(String paramPesquisa) {
        this.paramPesquisa = paramPesquisa;
    }

    /**
     * @return the paramPesquisa
     */
    public String getParamPesquisa() {
        return paramPesquisa;
    }

    /**
     * @return the procedimentos
     */
    public List<TB_PROCEDIMENTO> getProcedimentos() {
        return procedimentos;
    }

    /**
     * @return the leitoSelecionado
     */
    public Leito getLeitoSelecionado() {
        return leitoSelecionado;
    }

    /**
     * @param leitoSelecionado the leitoSelecionado to set
     */
    public void setLeitoSelecionado(Leito leitoSelecionado) {
        this.leitoSelecionado = leitoSelecionado;
    }

    /**
     * @return the pacienteSelecionado
     */
    public Paciente getPacienteSelecionado() {
        return pacienteSelecionado;
    }

    /**
     * @param pacienteSelecionado the pacienteSelecionado to set
     */
    public void setPacienteSelecionado(Paciente pacienteSelecionado) {
        this.pacienteSelecionado = pacienteSelecionado;
    }

    /**
     * @return the exibeTipoPesq
     */
    public Integer getExibeTipoPesq() {
        return tipoPesq;
    }

    /**
     * @return the msgValidacao
     */
    public String getMsgValidacao() {
        return msgValidacao;
    }

    /**
     * @param msgValidacao the msgValidacao to set
     */
    public void setMsgValidacao(String msgValidacao) {
        this.msgValidacao = msgValidacao;
    }

    /**
     * @return the permiteLiberar
     */
    public Boolean getPermiteLiberar() {
        return permiteLiberar;
    }

    /**
     * @param permiteLiberar the permiteLiberar to set
     */
    public void setPermiteLiberar(Boolean permiteLiberar) {
        this.permiteLiberar = permiteLiberar;
    }

}
