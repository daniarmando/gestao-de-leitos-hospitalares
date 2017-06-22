/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

/**
 *
 * @author Daniel
 */
public interface InterfaceBean {

    /**
     * método que executa ações na inicialização da página de pesquisa
     */
    public void inicializarPaginaPesquisa();

    /**
     * método que executa ações na inicialização da página de cadastro
     */
    public void inicializarPaginaCadastro();

    /**
     * método que redireciona para a página de cadastro quando clicado no botão
     * "Novo"
     *
     * @return página
     */
    public String novo();

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
     * método que traz os logs para do objeto selecionado
     */
    public void gerarLogs();

    /**
     * Método que passa uma lista e o nome do relatório para a classe
     * responsável em gerar os relatórios
     */
    public void gerarRelatorio();

}
