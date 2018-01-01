package br.com.gestaohospitalar.nir.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hospital")
public class Hospital  implements Serializable {

     private Integer idHospital;
     private String nomeHospital;
     private String emailHospital;
     private String telefoneHospital;
     private String siteHospital;
     private String statusHospital;

    public Hospital() {
    }

    public Hospital(Integer idHospital, String nomeHospital, String emailHospital, String telefoneHospital, String siteHospital, String statusHospital) {
        this.idHospital = idHospital;
        this.nomeHospital = nomeHospital;
        this.emailHospital = emailHospital;
        this.telefoneHospital = telefoneHospital;
        this.siteHospital = siteHospital;
        this.statusHospital = statusHospital;
    }

    /**
     * @return the idHospital
     */
    @Id
    @GeneratedValue
    public Integer getIdHospital() {
        return idHospital;
    }

    /**
     * @param idHospital the idHospital to set
     */
    public void setIdHospital(Integer idHospital) {
        this.idHospital = idHospital;
    }

    /**
     * @return the nomeHospital
     */
    public String getNomeHospital() {
        return nomeHospital;
    }

    /**
     * @param nomeHospital the nomeHospital to set
     */
    public void setNomeHospital(String nomeHospital) {
        this.nomeHospital = nomeHospital;
    }

    /**
     * @return the emailHospital
     */
    public String getEmailHospital() {
        return emailHospital;
    }

    /**
     * @param emailHospital the emailHospital to set
     */
    public void setEmailHospital(String emailHospital) {
        this.emailHospital = emailHospital;
    }

    /**
     * @return the telefoneHospital
     */
    public String getTelefoneHospital() {
        return telefoneHospital;
    }

    /**
     * @param telefoneHospital the telefoneHospital to set
     */
    public void setTelefoneHospital(String telefoneHospital) {
        this.telefoneHospital = telefoneHospital;
    }

    /**
     * @return the siteHospital
     */
    public String getSiteHospital() {
        return siteHospital;
    }

    /**
     * @param siteHospital the siteHospital to set
     */
    public void setSiteHospital(String siteHospital) {
        this.siteHospital = siteHospital;
    }

    /**
     * @return the statusHospital
     */
    public String getStatusHospital() {
        return statusHospital;
    }

    /**
     * @param statusHospital the statusHospital to set
     */
    public void setStatusHospital(String statusHospital) {
        this.statusHospital = statusHospital;
    }

     /**
     * Método que gera uma cópia do objeto
     *
     * @return
     */
    @Override
    public Hospital clone() {
        Hospital clone = new Hospital();
        clone.setIdHospital(idHospital);
        clone.setNomeHospital(nomeHospital);
        clone.setEmailHospital(emailHospital);
        clone.setTelefoneHospital(telefoneHospital);
        clone.setSiteHospital(siteHospital);
        clone.setStatusHospital(statusHospital);
        
        return clone;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.idHospital);
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
        final Hospital other = (Hospital) obj;
        if (!Objects.equals(this.idHospital, other.idHospital)) {
            return false;
        }
        return true;
    }
    
}


