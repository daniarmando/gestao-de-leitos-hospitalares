SELECT
estatisticas.`tempoInternacao` AS tempoInternacaoSegundos,
     truncate(estatisticas.`tempoInternacao` / 86400, 0) AS tempoInternacaoDias,
     truncate(estatisticas.`tempoInternacao` / 3600, 0) - (truncate(estatisticas.`tempoInternacao` / 86400, 0) * 24) AS tempoInternacaoHoras,
     truncate(estatisticas.`tempoInternacao` / 60, 0) - ((truncate(estatisticas.`tempoInternacao` / 86400, 0) * 1440) + ((truncate(estatisticas.`tempoInternacao` / 3600, 0) - (truncate(estatisticas.`tempoInternacao` / 86400, 0) * 24))*60)) AS tempoInternacaoMinutos,
     
     estatisticas.`tempoAltaQualificadaAteAlta` AS tempoAltaQualificadaAteAltaSegundos,
     truncate(estatisticas.`tempoAltaQualificadaAteAlta` / 86400, 0) AS tempoAltaQualificadaAteAltaDias,
     truncate(estatisticas.`tempoAltaQualificadaAteAlta` / 3600, 0) - (truncate(estatisticas.`tempoAltaQualificadaAteAlta` / 86400, 0) * 24) AS tempoAltaQualificadaAteAltaHoras,
     truncate(estatisticas.`tempoAltaQualificadaAteAlta` / 60, 0) - ((truncate(estatisticas.`tempoAltaQualificadaAteAlta` / 86400, 0) * 1440) + ((truncate(estatisticas.`tempoAltaQualificadaAteAlta` / 3600, 0) - (truncate(estatisticas.`tempoAltaQualificadaAteAlta` / 86400, 0) * 24))*60)) AS tempoAltaQualificadaAteAltaMinutos,
     
     estatisticas.`tempoSaidaPaciente` AS tempoSaidaPacienteSegundos,
     truncate(estatisticas.`tempoSaidaPaciente` / 86400, 0) AS tempoSaidaPacienteDias,
     truncate(estatisticas.`tempoSaidaPaciente` / 3600, 0) - (truncate(estatisticas.`tempoSaidaPaciente` / 86400, 0) * 24) AS tempoSaidaPacienteHoras,
     truncate(estatisticas.`tempoSaidaPaciente` / 60, 0) - ((truncate(estatisticas.`tempoSaidaPaciente` / 86400, 0) * 1440) + ((truncate(estatisticas.`tempoSaidaPaciente` / 3600, 0) - (truncate(estatisticas.`tempoSaidaPaciente` / 86400, 0) * 24))*60)) AS tempoSaidaPacienteMinutos,
     
     estatisticas.`tempoSaidaAteHigienizacao` AS tempoSaidaAteHigienizacaoSegundos,
     truncate(estatisticas.`tempoSaidaAteHigienizacao` / 86400, 0) AS tempoSaidaAteHigienizacaoDias,
     truncate(estatisticas.`tempoSaidaAteHigienizacao` / 3600, 0) - (truncate(estatisticas.`tempoSaidaAteHigienizacao` / 86400, 0) * 24) AS tempoSaidaAteHigienizacaoHoras,
     truncate(estatisticas.`tempoSaidaAteHigienizacao` / 60, 0) - ((truncate(estatisticas.`tempoSaidaAteHigienizacao` / 86400, 0) * 1440) + ((truncate(estatisticas.`tempoSaidaAteHigienizacao` / 3600, 0) - (truncate(estatisticas.`tempoSaidaAteHigienizacao` / 86400, 0) * 24))*60)) AS tempoSaidaAteHigienizacaoMinutos,
     estatisticas.`tempoHigienizacao` AS tempoHigienizacaoSegundos,
     truncate(estatisticas.`tempoHigienizacao` / 86400, 0) AS tempoHigienizacaoDias,
     truncate(estatisticas.`tempoHigienizacao` / 3600, 0) - (truncate(estatisticas.`tempoHigienizacao` / 86400, 0) * 24) AS tempoHigienizacaoHoras,
     truncate(estatisticas.`tempoHigienizacao` / 60, 0) - ((truncate(estatisticas.`tempoHigienizacao` / 86400, 0) * 1440) + ((truncate(estatisticas.`tempoHigienizacao` / 3600, 0) - (truncate(estatisticas.`tempoHigienizacao` / 86400, 0) * 24))*60)) AS tempoHigienizacaoMinutos,
     
	 estatisticas.`tempoOciosidade` AS tempoOciosidadeSegundos,
     truncate(estatisticas.`tempoOciosidade` / 86400, 0) AS tempoOciosidadeDias,
     truncate(estatisticas.`tempoOciosidade` / 3600, 0) - (truncate(estatisticas.`tempoOciosidade` / 86400, 0) * 24) AS tempoOciosidadeHoras,
     truncate(estatisticas.`tempoOciosidade` / 60, 0) - ((truncate(estatisticas.`tempoOciosidade` / 86400, 0) * 1440) + ((truncate(estatisticas.`tempoOciosidade` / 3600, 0) - (truncate(estatisticas.`tempoOciosidade` / 86400, 0) * 24))*60)) AS tempoOciosidadeMinutos,
     internacao.`idInternacao` AS internacao_idInternacao,
     internacao.`dataEntrada` AS internacao_dataEntrada,
     internacao.`dataPrevisaoAlta` AS internacao_dataPrevisaoAlta,
     internacao.`dataAlta` AS internacao_dataAlta,
     internacao.`dataSaidaLeito` AS internacao_dataSaidaLeito,
     internacao.`codigoInternacaoHospital` AS internacao_codigoInternacaoHospital,
     internacao.`dataHoraLimiteVermelho` AS internacao_dataHoraLimiteVermelho,
     setor.`idSetor` AS setor_idSetor,
     setor.`descricaoSetor` AS setor_descricaoSetor,
     quarto.`idQuarto` AS quarto_idQuarto,
     quarto.`descricaoQuarto` AS quarto_descricaoQuarto,
     leito.`idLeito` AS leito_idLeito,
     leito.`codigoLeito` AS leito_codigoLeito,
     leito.`descricaoLeito` AS leito_descricaoLeito,
     tb_procedimento.`QT_DIAS_PERMANENCIA` AS tb_procedimento_QT_DIAS_PERMANENCIA,
     estatisticas.`idEstatisticas` AS estatisticas_idEstatisticas
FROM
     `higienizacao` higienizacao LEFT OUTER JOIN `estatisticas` estatisticas ON higienizacao.`idHigienizacao` = estatisticas.`idHigienizacao`
     LEFT OUTER JOIN `internacao` internacao ON estatisticas.`idInternacao` = internacao.`idInternacao`
     AND internacao.`idInternacao` = higienizacao.`idInternacao`
     RIGHT OUTER JOIN `leito` leito ON internacao.`idLeito` = leito.`idLeito`
     LEFT OUTER JOIN `tb_procedimento` tb_procedimento ON internacao.`CO_PROCEDIMENTO` = tb_procedimento.`CO_PROCEDIMENTO`
     AND tb_procedimento.`chaveMesAno` = internacao.`chaveMesAnoProcedimento`
     AND leito.`idLeito` = estatisticas.`idLeito`
     LEFT OUTER JOIN `quarto` quarto ON leito.`idQuarto` = quarto.`idQuarto`
     LEFT OUTER JOIN `setor` setor ON quarto.`idSetor` = setor.`idSetor`
WHERE
     internacao.`statusInternacao` = 'EN'
 AND internacao.`dataEntrada` BETWEEN $P{data_inicial} AND $P{data_final}
 AND setor.`idSetor` = $P{id_setor}
ORDER BY
     setor.`idSetor` ASC,
     quarto.`idQuarto` ASC,
     leito.`idLeito` ASC,
     internacao.`dataEntrada` ASC