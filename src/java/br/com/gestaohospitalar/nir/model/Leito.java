/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model;

import br.com.gestaohospitalar.nir.model.sigtap.TB_TIPO_LEITO;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "leito")
public class Leito implements Serializable {

    private Integer idLeito;
    private String codigoLeito;
    private String descricaoLeito;
    private String statusLeito;
    private Quarto quarto;
    private TB_TIPO_LEITO tipo_leito;
    private String chaveMesAnoTipoLeito; //Atributo é a chaveMesAno para usar como parâmetro para as tabelas SigTap, porém não foi definiido como chave estrangeira, apenas no BD está como chave estrangeira da TB_TIPO_LEITO
    private String tipoSexo;
    private Integer idadeMinima;
    private Integer idadeMaxima;
    private Internacao internacao;

    public Leito() {
    }

    public Leito(Integer idLeito, String codigoLeito, String descricaoLeito, String statusLeito, Quarto quarto, TB_TIPO_LEITO tipo_leito, String chaveMesAnoTipoLeito, String tipoSexo, Integer idadeMinima, Integer idadeMaxima, Internacao internacao) {
        this.idLeito = idLeito;
        this.codigoLeito = codigoLeito;
        this.descricaoLeito = descricaoLeito;
        this.statusLeito = statusLeito;
        this.quarto = quarto;
        this.tipo_leito = tipo_leito;
        this.tipoSexo = tipoSexo;
        this.idadeMinima = idadeMinima;
        this.idadeMaxima = idadeMaxima;
        this.chaveMesAnoTipoLeito = chaveMesAnoTipoLeito;
        this.internacao = internacao;
    }

    /**
     * @return the idLeito
     */
    @Id
    @GeneratedValue
    public Integer getIdLeito() {
        return idLeito;
    }

    /**
     * @param idLeito the idLeito to set
     */
    public void setIdLeito(Integer idLeito) {
        this.idLeito = idLeito;
    }

    /**
     * @return the codigoLeito
     */
    public String getCodigoLeito() {
        return codigoLeito;
    }

    /**
     * @param codigoLeito the codigoLeito to set
     */
    public void setCodigoLeito(String codigoLeito) {
        this.codigoLeito = codigoLeito;
    }

    /**
     * @return the statusLeito
     */
    public String getStatusLeito() {
        return statusLeito;
    }

    /**
     * @param statusLeito the statusLeito to set
     */
    public void setStatusLeito(String statusLeito) {
        this.statusLeito = statusLeito;
    }

    /**
     * @return the quarto
     */
    @ManyToOne
    @JoinColumn(name = "idQuarto")
    public Quarto getQuarto() {
        return quarto;
    }

    /**
     * @param quarto the quarto to set
     */
    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    /**
     * @return the descricaoLeito
     */
    public String getDescricaoLeito() {
        return descricaoLeito;
    }

    /**
     * @param descricaoLeito the descricaoLeito to set
     */
    public void setDescricaoLeito(String descricaoLeito) {
        this.descricaoLeito = descricaoLeito;
    }

    /**
     * @return the tb_tipo_leito
     */
    @ManyToOne
    @JoinColumn(name = "CO_TIPO_LEITO")
    public TB_TIPO_LEITO getTipo_leito() {
        return tipo_leito;
    }

    /**
     * @param tipo_leito the tipo_leito to set
     */
    public void setTipo_leito(TB_TIPO_LEITO tipo_leito) {
        this.tipo_leito = tipo_leito;
    }

    /**
     * @return the chaveMesAnoTipoLeito
     */
    public String getChaveMesAnoTipoLeito() {
        return chaveMesAnoTipoLeito;
    }

    /**
     * @param chaveMesAnoTipoLeito the chaveMesAnoTipoLeito to set
     */
    public void setChaveMesAnoTipoLeito(String chaveMesAnoTipoLeito) {
        this.chaveMesAnoTipoLeito = chaveMesAnoTipoLeito;
    }
    
    /**
     * @return the tipoSexo
     */
    public String getTipoSexo() {
        return tipoSexo;
    }

    /**
     * @param tipoSexo the tipoSexo to set
     */
    public void setTipoSexo(String tipoSexo) {
        this.tipoSexo = tipoSexo;
    }

    /**
     * @return the idadeMinima
     */
    public Integer getIdadeMinima() {
        return idadeMinima;
    }

    /**
     * @param idadeMinima the idadeMinima to set
     */
    public void setIdadeMinima(Integer idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    /**
     * @return the idadeMaxima
     */
    public Integer getIdadeMaxima() {
        return idadeMaxima;
    }

    /**
     * @param idadeMaxima the idadeMaxima to set
     */
    public void setIdadeMaxima(Integer idadeMaxima) {
        this.idadeMaxima = idadeMaxima;
    }

    /**
     * @return the internacao
     */
    @ManyToOne(optional = true)
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

    //hashCode e equals não gerados pela IDE
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idLeito == null) ? 0 : idLeito.hashCode());
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
        Leito other = (Leito) obj;
        if (idLeito == null) {
            if (other.idLeito != null) {
                return false;
            }
        } else if (!idLeito.equals(other.idLeito)) {
            return false;
        }
        return true;
    }

    /**
     * Método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Leito clone() {
        Leito clone = new Leito();
        clone.setIdLeito(idLeito);
        clone.setCodigoLeito(codigoLeito);
        clone.setDescricaoLeito(descricaoLeito);
        clone.setStatusLeito(statusLeito);
        clone.setQuarto(quarto);
        clone.setTipo_leito(tipo_leito);
        clone.setTipoSexo(tipoSexo);
        clone.setIdadeMinima(idadeMinima);
        clone.setIdadeMaxima(idadeMaxima);

        return clone;
    }

}
