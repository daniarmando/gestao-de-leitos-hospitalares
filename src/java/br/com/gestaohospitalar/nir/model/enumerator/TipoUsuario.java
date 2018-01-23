/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model.enumerator;

/**
 * enum que define os tipos de usuarios
 * @author daniel
 */
public enum TipoUsuario {
    
    NIR("NIR"),
    GERENTE_ENFERMAGEM("GEN"),
    ENFERMEIRO("ENF"),
    MEDICO("MED");
    
    private final String tipoUsuario;
    
    TipoUsuario(String valorTipoUsuario) {
        this.tipoUsuario = valorTipoUsuario;
    }
    
    public String get() {
        return tipoUsuario;
    }
    
}
