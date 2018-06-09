package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "paciente")
@PrimaryKeyJoinColumn(name = "idPaciente")
public class Paciente extends Pessoa implements Serializable, Cloneable {

    private String codigoSusPaciente;
    private String observacoesPaciente;
    private String statusPaciente;

    public Paciente() {
    }

    public Paciente(String codigoSusPaciente, String observacoesPaciente, String statusPaciente, Integer idPessoa, String nomePessoa, String cpfPessoa, String rgPessoa, String sexoPessoa, Date dataNascimentoPessoa, String telefonePessoa, String celularPessoa, String enderecoPessoa, String numeroPessoa, String complementoPessoa, String bairroPessoa, String cepPessoa, String emailPessoa, String statusPessoa, Estado estado, Cidade cidade, Usuario usuario) {
        super(idPessoa, nomePessoa, cpfPessoa, rgPessoa, sexoPessoa, dataNascimentoPessoa, telefonePessoa, celularPessoa, enderecoPessoa, numeroPessoa, complementoPessoa, bairroPessoa, cepPessoa, emailPessoa, statusPessoa, estado, cidade);
        this.codigoSusPaciente = codigoSusPaciente;
        this.observacoesPaciente = observacoesPaciente;
        this.statusPaciente = statusPaciente;
    }

    /**
     * @return the codigoSusPaciente
     */
    public String getCodigoSusPaciente() {
        return codigoSusPaciente;
    }

    /**
     * @param codigoSusPaciente the codigoSusPaciente to set
     */
    public void setCodigoSusPaciente(String codigoSusPaciente) {
        this.codigoSusPaciente = codigoSusPaciente;
    }

    /**
     * @return the observacoesPaciente
     */
    public String getObservacoesPaciente() {
        return observacoesPaciente;
    }

    /**
     * @param observacoesPaciente the observacoesPaciente to set
     */
    public void setObservacoesPaciente(String observacoesPaciente) {
        this.observacoesPaciente = observacoesPaciente;
    }

    /**
     * @return the statusPaciente
     */
    public String getStatusPaciente() {
        return statusPaciente;
    }

    /**
     * @param statusPaciente the statusPaciente to set
     */
    public void setStatusPaciente(String statusPaciente) {
        this.statusPaciente = statusPaciente;
    }
    
    /**
     * Método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Paciente clone() {
        Paciente clone = new Paciente();
        clone.setCodigoSusPaciente(codigoSusPaciente);
        clone.setStatusPaciente(statusPaciente);
        clone.setObservacoesPaciente(observacoesPaciente);
        clone.setIdPessoa(idPessoa);
        clone.setNomePessoa(nomePessoa);
        clone.setCpfPessoa(cpfPessoa);
        clone.setRgPessoa(rgPessoa);
        clone.setSexoPessoa(sexoPessoa);
        clone.setDataNascimentoPessoa(dataNascimentoPessoa);
        clone.setTelefonePessoa(telefonePessoa);
        clone.setCelularPessoa(celularPessoa);
        clone.setEnderecoPessoa(enderecoPessoa);
        clone.setNumeroPessoa(numeroPessoa);
        clone.setComplementoPessoa(complementoPessoa);
        clone.setBairroPessoa(bairroPessoa);
        clone.setCepPessoa(cepPessoa);
        clone.setEmailPessoa(emailPessoa);
        clone.setStatusPessoa(statusPessoa);
        clone.setEstado(estado);
        clone.setCidade(cidade);

        return clone;
    }
    
    //hashCode e equals não gerados pela IDE
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPessoa == null) ? 0 : idPessoa.hashCode());
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
        Paciente other = (Paciente) obj;
        if (idPessoa == null) {
            if (other.idPessoa != null) {
                return false;
            }
        } else if (!idPessoa.equals(other.idPessoa)) {
            return false;
        }
        return true;
    }
    
}
