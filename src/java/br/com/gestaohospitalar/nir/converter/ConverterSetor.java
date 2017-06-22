/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.converter;

import br.com.gestaohospitalar.nir.DAO.SetorDAOImpl;
import br.com.gestaohospitalar.nir.model.Setor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Daniel
 */
@FacesConverter(value = "converterSetor")
public class ConverterSetor implements Converter {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals("")) {
            SetorDAOImpl daoSetor = new SetorDAOImpl();
            return daoSetor.setorPorId(Integer.valueOf(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Setor) {
            Setor setor = (Setor) value;
            return String.valueOf(setor.getIdSetor());
        }
        return "";
    }
    
}
