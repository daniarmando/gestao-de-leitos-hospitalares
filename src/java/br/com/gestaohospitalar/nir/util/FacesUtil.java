/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author daniel
 */
public class FacesUtil {
    
     public static void adicionarMensagem(FacesMessage.Severity tipo, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, msg, msg));
    }

    public static Object getRequestAttribute(String nome) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        return request.getAttribute(nome);
    }
    
}
