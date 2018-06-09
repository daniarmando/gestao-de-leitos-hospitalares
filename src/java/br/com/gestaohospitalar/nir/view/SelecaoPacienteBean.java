/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

import br.com.gestaohospitalar.nir.DAO.PacienteDAOImpl;
import br.com.gestaohospitalar.nir.DAO.ParametrosDAOImpl;
import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.Leito;
import br.com.gestaohospitalar.nir.model.Paciente;
import br.com.gestaohospitalar.nir.model.Parametros;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
public class SelecaoPacienteBean implements Serializable {

    private String paramPesquisa;
    private List<Paciente> pacientes = new ArrayList<>();

    private Paciente pacienteSelecionado;
    private Leito leitoSelecionado = new Leito();

    private String msgValidacao = "";
    private Boolean permiteLiberar = false;

    /**
     * Creates a new instance of SelecaoPaciente
     */
    public SelecaoPacienteBean() {
        this.pacienteSelecionado = new Paciente();
    }

    /**
     * método que abre a janela modal exibindo a página selecaoPaciente.xhtml
     */
    public void abrirDialogo() {
        this.pacientes = new ArrayList<>();
        this.paramPesquisa = "";

        Map<String, Object> opcoes = new HashMap<>();
        opcoes.put("modal", true);
        opcoes.put("resizable", false);
        opcoes.put("height", 470);
        opcoes.put("contentHeight", 470);

        RequestContext.getCurrentInstance().openDialog("selecaoPaciente", opcoes, null);
    }

    /**
     * método que recebe o objeto selecionado e verifica com relação a validação
     * do objeto
     *
     * @param paciente
     */
    public void selecionar(Paciente paciente) {
        //passando o paciente selecionado 
        this.pacienteSelecionado = paciente;

        //se passar na validação
        if (validar()) {
            //libera o paciente para a internação e passa false pois não vai gerar log da validação
            liberar(); 
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

        //cria um map com o paciente selecionado e com a mensagem da validação para o log
        Map<String, Paciente> paciente = new TreeMap<>();
        
        if (this.msgValidacao.length() > 0){
           this.msgValidacao = "mensagem emitida pelo sistema: " + this.msgValidacao + " [usuário selecionou opção sim]";
        }
        
        paciente.put(this.msgValidacao, this.pacienteSelecionado);
        

        //fecha o dialog e devolve o objeto selecionado
        RequestContext.getCurrentInstance().closeDialog(paciente);

        //limpando variáveis
        this.pacientes = new ArrayList<>();
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
        int idade = NIRDataUtil.calcularIdade(this.pacienteSelecionado.getDataNascimentoPessoa());

        //se a idade do paciente está incompatível com a idade mínima e máxima aceitas para o leito selecionado
        if (idade < this.leitoSelecionado.getIdadeMinima() || idade > this.leitoSelecionado.getIdadeMaxima()) {

            //montando a mensagem
            msg += "Idade do paciente de " + idade + " ano(s)"
                    + " não está dentro da faixa de idade aceita pelo leito de "
                    + this.leitoSelecionado.getIdadeMinima()
                    + " a "
                    + this.leitoSelecionado.getIdadeMaxima()
                    + " ano(s). ";

            validacao++;
        }

        //verificando se o sexo do paciente está incompatível com o tipo de sexo aceito para o leito selecionado
        if (!this.leitoSelecionado.getTipoSexo().equals("I") && !this.pacienteSelecionado.getSexoPessoa().equals(this.leitoSelecionado.getTipoSexo())) {

            //montando a mensagem
            msg += "Sexo do paciente (" + this.pacienteSelecionado.getSexoPessoa() + ")"
                    + " está incompatível com o tipo de sexo aceito pelo leito ("
                    + this.leitoSelecionado.getTipoSexo() + ").";

            validacao++;
        }

        if (validacao == 0) {
            //se passou na validação apenas retorna true
            return true;
        } else {
            //se não passou na validação
            //checando o parâmetro
            Parametros parametros = new ParametrosDAOImpl().porIdHospital(1); //passando o id do hospital manualmente
            this.permiteLiberar = parametros.getLeitoIncompativelPaciente();
            this.msgValidacao = this.permiteLiberar == true ? (msg + " Deseja continuar?") : this.permiteLiberar == false ? msg : "";
            return false;
        }
    }

     /**
      * método que calcula a idade do paciente
      * 
     * @param dataNascimento
     * @return idadePaciente
     */
    public int idadePaciente(Date dataNascimento) {
        return NIRDataUtil.calcularIdade(dataNascimento);
    }

    /**
     * @return this.pacientes
     */
    public List<Paciente> pesquisarPaciente() {
        this.pacientes = new PacienteDAOImpl().ativosPorNomeOuCodigoSusOuCPF(this.paramPesquisa);
        return this.pacientes;
    }

    /**
     * @return the paramPesquisa
     */
    public String getParamPesquisa() {
        return paramPesquisa;
    }

    /**
     * @param paramPesquisa the paramPesquisa to set
     */
    public void setParamPesquisa(String paramPesquisa) {
        this.paramPesquisa = paramPesquisa;
    }

    /**
     * @return the pacientes
     */
    public List<Paciente> getPacientes() {
        return pacientes;
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
