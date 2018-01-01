/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.ParametrosDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SigtapUploadDAOImpl;
import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.Paciente;
import br.com.gestaohospitalar.nir.model.Parametros;
import br.com.gestaohospitalar.nir.model.sigtap.TB_CID;
import br.com.gestaohospitalar.nir.model.sigtap.TB_PROCEDIMENTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Daniel
 */
@ManagedBean
@RequestScoped
public class SelecaoCidBean implements Serializable {

    private String paramPesquisa;
    private List<TB_CID> cids;

    private TB_CID cidSelecionado;
    private TB_PROCEDIMENTO procedimentoSelecionado = new TB_PROCEDIMENTO();
    private Leito leitoSelecionado = new Leito();
    private Paciente pacienteSelecionado = new Paciente();

    private String msgValidacao = "";
    private Boolean permiteLiberar = false;

    /**
     * Creates a new instance of SelecaoCIDBean
     */
    public SelecaoCidBean() {
        this.cidSelecionado = new TB_CID();
    }

    /**
     * método que abre a janela modal exibindo a página selecaoCid.xhtml
     */
    public void abrirDialogo() {
        this.cids = new ArrayList<>();
        this.paramPesquisa = "";

        Map<String, Object> opcoes = new HashMap<>();
        opcoes.put("modal", true);
        opcoes.put("resizable", false);
        opcoes.put("height", 470);
        opcoes.put("contentHeight", 470);

        RequestContext.getCurrentInstance().openDialog("selecaoCid", opcoes, null);
    }

    /**
     * método que recebe o objeto selecionado e verifica com relação a validação
     * do objeto
     *
     * @param cid
     */
    public void selecionar(TB_CID cid) {

        //passando o cid selecionado 
        this.cidSelecionado = cid;

        //se passar na validação
        if (validar()) {
            liberar(); //libera o cid para a internação
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

        //cria um map com o cid selecionado e com a mensagem da validação para o log
        Map<String, TB_CID> cid = new TreeMap<>();

        if (this.msgValidacao.length() > 0) {
            this.msgValidacao = "mensagem emitida pelo sistema: " + this.msgValidacao + " [usuário selecionou opção sim]";
        }

        cid.put(this.msgValidacao, this.cidSelecionado);

        //fecha o dialog e devolve o objeto selecionado
        RequestContext.getCurrentInstance().closeDialog(cid);

        //limpando variáveis
        this.cids = new ArrayList<>();
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

        //verificando se o tipo de sexo aceito pelo leito está incompatível com o tipo de sexo aceito para o cid selecionado
        if (!this.cidSelecionado.getTP_SEXO().equals("N")) {
            if (!this.cidSelecionado.getTP_SEXO().equals("I") && !this.leitoSelecionado.getTipoSexo().equals(this.cidSelecionado.getTP_SEXO())) {

                //montando a mensagem
                msg += "Tipo de sexo do cid (" + this.cidSelecionado.getTP_SEXO() + ")"
                        + " está incompatível com o tipo de sexo aceito pelo leito ("
                        + this.leitoSelecionado.getTipoSexo() + "). ";

                validacao++;
            }
        }

        //verificando se o sexo do paciente está incompatível com o tipo de sexo aceito para o cid selecionado
        if (!this.cidSelecionado.getTP_SEXO().equals("N")) {
            if (!this.cidSelecionado.getTP_SEXO().equals("I") && !this.pacienteSelecionado.getSexoPessoa().equals(this.procedimentoSelecionado.getTP_SEXO())) {

                //montando a mensagem
                msg += "Sexo do paciente (" + this.pacienteSelecionado.getSexoPessoa() + ")"
                        + " está incompatível com o tipo de sexo aceito pelo cid ("
                        + this.cidSelecionado.getTP_SEXO() + "). ";

                validacao++;
            }
        }

        if (validacao == 0) {
            //se passou na validação apenas retorna true
            return true;
        } else {
            //senão passou na validação
            //checando os parâmetros    
            Parametros parametros = new ParametrosDAOImpl().porIdHospital(1); //passando o id do hospital manualmente
            this.permiteLiberar = parametros.getCidIncompativelLeito() && parametros.getCidIncompativelPaciente();
            this.msgValidacao = this.permiteLiberar == true ? (msg + " Deseja continuar?") : this.permiteLiberar == false ? msg : "";
            return false;
        }
    }

    /**
     * @return this.cids
     */
    public List<TB_CID> listarCid() {
        this.cids = new SigtapUploadDAOImpl().listarCidPorProcedimento(this.paramPesquisa, this.procedimentoSelecionado.getCO_PROCEDIMENTO());
        return this.cids;
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
     * @return the cids
     */
    public List<TB_CID> getCids() {
        return cids;
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
     * @return the procedimentoSelecionado
     */
    public TB_PROCEDIMENTO getProcedimentoSelecionado() {
        return procedimentoSelecionado;
    }

    /**
     * @param procedimentoSelecionado the procedimentoSelecionado to set
     */
    public void setProcedimentoSelecionado(TB_PROCEDIMENTO procedimentoSelecionado) {
        this.procedimentoSelecionado = procedimentoSelecionado;
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
