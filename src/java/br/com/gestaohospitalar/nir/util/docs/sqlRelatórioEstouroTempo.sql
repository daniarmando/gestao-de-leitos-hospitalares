SELECT TIMESTAMPDIFF(DAY, i.dataHoraLimiteVermelho, i.dataAlta) AS tempoEstouroDias,
		TIMESTAMPDIFF(HOUR, i.dataHoraLimiteVermelho, i.dataAlta) AS tempoEstouroHoras,
        TIMESTAMPDIFF(MINUTE, i.dataHoraLimiteVermelho, i.dataAlta) AS tempoEstouroMinutos,
		CONCAT(i.idInternacao, '-', i.codigoInternacaoHospital) AS codInternacao,
		i.dataEntrada, i.dataHoraLimiteVermelho AS dataVencimento, i.dataAlta,
        s.idSetor, s.descricaoSetor,
        q.idQuarto, q.descricaoQuarto,
        l.idLeito, l.codigoLeito, l.descricaoLeito,
        p.nomePessoa AS nomePaciente,
        m.nomePessoa AS nomeMedico,
        tp.no_procedimento
FROM internacao i
INNER JOIN leito l ON l.idLeito = i.idLeito
INNER JOIN quarto q ON q.idQuarto = l.idQuarto
INNER JOIN setor s ON s.idSetor = q.idSetor
INNER JOIN pessoa p ON p.idPessoa = i.idPaciente 
INNER JOIN pessoa m ON m.idPessoa = i.idMedico
INNER JOIN tb_procedimento tp ON tp.co_procedimento = i.co_procedimento AND tp.chaveMesAno = i.chaveMesAnoProcedimento
INNER JOIN tb_cid tc ON tc.co_cid = i.co_cid AND tc.chaveMesAno = i.chaveMesAnoCid
WHERE i.dataAlta > i.dataHoraLimiteVermelho 
AND i.statusInternacao != 'C'
AND i.dataEntrada BETWEEN '2017-05-01' AND '2017-06-30'
ORDER BY s.idSetor, q.idQuarto, l.idLeito, i.dataEntrada;
