/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model.sigtap;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "tb_procedimento")
public class TB_PROCEDIMENTO implements Serializable {

    private String CO_PROCEDIMENTO;
    private String NO_PROCEDIMENTO;
    private String TP_COMPLEXIDADE;
    private String TP_SEXO;
    private Integer QT_MAXIMA_EXECUCAO;
    private Integer QT_DIAS_PERMANENCIA;
    private Integer QT_PONTOS;
    private Integer VL_IDADE_MINIMA;
    private Integer VL_IDADE_MAXIMA;
    private Double VL_SH;
    private Double VL_SA;
    private Double VL_SP;
    private Date DT_COMPETENCIA;
    private TB_FINANCIAMENTO tb_financiamento;
    private TB_RUBRICA tb_rubrica;
    private String chaveMesAno;

    public TB_PROCEDIMENTO() {
    }

    public TB_PROCEDIMENTO(String CO_PROCEDIMENTO, String NO_PROCEDIMENTO, String TP_COMPLEXIDADE, String TP_SEXO, Integer QT_MAXIMA_EXECUCAO, Integer QT_DIAS_PERMANENCIA, Integer QT_PONTOS, Integer VL_IDADE_MINIMA, Integer VL_IDADE_MAXIMA, Double VL_SH, Double VL_SA, Double VL_SP, Date DT_COMPETENCIA, TB_FINANCIAMENTO tb_financiamento, TB_RUBRICA tb_rubrica, String chaveMesAno) {
        this.CO_PROCEDIMENTO = CO_PROCEDIMENTO;
        this.NO_PROCEDIMENTO = NO_PROCEDIMENTO;
        this.TP_COMPLEXIDADE = TP_COMPLEXIDADE;
        this.TP_SEXO = TP_SEXO;
        this.QT_MAXIMA_EXECUCAO = QT_MAXIMA_EXECUCAO;
        this.QT_DIAS_PERMANENCIA = QT_DIAS_PERMANENCIA;
        this.QT_PONTOS = QT_PONTOS;
        this.VL_IDADE_MINIMA = VL_IDADE_MINIMA;
        this.VL_IDADE_MAXIMA = VL_IDADE_MAXIMA;
        this.VL_SH = VL_SH;
        this.VL_SA = VL_SA;
        this.VL_SP = VL_SP;
        this.DT_COMPETENCIA = DT_COMPETENCIA;
        this.tb_financiamento = tb_financiamento;
        this.tb_rubrica = tb_rubrica;
        this.chaveMesAno = chaveMesAno;
    }

    /**
     * @return the CO_PROCEDIMENTO
     */
    @Id
    public String getCO_PROCEDIMENTO() {
        return CO_PROCEDIMENTO;
    }

    /**
     * @param CO_PROCEDIMENTO the CO_PROCEDIMENTO to set
     */
    public void setCO_PROCEDIMENTO(String CO_PROCEDIMENTO) {
        this.CO_PROCEDIMENTO = CO_PROCEDIMENTO;
    }

    /**
     * @return the NO_PROCEDIMENTO
     */
    public String getNO_PROCEDIMENTO() {
        return NO_PROCEDIMENTO;
    }

    /**
     * @param NO_PROCEDIMENTO the NO_PROCEDIMENTO to set
     */
    public void setNO_PROCEDIMENTO(String NO_PROCEDIMENTO) {
        //Formatando a descrição do procedimento para melhor leitura
        NO_PROCEDIMENTO = NO_PROCEDIMENTO.substring(0, 1).toUpperCase().concat(NO_PROCEDIMENTO.substring(1).toLowerCase());
        this.NO_PROCEDIMENTO = NO_PROCEDIMENTO;
    }

    /**
     * @return the TP_COMPLEXIDADE
     */
    public String getTP_COMPLEXIDADE() {
        return TP_COMPLEXIDADE;
    }

    /**
     * @param TP_COMPLEXIDADE the TP_COMPLEXIDADE to set
     */
    public void setTP_COMPLEXIDADE(String TP_COMPLEXIDADE) {
        this.TP_COMPLEXIDADE = TP_COMPLEXIDADE;
    }

    /**
     * @return the TP_SEXO
     */
    public String getTP_SEXO() {
        return TP_SEXO;
    }

    /**
     * @param TP_SEXO the TP_SEXO to set
     */
    public void setTP_SEXO(String TP_SEXO) {
        this.TP_SEXO = TP_SEXO;
    }

    /**
     * @return the QT_MAXIMA_EXECUCAO
     */
    public Integer getQT_MAXIMA_EXECUCAO() {
        return QT_MAXIMA_EXECUCAO;
    }

    /**
     * @param QT_MAXIMA_EXECUCAO the QT_MAXIMA_EXECUCAO to set
     */
    public void setQT_MAXIMA_EXECUCAO(Integer QT_MAXIMA_EXECUCAO) {
        this.QT_MAXIMA_EXECUCAO = QT_MAXIMA_EXECUCAO;
    }

    /**
     * @return the QT_DIAS_PERMANENCIA
     */
    public Integer getQT_DIAS_PERMANENCIA() {
        return QT_DIAS_PERMANENCIA;
    }

    /**
     * @param QT_DIAS_PERMANENCIA the QT_DIAS_PERMANENCIA to set
     */
    public void setQT_DIAS_PERMANENCIA(Integer QT_DIAS_PERMANENCIA) {
        this.QT_DIAS_PERMANENCIA = QT_DIAS_PERMANENCIA;
    }

    /**
     * @return the QT_PONTOS
     */
    public Integer getQT_PONTOS() {
        return QT_PONTOS;
    }

    /**
     * @param QT_PONTOS the QT_PONTOS to set
     */
    public void setQT_PONTOS(Integer QT_PONTOS) {
        this.QT_PONTOS = QT_PONTOS;
    }

    /**
     * @return the VL_IDADE_MINIMA
     */
    public Integer getVL_IDADE_MINIMA() {
        return VL_IDADE_MINIMA;
    }

    /**
     * @param VL_IDADE_MINIMA the VL_IDADE_MINIMA to set
     */
    public void setVL_IDADE_MINIMA(Integer VL_IDADE_MINIMA) {
        this.VL_IDADE_MINIMA = VL_IDADE_MINIMA;
    }

    /**
     * @return the VL_IDADE_MAXIMA
     */
    public Integer getVL_IDADE_MAXIMA() {
        return VL_IDADE_MAXIMA;
    }

    /**
     * @param VL_IDADE_MAXIMA the VL_IDADE_MAXIMA to set
     */
    public void setVL_IDADE_MAXIMA(Integer VL_IDADE_MAXIMA) {
        this.VL_IDADE_MAXIMA = VL_IDADE_MAXIMA;
    }

    /**
     * @return the VL_SH
     */
    public Double getVL_SH() {
        return VL_SH;
    }

    /**
     * @param VL_SH the VL_SH to set
     */
    public void setVL_SH(Double VL_SH) {
        this.VL_SH = VL_SH;
    }

    /**
     * @return the VL_SA
     */
    public Double getVL_SA() {
        return VL_SA;
    }

    /**
     * @param VL_SA the VL_SA to set
     */
    public void setVL_SA(Double VL_SA) {
        this.VL_SA = VL_SA;
    }

    /**
     * @return the VL_SP
     */
    public Double getVL_SP() {
        return VL_SP;
    }

    /**
     * @param VL_SP the VL_SP to set
     */
    public void setVL_SP(Double VL_SP) {
        this.VL_SP = VL_SP;
    }

    /**
     * @return the DT_COMPETENCIA
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getDT_COMPETENCIA() {
        return DT_COMPETENCIA;
    }

    /**
     * @param DT_COMPETENCIA the DT_COMPETENCIA to set
     */
    public void setDT_COMPETENCIA(Date DT_COMPETENCIA) {
        this.DT_COMPETENCIA = DT_COMPETENCIA;
    }

    /**
     * @return the tb_financiamento
     */
    @ManyToOne
    @JoinColumn(name = "CO_FINANCIAMENTO")
    public TB_FINANCIAMENTO getTb_financiamento() {
        return tb_financiamento;
    }

    /**
     * @param tb_financiamento the tb_financiamento to set
     */
    public void setTb_financiamento(TB_FINANCIAMENTO tb_financiamento) {
        this.tb_financiamento = tb_financiamento;
    }

    /**
     * @return the tb_rubrica
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "CO_RUBRICA")
    public TB_RUBRICA getTb_rubrica() {
        return tb_rubrica;
    }

    /**
     * @param tb_rubrica the tb_rubrica to set
     */
    public void setTb_rubrica(TB_RUBRICA tb_rubrica) {
        this.tb_rubrica = tb_rubrica;
    }

    /**
     * @return the chaveMesAno
     */
    public String getChaveMesAno() {
        return chaveMesAno;
    }

    /**
     * @param chaveMesAno the chaveMesAno to set
     */
    public void setChaveMesAno(String chaveMesAno) {
        this.chaveMesAno = chaveMesAno;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.CO_PROCEDIMENTO);
        hash = 47 * hash + Objects.hashCode(this.NO_PROCEDIMENTO);
        hash = 47 * hash + Objects.hashCode(this.TP_COMPLEXIDADE);
        hash = 47 * hash + Objects.hashCode(this.TP_SEXO);
        hash = 47 * hash + Objects.hashCode(this.QT_MAXIMA_EXECUCAO);
        hash = 47 * hash + Objects.hashCode(this.QT_DIAS_PERMANENCIA);
        hash = 47 * hash + Objects.hashCode(this.QT_PONTOS);
        hash = 47 * hash + Objects.hashCode(this.VL_IDADE_MINIMA);
        hash = 47 * hash + Objects.hashCode(this.VL_IDADE_MAXIMA);
        hash = 47 * hash + Objects.hashCode(this.VL_SH);
        hash = 47 * hash + Objects.hashCode(this.VL_SA);
        hash = 47 * hash + Objects.hashCode(this.VL_SP);
        hash = 47 * hash + Objects.hashCode(this.DT_COMPETENCIA);
        hash = 47 * hash + Objects.hashCode(this.tb_financiamento);
        hash = 47 * hash + Objects.hashCode(this.tb_rubrica);
        hash = 47 * hash + Objects.hashCode(this.chaveMesAno);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TB_PROCEDIMENTO other = (TB_PROCEDIMENTO) obj;
        if (!Objects.equals(this.CO_PROCEDIMENTO, other.CO_PROCEDIMENTO)) {
            return false;
        }
        if (!Objects.equals(this.NO_PROCEDIMENTO, other.NO_PROCEDIMENTO)) {
            return false;
        }
        if (!Objects.equals(this.TP_COMPLEXIDADE, other.TP_COMPLEXIDADE)) {
            return false;
        }
        if (!Objects.equals(this.TP_SEXO, other.TP_SEXO)) {
            return false;
        }
        if (!Objects.equals(this.chaveMesAno, other.chaveMesAno)) {
            return false;
        }
        if (!Objects.equals(this.QT_MAXIMA_EXECUCAO, other.QT_MAXIMA_EXECUCAO)) {
            return false;
        }
        if (!Objects.equals(this.QT_DIAS_PERMANENCIA, other.QT_DIAS_PERMANENCIA)) {
            return false;
        }
        if (!Objects.equals(this.QT_PONTOS, other.QT_PONTOS)) {
            return false;
        }
        if (!Objects.equals(this.VL_IDADE_MINIMA, other.VL_IDADE_MINIMA)) {
            return false;
        }
        if (!Objects.equals(this.VL_IDADE_MAXIMA, other.VL_IDADE_MAXIMA)) {
            return false;
        }
        if (!Objects.equals(this.VL_SH, other.VL_SH)) {
            return false;
        }
        if (!Objects.equals(this.VL_SA, other.VL_SA)) {
            return false;
        }
        if (!Objects.equals(this.VL_SP, other.VL_SP)) {
            return false;
        }
        if (!Objects.equals(this.DT_COMPETENCIA, other.DT_COMPETENCIA)) {
            return false;
        }
        if (!Objects.equals(this.tb_financiamento, other.tb_financiamento)) {
            return false;
        }
        if (!Objects.equals(this.tb_rubrica, other.tb_rubrica)) {
            return false;
        }
        return true;
    }

}
