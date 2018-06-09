/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.converter;

import br.com.gestaohospitalar.nir.DAO.SigtapUploadDAOImpl;
import br.com.gestaohospitalar.nir.model.sigtap.TB_TIPO_LEITO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Daniel
 */
@FacesConverter(value = "converterTb_tipo_leito")
public class ConverterTB_TIPO_LEITO implements Converter {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals("")) {
            SigtapUploadDAOImpl daoSU = new SigtapUploadDAOImpl();
            return daoSU.tb_tipo_leitoPorCodigo(String.valueOf(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof TB_TIPO_LEITO) {
            TB_TIPO_LEITO tb_tipo_leito = (TB_TIPO_LEITO) value;
            return String.valueOf(tb_tipo_leito.getCO_TIPO_LEITO());
        }
        return "";
    }
}
