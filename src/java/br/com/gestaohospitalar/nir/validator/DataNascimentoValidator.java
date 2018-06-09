/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.validator;

import com.sun.faces.util.MessageFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * verifica se a data de nascimento informada é maior que 18 anos
 *
 * @author Daniel
 */
@FacesValidator("br.com.gestaohospitalar.nir.DataNascimentoValidator")
public class DataNascimentoValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {

        String descricaoErro = "";
        Object label = MessageFactory.getLabel(context, component);

        //pega o ano da data de nascimento informada e o transforma em inteiro
        Date data = (Date) value;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
        int anoInformado = Integer.parseInt(fmt.format(data));
        //pega o ano da data atual e a transforma em inteiro
        int anoAtual = Integer.parseInt(fmt.format(new Date()));
        //pega a data que equivala a uma idade de 18 anos 
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, - 18);

        //se estiver cadastrando a data de nascimento de um paciente
        if (component.getId().equals("datanascimentopaciente")) {
            //verifica se a data digitada é maior que a data atual
            if (data != null && data.after(new Date())) {
                descricaoErro = label + " não pode ser uma data futura.";                
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, descricaoErro, descricaoErro);
                throw new ValidatorException(message);
            }
        } //se estiver cadastrando data de nascimento de outras pessoas
        else if (component.getId().equals("datanascimento")) {
            //se constar que o ano da data de nascimento digitada é menor que 18 anos
            if (anoInformado != 0 && (anoAtual - anoInformado) < 18) {
                descricaoErro = label + " deve ser no mínimo a partir de " + fmt.format(cal.getTime()) + ", pois deve ser maior de 18 anos.";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, descricaoErro, descricaoErro);
                throw new ValidatorException(message);
            }
        }

        //se constar que o ano da data de nascimento digitada é menor que 140 anos
        if (anoInformado != 0 && anoInformado < (anoAtual - 140)) {

            descricaoErro = label + " inválida!";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, descricaoErro, descricaoErro);
            throw new ValidatorException(message);
        }
    }
}
