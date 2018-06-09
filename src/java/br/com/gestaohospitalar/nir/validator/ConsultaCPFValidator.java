/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.validator;

import br.com.gestaohospitalar.nir.DAO.PessoaDAOImpl;
import br.com.gestaohospitalar.nir.model.Pessoa;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import javax.faces.application.FacesMessage;

/**
 * Validação que irá apenas realizar uma consulta no BD para verificar se o CPF
 * informado já existe cadastrado no BD, as demais validações de CPF serão
 * feitas através da biblioteca Stella 2.1.2
 *
 * @author Daniel
 */
public class ConsultaCPFValidator {

    public static Boolean verificar(Pessoa pessoa, Pessoa clonePessoa) {
        PessoaDAOImpl daoPessoa = new PessoaDAOImpl();

        //Se for cadastro novo
        if (pessoa.getIdPessoa() == null || pessoa.getIdPessoa() == 0) {
            if (daoPessoa.isCPFCadastrado(pessoa.getCpfPessoa())) {
                FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "CPF já cadastrado no sistema.");
                return false;
            } else {
                return true;
            }

            //se for alteração
        } else {
            if (!pessoa.getCpfPessoa().equals(clonePessoa.getCpfPessoa())) {
                if (daoPessoa.isCPFCadastrado(pessoa.getCpfPessoa())) {
                    FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "CPF já cadastrado no sistema.");
                    return false;
                } else {
                    return true;
                }
            }
        }

        return true;
    }
}
