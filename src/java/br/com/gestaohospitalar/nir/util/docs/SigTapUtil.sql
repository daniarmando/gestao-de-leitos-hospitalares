-- deletar tabela sigtap mais recente
DELETE FROM rl_procedimento_leito WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);
DELETE FROM rl_procedimento_cid WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);
DELETE FROM tb_procedimento WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);
DELETE FROM tb_modalidade WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);
DELETE FROM tb_tipo_leito WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);
DELETE FROM tb_cid WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);
DELETE FROM tb_rubrica WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);
DELETE FROM tb_financiamento WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);
DELETE FROM sigtapuploadlog WHERE ChaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog);

-- consultar a quantidade de linhas da tabela sigtap mais recente
SELECT *, (tipoLeito + procedimentoLeito + procedimentoCid + procedimento + cid + rubrica + modalidade + financiamento) AS Total 
FROM (
(SELECT COUNT(*) tipoLeito FROM tb_tipo_leito WHERE chaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog)) AS tipoLeito,
(SELECT COUNT(*) procedimentoLeito FROM rl_procedimento_leito WHERE chaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog)) AS procedimentoLeito,
(SELECT COUNT(*) procedimentoCid FROM rl_procedimento_cid WHERE chaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog)) AS procedimentoCid,
(SELECT COUNT(*) procedimento FROM tb_procedimento WHERE chaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog)) AS procedimento,
(SELECT COUNT(*) cid FROM tb_cid WHERE chaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog)) AS cid,
(SELECT COUNT(*) rubrica FROM tb_rubrica WHERE chaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog)) AS rubrica,
(SELECT COUNT(*) modalidade FROM tb_modalidade WHERE chaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog)) AS modalidade,
(SELECT COUNT(*) financiamento FROM tb_financiamento WHERE chaveMesAno = (SELECT MAX(chaveMesAno) FROM sigtapuploadlog)) AS financiamento
);

