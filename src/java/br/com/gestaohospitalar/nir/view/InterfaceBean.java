/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;

/**
 *
 * @author Daniel
 */
public interface InterfaceBean {

    /**
     * método que executa ações na inicialização da página de consulta
     */
    public void inicializarConsulta();

    /**
     * método que executa ações na inicialização da página de cadastro
     */
    public void inicializarCadastro();
    
    /**
     * método que executa ações na inicialização da página de cadastro para edição
     */
    public void inicializarEdicao();

    /**
     * método que verifica se o usuario vai cadastrar ou alterar e envia o
     * objeto para o método respectivo na camada dao
     */
    public void salvar();

    /**
     * método para verificar se o usuário está editando um registro
     *
     * @return the true or false
     */
    public Boolean isEditar();

    /**
     * método que envia o objeto para a DAO para inativar ou excluir o cadastro
     */
    public void excluir();

    /**
     * método que monta e salva o log
     *
     */
    public void salvarLog();

    /**
     * método que exibe o último log na página
     *
     * @return
     */
    public String ultimoLog();

    /**
     * método que traz os logs para o objeto selecionado
     * @param idObjeto
     */
    public void gerarLogs(int idObjeto);

    /**
     * Método que passa uma lista e o nome do relatório para a classe
     * responsável em gerar os relatórios
     */
    public void gerarRelatorio();

}
