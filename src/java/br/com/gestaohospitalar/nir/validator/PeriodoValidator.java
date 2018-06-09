/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.validator;

import br.com.gestaohospitalar.nir.util.NIRDataUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * valida período entre datas
 *
 * @author Daniel
 */
public class PeriodoValidator {
    
     /**
     * método que compara duas datas verificando se a data inicial 
     * está menor que a data final
     *
     * @param dataInicial
     * @param dataFinal
     * @return true or false
     */
    public static Boolean comparaDatas (Date dataInicial, Date dataFinal) {
        //retorna true se a data inicial for menor que a data final
        return dataInicial.before(dataFinal);
    }

    /**
     * método que pega período em dias entre duas datas e verifica se a
     * quantidade de dias está dentro do limite de dias aceito
     *
     * @param dataInicial
     * @param dataFinal
     * @param limiteDias
     * @return true or false
     */
    public static Boolean qtdDias(Date dataInicial, Date dataFinal, int limiteDias) {

        //converte as datas de Date para LocalDate
        LocalDate ldDataInicial = NIRDataUtil.paraLocalDate(dataInicial);
        LocalDate ldDataFinal = NIRDataUtil.paraLocalDate(dataFinal);

        //pega a quantidade de dias entre as duas datas
        long dias = ChronoUnit.DAYS.between(ldDataInicial, ldDataFinal);

        //retorna true caso a quantidade de dias do
        //período seja menor qua o limite de dias aceito
        return dias < limiteDias;

    }
    
    /**
     * método que pega período em meses entre duas datas e verifica se a
     * quantidade de meses está dentro do limite de dias aceito
     *
     * @param dataInicial
     * @param dataFinal
     * @param limiteMeses
     * @return true or false
     */
    public static Boolean qtdMeses(Date dataInicial, Date dataFinal, int limiteMeses) {

        //converte as datas de Date para LocalDate
        LocalDate ldDataInicial = NIRDataUtil.paraLocalDate(dataInicial);
        LocalDate ldDataFinal = NIRDataUtil.paraLocalDate(dataFinal);

        //pega a quantidade de meses entre as duas datas
        long meses = ChronoUnit.MONTHS.between(ldDataInicial, ldDataFinal);

        //retorna true caso a quantidade de dias do
        //período seja menor qua o limite de dias aceito
        return meses < limiteMeses;

    }

}
