/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.util.report;

import br.com.gestaohospitalar.nir.util.HibernateUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * classe responsável em gerar os relatórios
 *
 * @author Daniel
 */
public class GerarRelatorio {

    private final HttpServletResponse response;
    private final FacesContext context;
    private ByteArrayOutputStream baos;
    private InputStream stream;
    private boolean relatorioGerado;
    private final ImageIcon logoNIR = new ImageIcon(getClass().getResource("/br/com/gestaohospitalar/nir/util/report/logo_nir.gif"));

    public GerarRelatorio() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();
    }

    /**
     * método que monta o relatório
     *
     * @param parametros
     * @param nomeRelatorio
     */
    public void getRelatorio(Map<String, Object> parametros, String nomeRelatorio) {
        this.stream = this.getClass().getResourceAsStream(nomeRelatorio + ".jasper");
        parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        parametros.put("LOGO", this.logoNIR.getImage());

        this.baos = new ByteArrayOutputStream();

        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);
            JasperPrint print = JasperFillManager.fillReport(report, parametros, HibernateUtil.getConnection());

            this.relatorioGerado = print.getPages().size() > 0;

            if (this.relatorioGerado) {

                JasperExportManager.exportReportToPdfStream(print, this.baos);

                this.response.reset();
                this.response.setContentType("application/pdf");
                this.response.setContentLength(this.baos.size());
                this.response.setHeader("Content-disposition", "inline; filename=" + nomeRelatorio + ".pdf");
                this.response.getOutputStream().write(this.baos.toByteArray());
                this.response.getOutputStream().flush();
                this.response.getOutputStream().close();

                this.context.responseComplete();

            }

        } catch (JRException | IOException ex) {
            Logger.getLogger(GerarRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * método que monta o relatório passando um ArrayList
     *
     * @param lista
     * @param nomeRelatorio
     */
    public void getRelatorio(List<Object> lista, String nomeRelatorio) {
        this.stream = this.getClass().getResourceAsStream(nomeRelatorio + ".jasper");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        parametros.put("LOGO", this.logoNIR.getImage());
        
        this.baos = new ByteArrayOutputStream();

        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(this.stream);
            JasperPrint print = JasperFillManager.fillReport(report, parametros, new JRBeanCollectionDataSource(lista));
            JasperExportManager.exportReportToPdfStream(print, this.baos);

            this.response.reset();
            this.response.setContentType("application/pdf");
            this.response.setContentLength(this.baos.size());
            this.response.setHeader("Content-disposition", "inline; filename=" + nomeRelatorio + ".pdf");
            this.response.getOutputStream().write(this.baos.toByteArray());
            this.response.getOutputStream().flush();
            this.response.getOutputStream().close();

            this.context.responseComplete();

        } catch (JRException | IOException ex) {
            Logger.getLogger(GerarRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the relatorioGerado
     */
    public Boolean isRelatorioGerado() {
        return relatorioGerado;
    }

}
