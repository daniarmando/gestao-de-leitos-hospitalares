/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.view;
 
public class Message {
     
    private String text;
    private String user;
    private boolean updateList;
 
    public Message() {
    }
 
    public Message(String text) {
        this.text = text;
    }
     
    public Message(String text, boolean updateList) {
        this.text = text;
        this.updateList = updateList;
    }
 
    public Message(String user, String text, boolean updateList) {
        this.text = text;
        this.user = user;
        this.updateList = updateList;
    }
     
    public String getText() {
        return text;
    }
 
    public Message setText(String text) {
        this.text = text;
        return this;
    }
 
    public String getUser() {
        return user;
    }
 
    public Message setUser(String user) {
        this.user = user;
        return this;
    }
 
    public boolean isUpdateList() {
        return updateList;
    }
 
    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}