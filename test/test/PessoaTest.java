/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import br.com.gestaohospitalar.nir.model.Pessoa;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author daniel
 */
public class PessoaTest {
    
    Pessoa pessoa = new Pessoa();
    
    public PessoaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
     @Test
     public void nomeReduzidoPessoa() {
         pessoa.setNomePessoa("Daniel Armando");
         assertEquals("Daniel", pessoa.nomeReduzidoPessoa());
         assertEquals("Maria", pessoa.nomeReduzidoPessoa(" Maria da Silva"));
     }
     
}
