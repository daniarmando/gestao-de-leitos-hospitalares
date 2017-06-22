/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.converter;

import br.com.gestaohospitalar.nir.DAO.FuncionarioDAOImpl;
import br.com.gestaohospitalar.nir.model.Funcionario;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Daniel
 */
@FacesConverter(value = "converterFuncionario")
public class ConverterFuncionario implements Converter{

   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals("")) {
            FuncionarioDAOImpl daoFuncionario = new FuncionarioDAOImpl();
            return daoFuncionario.funcionarioPorId(Integer.valueOf(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Funcionario) {
            Funcionario funcionario = (Funcionario) value;
            return String.valueOf(funcionario.getIdPessoa());
        }
        return "";
    }
    
}
