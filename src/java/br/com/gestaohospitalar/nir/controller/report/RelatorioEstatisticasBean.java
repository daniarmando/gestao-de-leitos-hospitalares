/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller.report;

import br.com.gestaohospitalar.nir.DAO.SetorDAOImpl;
import br.com.gestaohospitalar.nir.controller.UsuarioBean;
import br.com.gestaohospitalar.nir.model.Setor;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import br.com.gestaohospitalar.nir.util.report.GerarRelatorio;
import br.com.gestaohospitalar.nir.validator.PeriodoValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Daniel
 */
@ManagedBean
@RequestScoped
public class RelatorioEstatisticasBean implements Serializable {

    private List<Setor> setores = new ArrayList<>();
    private Setor setor;

    private Date dataInicial;
    private Date dataFinal;
    
    //Injetando o usuário
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    /**
     * Creates a new instance of relatorioEstatisticas
     */
    public RelatorioEstatisticasBean() {
        this.setor = new Setor();
    }

    @PostConstruct
    public void init() {
        //listando os setores
        this.setores = new SetorDAOImpl().listar();
    }

    /**
     * método para gerar o relatório
     */
    public void gerar() {

        //verificando se data inicial está menor que a data final
        if (PeriodoValidator.comparaDatas(dataInicial, dataFinal) == false) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "A data inicial não pode ser maior que a data final.");
        } else {

            //vinculando os parâmetros do relatório
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("data_inicial", this.dataInicial);
            parametros.put("data_final", this.dataFinal);
            parametros.put("id_setor", this.setor.getIdSetor());
            parametros.put("usuario", this.usuarioBean.getUsuario().getLogin());

            //passando o nome
            String nomeRelatorio = "relatorioEstatisticas";

            //gerando o relatório
            GerarRelatorio relatorio = new GerarRelatorio();
            relatorio.getRelatorio(parametros, nomeRelatorio);

            //se relatório não foi gerado
            if (relatorio.isRelatorioGerado() == false) {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Não foram encontradas informações para o relatório solicitado.");
            }
        }

    }

    /**
     * @return the setores
     */
    public List<Setor> getSetores() {
        return setores;
    }

    /**
     * @return the setor
     */
    public Setor getSetor() {
        return setor;
    }

    /**
     * @param setor the setor to set
     */
    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    /**
     * @return the dataInicial
     */
    public Date getDataInicial() {
        return dataInicial;
    }

    /**
     * @param dataInicial the dataInicial to set
     */
    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    /**
     * @return the dataFinal
     */
    public Date getDataFinal() {
        return dataFinal;
    }

    /**
     * @param dataFinal the dataFinal to set
     */
    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    //Setter necessario para a anotação @ManagedProperty funcionar corretamente
    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }
}
