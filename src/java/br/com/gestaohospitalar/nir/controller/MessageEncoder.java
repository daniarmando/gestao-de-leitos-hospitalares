/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;
 
import org.primefaces.json.JSONObject;
import org.primefaces.push.Encoder;
 
/**
 * A Simple {@link org.primefaces.push.Encoder} that decode a {@link Message} into a simple JSON object.
 */
public final class MessageEncoder implements Encoder<Message, String> {
 
    //@Override
    public String encode(Message message) {
        return new JSONObject(message).toString();
    }
}