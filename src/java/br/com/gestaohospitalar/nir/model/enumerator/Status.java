/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model.enumerator;

/**
 * enum que define os valores para os status usados nas classes
 * @author Daniel
 */
public enum Status {
    
    ATIVO("A"),
    INATIVO("I"),
    ABERTA("AB"),
    ENCERRADA("EN"),
    CANCELADA("C"),
    DISPONIVEL("D"),
    INDISPONIVEL("IN"),
    INTERNACAO("INT"),
    HIGIENIZACAO("H"),
    MANUTENCAO("M"),
    ESPERA("ES");

    private final String valorStatus;

    Status(String valorStatus) {
        this.valorStatus = valorStatus;
    }

    public String get() {
        return valorStatus;
    }

}

/**
 * Enfermeiro: ATIVO, INATIVO
 * Fisioterapeuta: ATIVO, INATIVO
 * Funcionario: ATIVO, INATIVO 
 * GerenteEnfermagem: ATIVO, INATIVO
 * Hospital: ATIVO, INATIVO
 * Internação: ABERTA, ENCERRADA, CANCELADA, HIGIENIZACAO
 * Leito: DISPONIVEL, INDISPONIVEL, MANUTENCAO, INTERNACAO, ESPERA, HIGIENIZACAO
 * Medico: ATIVO, INATIVO
 * NIR: ATIVO, INATIVO
 * Paciente: ATIVO, INATIVO, INTERNACAO
 * Pessoa: ATIVO, INATIVO
 * Quarto: ATIVO, INATIVO, MANUTENCAO
 * Setor: ATIVO, INATIVO 
 */
