/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.converter;

import br.com.gestaohospitalar.nir.DAO.HospitalDAOImpl;
import br.com.gestaohospitalar.nir.model.Hospital;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Daniel
 */
@FacesConverter(value = "converterHospital")
public class ConverterHospital implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals("")) {
            HospitalDAOImpl daoHospital = new HospitalDAOImpl();
            return daoHospital.hospitalPorId(Integer.valueOf(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Hospital) {
            Hospital hospital = (Hospital) value;
            return String.valueOf(hospital.getIdHospital());
        }
        return "";
    }
}
