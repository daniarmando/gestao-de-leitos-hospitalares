/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author daniel
 */
@WebFilter(servletNames = "Faces Servlet")
public class HibernateSessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trx = null;

        try {
            trx = session.beginTransaction();
            request.setAttribute("session", session);

            chain.doFilter(request, response);

            trx.commit();
        } catch (Exception e) {
            if (trx != null) {
                trx.rollback();
            }
        } finally {
            session.close();
        }

    }

    @Override
    public void destroy() {
    }

}
