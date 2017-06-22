/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.converter;

import br.com.gestaohospitalar.nir.DAO.SigtapUploadLogDAOImpl;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * classe com métodos para realizar conversões com datas
 * 
 * @author Daniel
 */
public class ConverterDataHora {

    /**
     * método que retorna uma data formatada (dd/MM/yyyy as HH:mm:ss)
     *
     * @param data
     * @return data e hora formatada
     */
    public static String formatarDataHora(Date data) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy 'as' HH:mm");
        return (String) fmt.format(data);
    }
    
    /**
     * método que retorna uma data formatada (dd/MM/yyyy)
     *
     * @param data
     * @return data formatada
     */
    public static String formatarData(Date data) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        return (String) fmt.format(data);
    }
    
    /**
     * método que retorna a idade
     *
     * @param dataNascimento
     * @return idade
     */
    public static int calcularIdade(Date dataNascimento) {
        Calendar cdataNascimento = paraCalendar(dataNascimento);
        Calendar hoje = Calendar.getInstance();
        
        int idade = hoje.get(Calendar.YEAR) - cdataNascimento.get(Calendar.YEAR);
        
        return idade;
    }
    
    /**
     * método que traz a última chaveMesAno cadastrada
     *
     * @return the umtilaChaveMesAno
     */
    public static String ultimaChaveMesAno() {
        SigtapUploadLogDAOImpl daoSigtapUploadLog = new SigtapUploadLogDAOImpl();
        return daoSigtapUploadLog.ultimaChaveMesAno();
    }

    /**
     * método que transforma a data atual na chave de "MesAno" para usar no
     * cadastro das tabelas SigTap Ex. 27/12/2016 = 122016
     *
     * @return the chaveMesAno
     */
    public static String gerarChaveMesAno() {
        SimpleDateFormat fmtMesAno = new SimpleDateFormat("MMyyyy");
        return fmtMesAno.format(new Date());
    }

    /**
     * método que transforma uma data em mês escrito Ex. 27/12/2016 = Dezembro
     *
     * @param data
     * @return the mesTexto
     */
    public static String paraMes(Date data) {
        try {
            if (data != null) {
                //Cria uma formatação para a data, pegando apenas o mês
                SimpleDateFormat fmtMes = new SimpleDateFormat("MM");
                //Pega o mês passado como paramêtro e o transforma em inteiro
                Integer mes = Integer.parseInt(fmtMes.format(data));
                String mesTexto = "";

                switch (mes) {
                    case 1:
                        mesTexto = "Janeiro";
                        break;

                    case 2:
                        mesTexto = "Fevereiro";
                        break;

                    case 3:
                        mesTexto = "Março";
                        break;

                    case 4:
                        mesTexto = "Abril";
                        break;

                    case 5:
                        mesTexto = "Maio";
                        break;

                    case 6:
                        mesTexto = "Junho";
                        break;

                    case 7:
                        mesTexto = "Julho";
                        break;

                    case 8:
                        mesTexto = "Agosto";
                        break;

                    case 9:
                        mesTexto = "Setembro";
                        break;

                    case 10:
                        mesTexto = "Outubro";
                        break;

                    case 11:
                        mesTexto = "Novembro";
                        break;

                    case 12:
                        mesTexto = "Dezembro";
                        break;

                    default:
                        System.out.println("Data inválida");
                        return null;
                }
                return mesTexto;
            } else {
                return null;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Problemas ao converter data em mês em texto! " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * método que recebe duas datas com horários e calcula a diferença entre
     * elas em minutos
     *
     * @param data1
     * @param data2
     * @return the minutos
     */
    public static Integer diferencaEmMinutos(Date data1, Date data2) {
        try {
            if (data1 != null && data2 != null) {
                //Convertendo datas em LocalDateTime para fazer o cálculo
                LocalDateTime dataInicio = paraLocalDateTime(data1);
                LocalDateTime dataFim = paraLocalDateTime(data2);

                //Fazendo o cálculo
                Duration duracao = Duration.between(dataInicio, dataFim);
                Long minutos = duracao.getSeconds() / 60;
                return minutos.intValue();
            } else {
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Problemas ao calcular a diferença entre as datas em minutos! " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * converte LocalDate em Date
     *
     * @param localDate
     * @return the Date
     */
    public static Date paraDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * converte LocalDateTime em Date
     *
     * @param localDateTime
     * @return the Date
     */
    public static Date paraDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * converte Date em LocalDate
     *
     * @param date
     * @return the Date
     */
    public static LocalDate paraLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * converte Date em LocalDateTime
     *
     * @param date
     * @return the Date
     */
    public static LocalDateTime paraLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * converte Date em Calendar
     *
     * @param date
     * @return the Date
     */
    public static Calendar paraCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
