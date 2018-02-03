/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import br.com.gestaohospitalar.nir.model.sigtap.TB_PROCEDIMENTO;
import br.com.gestaohospitalar.nir.model.sigtap.TB_CID;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "internacao")
public class Internacao implements Serializable {

    private Integer idInternacao;
    private Date dataEntrada;
    private Date dataPrevisaoAlta;
    private Date dataAlta;
    private Date dataSaidaLeito;
    private Integer codigoInternacaoHospital;
    private String statusInternacao;
    private Date dataHoraLimiteVerde;
    private Date dataHoraLimiteAmarelo;
    private Date dataHoraLimiteVermelho;
    private Paciente paciente;
    private Medico medico;
    private Leito leito;
    private TB_PROCEDIMENTO procedimento;
    private String chaveMesAnoProcedimento; //Atributo é a chaveMesAno para usar como parâmetro para as tabelas SigTap, porém não foi definiido como chave estrangeira, apenas no BD está como chave estrangeira da TB_PROCEDIMENTO
    private TB_CID cid;
    private String chaveMesAnoCID; //Atributo é a chaveMesAno para usar como parâmetro para as tabelas SigTap, porém não foi definiido como chave estrangeira, apenas no BD está como chave estrangeira da TB_CID

    public Internacao() {
    }

    public Internacao(Integer idInternacao, Date dataPrevisaoAlta, Date dataEntrada, Date dataAlta, Date dataSaidaLeito, Integer codigoInternacaoHospital, String statusInternacao, Date dataHoraLimiteVerde, Date dataHoraLimiteAmarelo, Date dataHoraLimiteVermelho, Paciente paciente, Medico medico, Leito leito, TB_PROCEDIMENTO procedimento, String chaveMesAnoProcedimento, TB_CID cid, String chaveMesAnoCID) {
        this.idInternacao = idInternacao;
        this.dataEntrada = dataEntrada;
        this.dataPrevisaoAlta = dataPrevisaoAlta;
        this.dataAlta = dataAlta;
        this.dataSaidaLeito = dataSaidaLeito;
        this.codigoInternacaoHospital = codigoInternacaoHospital;
        this.statusInternacao = statusInternacao;
        this.dataHoraLimiteVerde = dataHoraLimiteVerde;
        this.dataHoraLimiteAmarelo = dataHoraLimiteAmarelo;
        this.dataHoraLimiteVermelho = dataHoraLimiteVermelho;
        this.paciente = paciente;
        this.medico = medico;
        this.leito = leito;
        this.procedimento = procedimento;
        this.chaveMesAnoProcedimento = chaveMesAnoProcedimento;
        this.cid = cid;
        this.chaveMesAnoCID = chaveMesAnoCID;
    }

    /**
     * @return the idInternacao
     */
    @Id
    @GeneratedValue
    public Integer getIdInternacao() {
        return idInternacao;
    }

    /**
     * @param idInternacao the idInternacao to set
     */
    public void setIdInternacao(Integer idInternacao) {
        this.idInternacao = idInternacao;
    }

    /**
     * @return the dataEntrada
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataEntrada() {
        return dataEntrada;
    }

    /**
     * @param dataEntrada the dataEntrada to set
     */
    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    /**
     * @return the dataPrevisaoAlta
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataPrevisaoAlta() {
        return dataPrevisaoAlta;
    }

    /**
     * @param dataPrevisaoAlta the dataPrevisaoAlta to set
     */
    public void setDataPrevisaoAlta(Date dataPrevisaoAlta) {
        this.dataPrevisaoAlta = dataPrevisaoAlta;
    }

    /**
     * @return the dataAlta
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataAlta() {
        return dataAlta;
    }

    /**
     * @param dataAlta the dataAlta to set
     */
    public void setDataAlta(Date dataAlta) {
        this.dataAlta = dataAlta;
    }

    /**
     * @return the dataSaidaLeito
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataSaidaLeito() {
        return dataSaidaLeito;
    }

    /**
     * @param dataSaidaLeito the dataSaidaLeito to set
     */
    public void setDataSaidaLeito(Date dataSaidaLeito) {
        this.dataSaidaLeito = dataSaidaLeito;
    }

    /**
     * @return the codigoInternacaoHospital
     */
    public Integer getCodigoInternacaoHospital() {
        return codigoInternacaoHospital;
    }

    /**
     * @param codigoInternacaoHospital the codigoInternacaoHospital to set
     */
    public void setCodigoInternacaoHospital(Integer codigoInternacaoHospital) {
        this.codigoInternacaoHospital = codigoInternacaoHospital;
    }

    /**
     * @return the statusInternacao
     */
    public String getStatusInternacao() {
        return statusInternacao;
    }

    /**
     * @param statusInternacao the statusInternacao to set
     */
    public void setStatusInternacao(String statusInternacao) {
        this.statusInternacao = statusInternacao;
    }

    /**
     * @return the dataHoraLimiteVerde
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHoraLimiteVerde() {
        return dataHoraLimiteVerde;
    }

    /**
     * @param dataHoraLimiteVerde the dataHoraLimiteVerde to set
     */
    public void setDataHoraLimiteVerde(Date dataHoraLimiteVerde) {
        this.dataHoraLimiteVerde = dataHoraLimiteVerde;
    }

    /**
     * @return the dataHoraLimiteAmarelo
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHoraLimiteAmarelo() {
        return dataHoraLimiteAmarelo;
    }

    /**
     * @param dataHoraLimiteAmarelo the dataHoraLimiteAmarelo to set
     */
    public void setDataHoraLimiteAmarelo(Date dataHoraLimiteAmarelo) {
        this.dataHoraLimiteAmarelo = dataHoraLimiteAmarelo;
    }

    /**
     * @return the dataHoraLimiteVermelho
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDataHoraLimiteVermelho() {
        return dataHoraLimiteVermelho;
    }

    /**
     * @param dataHoraLimiteVermelho the dataHoraLimiteVermelho to set
     */
    public void setDataHoraLimiteVermelho(Date dataHoraLimiteVermelho) {
        this.dataHoraLimiteVermelho = dataHoraLimiteVermelho;
    }

    /**
     * @return the paciente
     */
    @ManyToOne
    @JoinColumn(name = "idPaciente")
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * @return the medico
     */
    @ManyToOne
    @JoinColumn(name = "idMedico")
    public Medico getMedico() {
        return medico;
    }

    /**
     * @param medico the medico to set
     */
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    /**
     * @return the leito
     */
    @ManyToOne
    @JoinColumn(name = "idLeito")
    public Leito getLeito() {
        return leito;
    }

    /**
     * @param leito the leito to set
     */
    public void setLeito(Leito leito) {
        this.leito = leito;
    }

    /**
     * @return the procedimento
     */
    @ManyToOne
    @JoinColumn(name = "CO_PROCEDIMENTO")
    public TB_PROCEDIMENTO getProcedimento() {
        return procedimento;
    }

    /**
     * @param procedimento the procedimento to set
     */
    public void setProcedimento(TB_PROCEDIMENTO procedimento) {
        this.procedimento = procedimento;
    }

    /**
     * @return the chaveMesAnoProcedimento
     */
    public String getChaveMesAnoProcedimento() {
        return chaveMesAnoProcedimento;
    }

    /**
     * @param chaveMesAnoProcedimento the chaveMesAnoProcedimento to set
     */
    public void setChaveMesAnoProcedimento(String chaveMesAnoProcedimento) {
        this.chaveMesAnoProcedimento = chaveMesAnoProcedimento;
    }

    /**
     * @return the cid
     */
    @ManyToOne
    @JoinColumn(name = "CO_CID")
    public TB_CID getCid() {
        return cid;
    }

    /**
     * @param cid the cid to set
     */
    public void setCid(TB_CID cid) {
        this.cid = cid;
    }

    /**
     * @return the chaveMesAnoCID
     */
    public String getChaveMesAnoCID() {
        return chaveMesAnoCID;
    }

    /**
     * @param chaveMesAnoCID the chaveMesAnoCID to set
     */
    public void setChaveMesAnoCID(String chaveMesAnoCID) {
        this.chaveMesAnoCID = chaveMesAnoCID;
    }
    

    public void calcularLimiteTempoCoresKanban(ConfiguracaoKanban kanban, LocalDateTime dataEntrada) {
        
        //verde
        Long verde = Math.round(((this.procedimento.getQT_DIAS_PERMANENCIA() * 24) * kanban.getValorVerdeKanban()) / 100.0);
        LocalDateTime tempoLimiteVerde = dataEntrada.plusHours(verde);
        this.dataHoraLimiteVerde = NIRDataUtil.paraDate(tempoLimiteVerde);
        
        //amarelo
        Long amarelo = verde + Math.round(((this.procedimento.getQT_DIAS_PERMANENCIA() * 24) * kanban.getValorAmareloKanban()) / 100.0);
        LocalDateTime tempoLimiteAmarelo = dataEntrada.plusHours(amarelo);
        this.dataHoraLimiteAmarelo = NIRDataUtil.paraDate(tempoLimiteAmarelo);
        
        //vermelho
        Long vermelho = Long.valueOf(this.procedimento.getQT_DIAS_PERMANENCIA() * 24);
        LocalDateTime tempoLimiteVermelho = dataEntrada.plusHours(vermelho);
        this.dataHoraLimiteVermelho = NIRDataUtil.paraDate(tempoLimiteVermelho);
    }
    
    /**
     * Método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Internacao clone() {
        Internacao clone = new Internacao();
        clone.setIdInternacao(idInternacao);
        clone.setDataEntrada(dataEntrada);
        clone.setDataPrevisaoAlta(dataPrevisaoAlta);
        clone.setDataAlta(dataAlta);
        clone.setDataSaidaLeito(dataSaidaLeito);
        clone.setCodigoInternacaoHospital(codigoInternacaoHospital);
        clone.setStatusInternacao(statusInternacao);
        clone.setDataHoraLimiteVerde(dataHoraLimiteVerde);
        clone.setDataHoraLimiteAmarelo(dataHoraLimiteAmarelo);
        clone.setDataHoraLimiteVermelho(dataHoraLimiteVermelho);
        clone.setPaciente(paciente);
        clone.setMedico(medico);
        clone.setLeito(leito);
        clone.setProcedimento(procedimento);
        clone.setCid(cid);

        return clone;
    }

    //hashCode e equals não gerados pela IDE
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idInternacao == null) ? 0 : idInternacao.hashCode());
        return result;
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
        Internacao other = (Internacao) obj;
        if (idInternacao == null) {
            if (other.idInternacao != null) {
                return false;
            }
        } else if (!idInternacao.equals(other.idInternacao)) {
            return false;
        }
        return true;
    }

}
