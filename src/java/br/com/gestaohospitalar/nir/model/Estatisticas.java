/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classe que traz as informações de duração de tempo dos processos de internação que são armazenados
 * por triggers na tabela de estatísticas no banco de dados conforme vão ocorrendo eventos na internação,
 * esses valores representam o tempo que durou algum processo em segundos.
 * 
 * 1 dia = 86400 segundos ou 1440 minutos (para calcular a quantidade de dias: tempoEmSegundos / 86400)
 * 1 hora = 3600 segundos ou   60 minutos (para calcular a quantidade de horas: (tempoEmSegundos / 3600) - (qtdDias * 24))
 * 1 minuto = 60 segundos                 (para calcular a quantidade de minutos: (tempoEmSegundos / 60) - ((qtdDias * 1440) + (qtdHoras * 60)))
 *                                        (para calcular a quantidade de segundos: tempoEmSegundos - ((qtdDias * 86400) + (qtdHoras * 3600) + (qtdMinutos * 60))) 
 * 
 * @author Daniel
 */
@Entity
@Table(name = "estatisticas")
public class Estatisticas implements Serializable {
    
    private Integer idEstatisticas;
    private Internacao internacao;
    private Leito leito;
    private AltaQualificada altaQualificada;
    private Alta alta;
    private Higienizacao higienizacao;
    private Integer tempoInternacao;
    private Integer tempoAltaQualificadaAteAlta;
    private Integer tempoSaidaPaciente;
    private Integer tempoSaidaAteHigienizacao;
    private Integer tempoHigienizacao;
    private Integer IdInternacaoPosterior;
    private Integer tempoOciosidade;

    public Estatisticas() {
    }

    public Estatisticas(Integer idEstatisticas, Internacao internacao, Leito leito, AltaQualificada altaQualificada, Alta alta, Higienizacao higienizacao, Integer tempoInternacao, Integer tempoAltaQualificadaAteAlta, Integer tempoSaidaPaciente, Integer tempoSaidaAteHigienizacao, Integer tempoHigienizacao, Integer IdInternacaoPosterior, Integer tempoOciosidade) {
        this.idEstatisticas = idEstatisticas;
        this.internacao = internacao;
        this.leito = leito;
        this.altaQualificada = altaQualificada;
        this.alta = alta;
        this.higienizacao = higienizacao;
        this.tempoInternacao = tempoInternacao;
        this.tempoAltaQualificadaAteAlta = tempoAltaQualificadaAteAlta;
        this.tempoSaidaPaciente = tempoSaidaPaciente;
        this.tempoSaidaAteHigienizacao = tempoSaidaAteHigienizacao;
        this.tempoHigienizacao = tempoHigienizacao;
        this.IdInternacaoPosterior = IdInternacaoPosterior;
        this.tempoOciosidade = tempoOciosidade;
    }

    /**
     * @return the idEstatisticas
     */
    @Id
    @GeneratedValue
    public Integer getIdEstatisticas() {
        return idEstatisticas;
    }

    /**
     * @param idEstatisticas the idEstatisticas to set
     */
    public void setIdEstatisticas(Integer idEstatisticas) {
        this.idEstatisticas = idEstatisticas;
    }

    /**
     * @return the internacao
     */
    @ManyToOne
    @JoinColumn(name = "idInternacao")
    public Internacao getInternacao() {
        return internacao;
    }

    /**
     * @param internacao the internacao to set
     */
    public void setInternacao(Internacao internacao) {
        this.internacao = internacao;
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
     * @return the altaQualificada
     */
    @ManyToOne
    @JoinColumn(name = "idAltaQualificada")
    public AltaQualificada getAltaQualificada() {
        return altaQualificada;
    }

    /**
     * @param altaQualificada the altaQualificada to set
     */
    public void setAltaQualificada(AltaQualificada altaQualificada) {
        this.altaQualificada = altaQualificada;
    }

    /**
     * @return the alta
     */
    @ManyToOne
    @JoinColumn(name = "idAlta")
    public Alta getAlta() {
        return alta;
    }

    /**
     * @param alta the alta to set
     */
    public void setAlta(Alta alta) {
        this.alta = alta;
    }

    /**
     * @return the higienizacao
     */
    @ManyToOne
    @JoinColumn(name = "idHigienizacao")
    public Higienizacao getHigienizacao() {
        return higienizacao;
    }

    /**
     * @param higienizacao the higienizacao to set
     */
    public void setHigienizacao(Higienizacao higienizacao) {
        this.higienizacao = higienizacao;
    }

    /**
     * @return the tempoInternacao
     */
    public Integer getTempoInternacao() {
        return tempoInternacao;
    }

    /**
     * @param tempoInternacao the tempoInternacao to set
     */
    public void setTempoInternacao(Integer tempoInternacao) {
        this.tempoInternacao = tempoInternacao;
    }

    /**
     * @return the tempoAltaQualificadaAteAlta
     */
    public Integer getTempoAltaQualificadaAteAlta() {
        return tempoAltaQualificadaAteAlta;
    }

    /**
     * @param tempoAltaQualificadaAteAlta the tempoAltaQualificadaAteAlta to set
     */
    public void setTempoAltaQualificadaAteAlta(Integer tempoAltaQualificadaAteAlta) {
        this.tempoAltaQualificadaAteAlta = tempoAltaQualificadaAteAlta;
    }

    /**
     * @return the tempoSaidaPaciente
     */
    public Integer getTempoSaidaPaciente() {
        return tempoSaidaPaciente;
    }

    /**
     * @param tempoSaidaPaciente the tempoSaidaPaciente to set
     */
    public void setTempoSaidaPaciente(Integer tempoSaidaPaciente) {
        this.tempoSaidaPaciente = tempoSaidaPaciente;
    }

    /**
     * @return the tempoSaidaAteHigienizacao
     */
    public Integer getTempoSaidaAteHigienizacao() {
        return tempoSaidaAteHigienizacao;
    }

    /**
     * @param tempoSaidaAteHigienizacao the tempoSaidaAteHigienizacao to set
     */
    public void setTempoSaidaAteHigienizacao(Integer tempoSaidaAteHigienizacao) {
        this.tempoSaidaAteHigienizacao = tempoSaidaAteHigienizacao;
    }

    /**
     * @return the tempoHigienizacao
     */
    public Integer getTempoHigienizacao() {
        return tempoHigienizacao;
    }

    /**
     * @param tempoHigienizacao the tempoHigienizacao to set
     */
    public void setTempoHigienizacao(Integer tempoHigienizacao) {
        this.tempoHigienizacao = tempoHigienizacao;
    }

    /**
     * @return the IdInternacaoPosterior
     */
    public Integer getIdInternacaoPosterior() {
        return IdInternacaoPosterior;
    }

    /**
     * @param IdInternacaoPosterior the IdInternacaoPosterior to set
     */
    public void setIdInternacaoPosterior(Integer IdInternacaoPosterior) {
        this.IdInternacaoPosterior = IdInternacaoPosterior;
    }

    /**
     * @return the tempoOciosidade
     */
    public Integer getTempoOciosidade() {
        return tempoOciosidade;
    }

    /**
     * @param tempoOciosidade the tempoOciosidade to set
     */
    public void setTempoOciosidade(Integer tempoOciosidade) {
        this.tempoOciosidade = tempoOciosidade;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.idEstatisticas);
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
        final Estatisticas other = (Estatisticas) obj;
        if (!Objects.equals(this.idEstatisticas, other.idEstatisticas)) {
            return false;
        }
        return true;
    }

}
