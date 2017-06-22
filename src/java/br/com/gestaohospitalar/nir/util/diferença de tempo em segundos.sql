SELECT
     estatisticas.`idEstatisticas` AS estatisticas_idEstatisticas,
     
     
     estatisticas.`tempoAltaQualificadaAteAlta` AS estatisticas_tempoAltaQualificadaAteAlta,
     
     
     estatisticas.`tempoSaidaPaciente` AS estatisticas_tempoSaidaPaciente,
     estatisticas.`tempoSaidaAteHigienizacao` AS estatisticas_tempoSaidaAteHigienizacao,
     estatisticas.`tempoHigienizacao` AS estatisticas_tempoHigienizacao,
     estatisticas.`IdInternacaoPosterior` AS estatisticas_IdInternacaoPosterior,
     estatisticas.`tempoOciosidade` AS estatisticas_tempoOciosidade,
     higienizacao.`dataHoraInicio` AS higienizacao_dataHoraInicio,
     internacao.`idInternacao` AS internacao_idInternacao,
     internacao.`dataEntrada` AS internacao_dataEntrada,
     internacao.`dataPrevisaoAlta` AS internacao_dataPrevisaoAlta,
     internacao.`dataAlta` AS internacao_dataAlta,
     internacao.`dataSaidaLeito` AS internacao_dataSaidaLeito,
     internacao.`codigoInternacaoHospital` AS internacao_codigoInternacaoHospital,
     internacao.`dataHoraLimiteVermelho` AS internacao_dataHoraLimiteVermelho,
     setor.`idSetor` AS setor_idSetor,
     setor.`tipoSetor` AS setor_tipoSetor,
     quarto.`idQuarto` AS quarto_idQuarto,
     quarto.`tipoQuarto` AS quarto_tipoQuarto,
     leito.`idLeito` AS leito_idLeito,
     leito.`codigoLeito` AS leito_codigoLeito,
     leito.`descricaoLeito` AS leito_descricaoLeito,
     tb_procedimento.`QT_DIAS_PERMANENCIA` AS tb_procedimento_QT_DIAS_PERMANENCIA
FROM
     `higienizacao` higienizacao LEFT OUTER JOIN `estatisticas` estatisticas ON higienizacao.`idHigienizacao` = estatisticas.`idHigienizacao`
     LEFT OUTER JOIN `internacao` internacao ON estatisticas.`idInternacao` = internacao.`idInternacao`
     AND internacao.`idInternacao` = higienizacao.`idInternacao`
     LEFT OUTER JOIN `leito` leito ON internacao.`idInternacao` = leito.`idInternacao`
     AND leito.`idLeito` = internacao.`idLeito`
      

     
     LEFT OUTER JOIN `tb_procedimento` tb_procedimento ON internacao.`CO_PROCEDIMENTO` = tb_procedimento.`CO_PROCEDIMENTO`
     AND tb_procedimento.`chaveMesAno` = internacao.`chaveMesAnoProcedimento`
    
     LEFT OUTER JOIN `quarto` quarto ON leito.`idQuarto` = quarto.`idQuarto`
     LEFT OUTER JOIN `setor` setor ON quarto.`idSetor` = setor.`idSetor`