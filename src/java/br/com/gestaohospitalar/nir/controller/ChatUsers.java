/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;
 
 
import br.com.gestaohospitalar.nir.DAO.UsuarioDAOImpl;
import br.com.gestaohospitalar.nir.model.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
 
@ManagedBean
@ApplicationScoped
public class ChatUsers implements Serializable {
     
    private List<String> users;
     
     
    @PostConstruct
    public void init() {
        users = new ArrayList<String>();
        
        for (Usuario usuario : new UsuarioDAOImpl().listar()) {
            users.add(usuario.getLogin());
        }
    }
 
    public List<String> getUsers() {
        return users;
    }
     
    public void remove(String user) {
        this.users.remove(user);
    }
     
    public void add(String user) {
        this.users.add(user);
    }
         
    public boolean contains(String user) {
        return this.users.contains(user);
    }
    
    
   
}