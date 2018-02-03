/* deletar todas as informações de internações */

-- atualiza tabelas
UPDATE leito SET idInternacao = NULL, statusLeito = 'D' WHERE NOT statusLeito IN ('D', 'I', 'M');
UPDATE paciente SET statusPaciente = 'A' WHERE statusPaciente = 'INT';
-- limpa tabelas
DELETE FROM estatisticas WHERE idEstatisticas > 0;
DELETE FROM higienizacao_funcionario WHERE funcionarios_idFuncionario > 0;
DELETE FROM higienizacao WHERE idHigienizacao > 0;
DELETE FROM alta WHERE idAlta > 0;
DELETE FROM altaqualificada WHERE idAltaQualificada > 0;
DELETE FROM internacao WHERE idInternacao > 0;

