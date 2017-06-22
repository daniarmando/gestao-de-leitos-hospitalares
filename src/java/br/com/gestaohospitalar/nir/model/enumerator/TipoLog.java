/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.model.enumerator;

/**
 * enum que define os valores para os tipos de logs
 * @author Daniel
 */
public enum TipoLog {
    
    //cadastros
    INCLUSAO("Inclusão"),
    ALTERACAO("Alteração"),
    ATIVACAO("Ativação"),
    INATIVACAO("Inativação"),
    EXCLUSAO("Exclusão"),
    
    //importação tabela sigtap
    IMPORTACAO_SIGTAP("Importação Tabela Sigtap"),
    REIMPORTACAO_SIGTAP("Reimportação Tabela Sigtap"),
    
    //processos de internação 
    REGISTRAR_INTERNACAO("Internação"),
    CANCELAR_INTERNACAO("Cancelamento de Internação"),
    REGISTRAR_ALTA_QUALIFICADA("Previsão de Alta"),
    ALTERAR_ALTA_QUALIFICADA("Alteração de Previsão de Alta"),
    EXCLUIR_ALTA_QUALIFICADA("Exclusão de Previsão de Alta"),
    REGISTRAR_ALTA("Registro de Alta"),
    ALTERAR_ALTA("Alteração de Alta"),
    EXCLUIR_ALTA("Exclusão de Alta"),
    REGISTRAR_SAIDA("Registro de Saída do Paciente"),
    REGISTRAR_HIGIENIZACAO("Registro de Higienização"),
    ALTERAR_HIGIENIZACAO("Alteração de Higienização"),
    EXCLUIR_HIGIENIZACAO("Exclusão de Higienização"),
    ERRO_VALIDACAO_PACIENTE("Erro de validação paciente"),
    ERRO_VALIDACAO_PROCEDIMENTO("Erro de validação procedimento"),
    ERRO_VALIDACAO_CID("Erro de validação cid");
    
    private final String valorTipoLog;

    TipoLog(String valorTipoLog) {
        this.valorTipoLog = valorTipoLog;
    }

    public String get() {
        return valorTipoLog;
    }
    
}
