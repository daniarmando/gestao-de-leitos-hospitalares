/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.converter;

import br.com.gestaohospitalar.nir.DAO.GerenteEnfermagemDAOImpl;
import br.com.gestaohospitalar.nir.model.GerenteEnfermagem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Daniel
 */
@FacesConverter(value = "converterGerenteEnfermagem")
public class ConverterGerenteEnfermagem implements Converter {
    
     @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals("")) {
            GerenteEnfermagemDAOImpl daoGerenteEnfermagem = new GerenteEnfermagemDAOImpl();
            return daoGerenteEnfermagem.gerenteEnfermagemPorId(Integer.valueOf(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof GerenteEnfermagem) {
            GerenteEnfermagem gerenteEnfermagem = (GerenteEnfermagem) value;
            return String.valueOf(gerenteEnfermagem.getIdPessoa());
        }
        return "";
    }
    
}
