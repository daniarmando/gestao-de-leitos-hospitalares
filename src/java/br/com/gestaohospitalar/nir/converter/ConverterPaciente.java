/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.converter;

import br.com.gestaohospitalar.nir.DAO.PacienteDAOImpl;
import br.com.gestaohospitalar.nir.model.Paciente;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Daniel
 */
@FacesConverter(value = "converterPaciente")
public class ConverterPaciente implements Converter {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals("")) {
            PacienteDAOImpl daoPaciente = new PacienteDAOImpl();
            return daoPaciente.pacientePorId(Integer.valueOf(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Paciente) {
            Paciente paciente = (Paciente) value;
            return String.valueOf(paciente.getIdPessoa());
        }
        return "";
    }
    
}
