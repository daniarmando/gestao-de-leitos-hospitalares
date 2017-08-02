/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.controller;

import br.com.gestaohospitalar.nir.DAO.InternacaoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LeitoDAOImpl;
import br.com.gestaohospitalar.nir.DAO.LogDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SigtapUploadDAOImpl;
import br.com.gestaohospitalar.nir.DAO.SigtapUploadLogDAOImpl;
import br.com.gestaohospitalar.nir.converter.ConverterDataHora;
import br.com.gestaohospitalar.nir.model.Log;
import br.com.gestaohospitalar.nir.model.sigtap.RL_PROCEDIMENTO_CID;
import br.com.gestaohospitalar.nir.model.sigtap.RL_PROCEDIMENTO_LEITO;
import br.com.gestaohospitalar.nir.model.SigtapUploadLog;
import br.com.gestaohospitalar.nir.model.enumerator.TipoLog;
import br.com.gestaohospitalar.nir.model.sigtap.TB_CID;
import br.com.gestaohospitalar.nir.model.sigtap.TB_FINANCIAMENTO;
import br.com.gestaohospitalar.nir.model.sigtap.TB_MODALIDADE;
import br.com.gestaohospitalar.nir.model.sigtap.TB_PROCEDIMENTO;
import br.com.gestaohospitalar.nir.model.sigtap.TB_RUBRICA;
import br.com.gestaohospitalar.nir.model.sigtap.TB_TIPO_LEITO;
import br.com.gestaohospitalar.nir.service.DAOException;
import br.com.gestaohospitalar.nir.util.FacesUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Daniel
 */
@ManagedBean
@ViewScoped
public class SigtapUploadBean implements Serializable {

    //upload
    private String caminho = "";
    private String diretorio = "";

    //progresso importação
    private Integer progresso = 0;
    private final Integer qtdTabelas = 8; //são importados 8 tabelas
    private String mensagem = "Preparando...";
    private String nomeArquivo = "";

    List<TB_TIPO_LEITO> tb_tipos_leitos = new ArrayList<>();
    List<TB_RUBRICA> tb_rubricas = new ArrayList<>();
    List<TB_FINANCIAMENTO> tb_financiamentos = new ArrayList<>();
    List<TB_CID> tb_cids = new ArrayList<>();
    List<TB_MODALIDADE> tb_modalidades = new ArrayList<>();
    List<TB_PROCEDIMENTO> tb_procedimentos = new ArrayList<>();
    List<RL_PROCEDIMENTO_LEITO> rl_procedimentos_leitos = new ArrayList<>();
    List<RL_PROCEDIMENTO_CID> rl_procedimentos_cids = new ArrayList<>();

    private SigtapUploadDAOImpl daoSigtapUpload;

    private SigtapUploadLogDAOImpl daoSigtapUploadLog;
    private final SigtapUploadLog sigtapUploadLog = new SigtapUploadLog();

    //injetando o usuário logado
    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    private LogDAOImpl daoLog;
    private Log log;

    //transforma String em data de acordo com o formato que vem no arquivo txt
    private final SimpleDateFormat fmtData = new SimpleDateFormat("ddyyMM");

    //monta a chave MesAno
    private final String chaveMesAno = ConverterDataHora.gerarChaveMesAno();

    //cria uma data atual
    private final Date data = new Date();

    /**
     * Creates a new instance of SigtapUploadBean
     */
    public SigtapUploadBean() {
    }

    /**
     * método que retorna uma conversão de data em mês em texto ex. se for mês
     * "12" ele retorna "Dezembro".
     *
     * @return mesTexto
     */
    public String mostrarMes() {
        return ConverterDataHora.paraMes(data);
    }

    /**
     * método que dispara uma consulta no BD para verificar se o mês atual já
     * foi importado para o BD ou não.
     *
     * @return true or false
     */
    public boolean isTabelaImportada() {
        this.daoSigtapUpload = new SigtapUploadDAOImpl();

        //retorna true se tabela para o mês atual já estiver importada
        return this.daoSigtapUpload.isTabelaImportada(chaveMesAno);
    }

    /**
     * método que verifica se a tabela do mês já foi importada e retorna uma
     * String que contém a exibição de um dialog na página sigtap-upload.xhtml,
     * se tiver sido importada, ele exibe o diálogo para reimportação, senão ele
     * exibe o diálogo para importação normal.
     *
     * @return exibirDialogo
     */
    public String exibirDialogo() {
        String exibirDialogo;
        //se a tabela já estiver sido importada anteriormente
        if (isTabelaImportada()) {
            //se existirem registros que dependem da tabela, chama o dialog que apresenta uma mensagem informando que a tabela não pode ser substituída
            if (new InternacaoDAOImpl().temTB_TIPO_LEITO(chaveMesAno) || new LeitoDAOImpl().temTB_TIPO_LEITO(chaveMesAno)) {
                exibirDialogo = "PF('dlgnaoimportar').show()";
                //senão existirem registros que dependem da tabela, chama o dialog para substituir a tabela
            } else {
                exibirDialogo = "PF('dlgreimportar').show()";
            }
            //se a tabela não estiver sido importada, chama o dialog de importação da tabela
        } else {
            exibirDialogo = "PF('dlgimportar').show()";
        }
        return exibirDialogo;
    }

    /**
     * método que executa o upload da tabela SigTap
     *
     * @param event
     */
    public void upload(FileUploadEvent event) {
        try {
            //ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            //HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            FacesContext aFacesContext = FacesContext.getCurrentInstance();
            ServletContext context = (ServletContext) aFacesContext.getExternalContext().getContext();

            String realPath = context.getRealPath("/");

            //pega o ano e mês atual para montar o diretório
            LocalDateTime dataAtual = LocalDateTime.now();
            String ano = String.valueOf(dataAtual.getYear());
            String mes = String.valueOf(dataAtual.getMonth().getValue());

            //monta o diretorio
            diretorio = realPath + "/tabelas_sigtap/" + ano + "/" + mes + "/";
            File file = new File(diretorio);
            file.mkdirs();

            byte[] arquivo = event.getFile().getContents();
            //monta o caminho completo do arquivo
            caminho = realPath + "/tabelas_sigtap/" + ano + "/" + mes + "/" + event.getFile().getFileName();

            //passando o nome do arquivo para as informações da barra de progresso
            nomeArquivo = event.getFile().getFileName();

            // esse trecho grava o arquivo no diretório
            FileOutputStream fos = new FileOutputStream(caminho);
            fos.write(arquivo);
            fos.close();

        } catch (IOException ex) {
            System.out.println("Erro no upload " + ex);
        }
    }

    /**
     * método que descompacta o arquivo da tabela SigTap
     *
     * @throws java.io.IOException
     */
    public void unzip() throws IOException {

        //concatena com o diretório o nome da pasta "unzip" para descompactar o arquivo 
        diretorio = diretorio + "unzip/";

        File zipFile = new File(caminho);
        File dir = new File(diretorio);
        ZipFile zip = null;
        File arquivo = null;
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer = new byte[1024];

        try {
            // cria diretório informado, caso não exista
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!dir.exists() || !dir.isDirectory()) {
                throw new IOException("O diretório " + dir.getName()
                        + " não é um diretório válido");
            }

            zip = new ZipFile(zipFile);
            Enumeration e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry entrada = (ZipEntry) e.nextElement();
                arquivo = new File(dir, entrada.getName());

                // se for diretório inexistente, cria a estrutura e pula 
                // pra próxima entrada
                if (entrada.isDirectory() && !arquivo.exists()) {
                    arquivo.mkdirs();
                    continue;
                }

                // se a estrutura de diretórios não existe, cria
                if (!arquivo.getParentFile().exists()) {
                    arquivo.getParentFile().mkdirs();
                }
                try {
                    // lê o arquivo do zip e grava em disco
                    is = zip.getInputStream(entrada);
                    os = new FileOutputStream(arquivo);
                    int bytesLidos = 0;
                    if (is == null) {
                        throw new ZipException("Erro ao ler a entrada do zip: "
                                + entrada.getName());
                    }
                    while ((bytesLidos = is.read(buffer)) > 0) {
                        os.write(buffer, 0, bytesLidos);
                    }
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ex) {
                        }
                    }
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) {
                    System.out.println("Problemas para descompactar arquivo Erro: " + e.getMessage());
                }
            }
        }
    }

    /**
     * método realiza o controle e libera os cadastros das tabelas no BD
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws br.com.gestaohospitalar.nir.service.DAOException
     */
    public void importarSigtap() throws FileNotFoundException, IOException, DAOException {
        this.daoSigtapUpload = new SigtapUploadDAOImpl();

        try {

            this.log = new Log();

            //descompacta arquivo
            unzip();

            //se caso a tabela já estiver sido importada anteriormente 
            if (isTabelaImportada()) {
                this.log.setTipo(TipoLog.REIMPORTACAO_SIGTAP.get());
                atualizarProgresso(1, 0, "D");
                this.daoSigtapUpload.excluir(); //excluindo tabela sigtap
                this.daoSigtapUploadLog.excluir(); //excluindo log  
            } else {
                this.log.setTipo(TipoLog.IMPORTACAO_SIGTAP.get());
            }

            atualizarProgresso(1, 1, "Tipos de Leitos");
            this.daoSigtapUpload.salvarTB_TIPO_LEITO(montarTB_TIPO_LEITO());

            atualizarProgresso(1, 2, "Rubricas");
            this.daoSigtapUpload.salvarTB_RUBRICA(montarTB_RUBRICA());

            atualizarProgresso(2, 3, "Financiamentos");
            this.daoSigtapUpload.salvarTB_FINANCIAMENTO(montarTB_FINANCIAMENTO());

            atualizarProgresso(3, 4, "CIDs");
            this.daoSigtapUpload.salvarTB_CID(montarTB_CID());

            atualizarProgresso(4, 5, "Modalidades");
            this.daoSigtapUpload.salvarTB_MODALIDADE(montarTB_MODALIDADE());

            atualizarProgresso(5, 6, "Procedimentos");
            this.daoSigtapUpload.salvarTB_PROCEDIMENTO(montarTB_PROCEDIMENTO());

            atualizarProgresso(6, 7, "RL. Procedimentos/Leitos");
            this.daoSigtapUpload.salvarRL_PROCEDIMENTO_LEITO(montarRL_PROCEDIMENTO_LEITO());

            atualizarProgresso(7, 8, "RL. Procedimentos/CIDs");
            this.daoSigtapUpload.salvarRL_PROCEDIMENTO_CID(montarRL_PROCEDIMENTO_CID());

            atualizarProgresso(8, 8, "F");
            //deletando a pasta onde o arquivo foi descompactado
            deletarPasta(new File(diretorio));

            //salvando log específico do sigtap upload
            this.sigtapUploadLog.setDataImportacao(data);
            this.sigtapUploadLog.setChaveMesAno(chaveMesAno);
            new SigtapUploadLogDAOImpl().salvar(this.sigtapUploadLog);

            //salvando log na tabela de logs
            salvarLog();

            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Tabela Sigtap de " + mostrarMes() + " importada com sucesso!");

        } catch (FileNotFoundException | DAOException e) {
            FacesUtil.adicionarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    /**
     * método que apaga os arquivos e a pasta onde o arquivo da tabela Sigtap
     * foi descompactado
     *
     * @param f
     */
    public void deletarPasta(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; ++i) {
                deletarPasta(files[i]);
            }
        }
        f.delete();
    }

    /**
     * Atualiza o progresso
     *
     * @param indice
     * @param qtd
     * @param nome
     */
    public void atualizarProgresso(int indice, int qtd, String nome) {
        this.progresso = ((Integer) (indice * 100) / qtdTabelas);
        switch (nome) {
            case "F":
                this.mensagem = "Finalizando...";
                break;
            case "D":
                this.mensagem = "Deletando Tabelas...";
                break;
            default:
                this.mensagem = "Gravando Tabela " + qtd + " de " + qtdTabelas + ". " + nome + "...";
                break;
        }
    }

    /**
     * método que monta e salva o log
     *
     */
    public void salvarLog() {
        this.daoLog = new LogDAOImpl();
        SimpleDateFormat fmt = new SimpleDateFormat("MM/yyyy");
        String detalhe = "referente ao mês: " + fmt.format(this.data)
                + ", nome do arquivo: " + this.nomeArquivo + ".";

        //passando as demais informações 
        this.log.setDetalhe(detalhe);
        this.log.setDataHora(new Date());
        this.log.setObjeto("sigtapUpload");
        this.log.setUsuario(this.usuarioBean.getUsuario());
        //salvando o log
        this.daoLog.salvar(this.log);
    }

    /**
     * método que exibe o último log na página
     *
     * @return
     */
    public String ultimoLog() {
        this.log = new LogDAOImpl().ultimoLogPorObjeto("sigtapUpload");
        return this.log != null ? "Última importação feita em " + ConverterDataHora.formatarDataHora(this.log.getDataHora()) + " por " + this.log.getUsuario().getLogin() + "." : "";
    }

    //abaixo estão os métodos que fazem a leitura linha a linha dos txts e os 
    //enviam para serem salvos no BD, contém um método para cada tabela.
    /**
     * método que passa os dados do arquivo txt para uma lista do objeto a ser
     * montado
     *
     * @return list tb_tipos_leitos
     * @throws java.io.FileNotFoundException
     */
    public List montarTB_TIPO_LEITO() throws FileNotFoundException {

        try {
            FileReader reader = new FileReader(diretorio + "/tb_tipo_leito.txt");
            BufferedReader leitor = new BufferedReader(reader);
            String linha = null;

            //laço que lê linha a linha
            while ((linha = leitor.readLine()) != null) {
                TB_TIPO_LEITO tb_tipo_leito = new TB_TIPO_LEITO();

                //lendo a linha e a quebrando com substring para enviar para o objeto
                tb_tipo_leito.setCO_TIPO_LEITO(linha.substring(0, 2).trim());
                tb_tipo_leito.setNO_TIPO_LEITO(linha.substring(2, 62).trim());
                tb_tipo_leito.setDT_COMPETENCIA(fmtData.parse(linha.substring(62, 68)));
                tb_tipo_leito.setChaveMesAno(chaveMesAno);

                tb_tipos_leitos.add(tb_tipo_leito);

            }

            leitor.close();
            reader.close();

        } catch (IOException | ParseException ex) {
            System.out.println("Erro: " + ex.getMessage());
            ex.printStackTrace();
        }

        return this.tb_tipos_leitos;
    }

    /**
     * método que passa os dados do arquivo txt para uma lista do objeto a ser
     * montado
     *
     * @return list tb_rubricas
     * @throws java.io.FileNotFoundException
     */
    public List montarTB_RUBRICA() throws FileNotFoundException {

        try {
            FileReader reader = new FileReader(diretorio + "/tb_rubrica.txt");
            BufferedReader leitor = new BufferedReader(reader);
            String linha = null;

            //laço que lê linha a linha
            while ((linha = leitor.readLine()) != null) {

                TB_RUBRICA tb_rubrica = new TB_RUBRICA();

                //lendo a linha e a quebrando com substring para enviar para o objeto
                tb_rubrica.setCO_RUBRICA(linha.substring(0, 6).trim());
                tb_rubrica.setNO_RUBRICA(linha.substring(6, 106).trim());
                tb_rubrica.setDT_COMPETENCIA(fmtData.parse(linha.substring(106, 112)));
                tb_rubrica.setChaveMesAno(chaveMesAno);

                tb_rubricas.add(tb_rubrica);
            }
            leitor.close();
            reader.close();

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            ex.printStackTrace();

        }
        return tb_rubricas;
    }

    /**
     * método que passa os dados do arquivo txt para uma lista do objeto a ser
     * montado
     *
     * @return list tb_financiamentos
     * @throws java.io.FileNotFoundException
     */
    public List montarTB_FINANCIAMENTO() throws FileNotFoundException {

        try {
            FileReader reader = new FileReader(diretorio + "/tb_financiamento.txt");
            BufferedReader leitor = new BufferedReader(reader);
            String linha = null;

            //laço que lê linha a linha
            while ((linha = leitor.readLine()) != null) {

                TB_FINANCIAMENTO tb_financiamento = new TB_FINANCIAMENTO();

                //lendo a linha e a quebrando com substring para enviar para o objeto
                tb_financiamento.setCO_FINANCIAMENTO(linha.substring(0, 2).trim());
                tb_financiamento.setNO_FINANCIAMENTO(linha.substring(2, 102).trim());
                tb_financiamento.setDT_COMPETENCIA(fmtData.parse(linha.substring(102, 108)));
                tb_financiamento.setChaveMesAno(chaveMesAno);

                tb_financiamentos.add(tb_financiamento);
            }
            leitor.close();
            reader.close();

        } catch (IOException | ParseException ex) {
            System.out.println("Erro: " + ex.getMessage());
            ex.printStackTrace();

        }
        return tb_financiamentos;
    }

    /**
     * método que passa os dados do arquivo txt para uma lista do objeto a ser
     * montado
     *
     * @return list tb_tipos_cids
     * @throws java.io.FileNotFoundException
     */
    public List montarTB_CID() throws FileNotFoundException {

        try {
            FileReader reader = new FileReader(diretorio + "/tb_cid.txt");
            BufferedReader leitor = new BufferedReader(reader);
            String linha = null;

            //laço que lê linha a linha
            while ((linha = leitor.readLine()) != null) {
                TB_CID tb_cid = new TB_CID();

                //lendo a linha e a quebrando com substring para enviar para o objeto
                tb_cid.setCO_CID(linha.substring(0, 4).trim());
                tb_cid.setNO_CID(linha.substring(4, 104).trim());
                tb_cid.setTP_AGRAVO(linha.substring(104, 105));
                tb_cid.setTP_SEXO(linha.substring(105, 106));
                tb_cid.setTP_ESTADIO(linha.substring(106, 107));
                tb_cid.setVL_CAMPOS_IRRADIADOS(Integer.parseInt(linha.substring(107, 111)));
                tb_cid.setChaveMesAno(chaveMesAno);

                tb_cids.add(tb_cid);

            }
            leitor.close();
            reader.close();

        } catch (IOException | NumberFormatException ex) {
            System.out.println("Erro: " + ex.getMessage());
            ex.printStackTrace();

        }
        return tb_cids;
    }

    /**
     * método que passa os dados do arquivo txt para uma lista do objeto a ser
     * montado
     *
     * @return list tb_modalidades
     * @throws java.io.FileNotFoundException
     */
    public List montarTB_MODALIDADE() throws FileNotFoundException {

        try {
            FileReader reader = new FileReader(diretorio + "/tb_modalidade.txt");
            BufferedReader leitor = new BufferedReader(reader);
            String linha = null;

            //laço que lê linha a linha
            while ((linha = leitor.readLine()) != null) {

                TB_MODALIDADE tb_modalidade = new TB_MODALIDADE();

                //lendo a linha e a quebrando com substring para enviar para o objeto
                tb_modalidade.setCO_MODALIDADE(linha.substring(0, 2).trim());
                tb_modalidade.setNO_MODALIDADE(linha.substring(2, 102).trim());
                tb_modalidade.setDT_COMPETENCIA(fmtData.parse(linha.substring(102, 108)));
                tb_modalidade.setChaveMesAno(chaveMesAno);

                tb_modalidades.add(tb_modalidade);
            }
            leitor.close();
            reader.close();

        } catch (IOException | ParseException ex) {
            System.out.println("Erro: " + ex.getMessage());
            ex.printStackTrace();

        }
        return tb_modalidades;
    }

    /**
     * método que passa os dados do arquivo txt para uma lista do objeto a ser
     * montado
     *
     * @return list tb_procedimentos
     * @throws java.io.FileNotFoundException
     */
    public List montarTB_PROCEDIMENTO() throws FileNotFoundException {

        try {
            FileReader reader = new FileReader(diretorio + "/tb_procedimento.txt");
            BufferedReader leitor = new BufferedReader(reader);
            String linha = null;

            //laço que lê linha a linha
            while ((linha = leitor.readLine()) != null) {

                TB_PROCEDIMENTO tb_procedimento = new TB_PROCEDIMENTO();
                TB_RUBRICA tb_rubrica = new TB_RUBRICA();
                TB_FINANCIAMENTO tb_financiamento = new TB_FINANCIAMENTO();

                //lendo a linha e a quebrando com substring para enviar para o objeto
                tb_procedimento.setCO_PROCEDIMENTO(linha.substring(0, 10).trim());
                tb_procedimento.setNO_PROCEDIMENTO(linha.substring(10, 260).trim());
                tb_procedimento.setTP_COMPLEXIDADE(linha.substring(260, 261));
                tb_procedimento.setTP_SEXO(linha.substring(261, 262));
                tb_procedimento.setQT_MAXIMA_EXECUCAO(Integer.parseInt(linha.substring(262, 266)));
                tb_procedimento.setQT_DIAS_PERMANENCIA(Integer.parseInt(linha.substring(266, 270)));
                tb_procedimento.setQT_PONTOS(Integer.parseInt(linha.substring(270, 274)));
                tb_procedimento.setVL_IDADE_MINIMA(Integer.parseInt(linha.substring(274, 278)));
                tb_procedimento.setVL_IDADE_MAXIMA(Integer.parseInt(linha.substring(278, 282)));
                tb_procedimento.setVL_SH(Double.parseDouble(linha.substring(282, 292)));
                tb_procedimento.setVL_SA(Double.parseDouble(linha.substring(292, 302)));
                tb_procedimento.setVL_SP(Double.parseDouble(linha.substring(302, 312)));

                tb_financiamento.setCO_FINANCIAMENTO(linha.substring(312, 314).trim());
                tb_procedimento.setTb_financiamento(tb_financiamento);

                //somente seta o CO_RUBRICA se ele tiver valor
                if (!linha.substring(314, 320).trim().equals("")) {
                    tb_rubrica.setCO_RUBRICA(linha.substring(314, 320).trim());
                    tb_procedimento.setTb_rubrica(tb_rubrica);

                }

                tb_procedimento.setDT_COMPETENCIA(fmtData.parse(linha.substring(324, 330)));
                tb_procedimento.setChaveMesAno(chaveMesAno);

                tb_procedimentos.add(tb_procedimento);
            }

            leitor.close();
            reader.close();

        } catch (IOException | NumberFormatException | ParseException ex) {
            System.out.println("Erro: " + ex.getMessage());
            ex.printStackTrace();

        }
        return tb_procedimentos;
    }

    /**
     * método que passa os dados do arquivo txt para uma lista do objeto a ser
     * montado
     *
     * @return list rl_procedimentos_leitos
     * @throws java.io.FileNotFoundException
     */
    public List montarRL_PROCEDIMENTO_LEITO() throws FileNotFoundException {

        try {
            FileReader reader = new FileReader(diretorio + "/rl_procedimento_leito.txt");
            BufferedReader leitor = new BufferedReader(reader);
            String linha = null;

            //laço que lê linha a linha
            while ((linha = leitor.readLine()) != null) {

                RL_PROCEDIMENTO_LEITO rl_procedimento_leito = new RL_PROCEDIMENTO_LEITO();
                TB_PROCEDIMENTO tb_procedimento = new TB_PROCEDIMENTO();
                TB_TIPO_LEITO tb_tipo_leito = new TB_TIPO_LEITO();

                //lendo a linha e a quebrando com substring para enviar para o objeto
                tb_procedimento.setCO_PROCEDIMENTO(linha.substring(0, 10).trim());
                rl_procedimento_leito.setTb_procedimento(tb_procedimento);

                tb_tipo_leito.setCO_TIPO_LEITO(linha.substring(10, 12).trim());
                rl_procedimento_leito.setTb_tipo_leito(tb_tipo_leito);

                rl_procedimento_leito.setDT_COMPETENCIA(fmtData.parse(linha.substring(12, 18)));
                rl_procedimento_leito.setChaveMesAno(chaveMesAno);

                rl_procedimentos_leitos.add(rl_procedimento_leito);

            }

            leitor.close();
            reader.close();

        } catch (IOException | ParseException ex) {
            System.out.println("Erro: " + ex.getMessage());
            ex.printStackTrace();

        }
        return rl_procedimentos_leitos;
    }

    /**
     * método que passa os dados do arquivo txt para uma lista do objeto a ser
     * montado
     *
     * @return list rl_procedimentos_cids
     * @throws java.io.FileNotFoundException
     */
    public List montarRL_PROCEDIMENTO_CID() throws FileNotFoundException {

        try {
            FileReader reader = new FileReader(diretorio + "/rl_procedimento_cid.txt");
            BufferedReader leitor = new BufferedReader(reader);
            String linha = null;

            //laço que lê linha a linha
            while ((linha = leitor.readLine()) != null) {

                RL_PROCEDIMENTO_CID rl_procedimento_cid = new RL_PROCEDIMENTO_CID();
                TB_PROCEDIMENTO tb_procedimento = new TB_PROCEDIMENTO();
                TB_CID tb_cid = new TB_CID();

                //lendo a linha e a quebrando com substring para enviar para o objeto
                tb_procedimento.setCO_PROCEDIMENTO(linha.substring(0, 10).trim());
                rl_procedimento_cid.setTb_procedimento(tb_procedimento);

                tb_cid.setCO_CID(linha.substring(10, 14).trim());
                rl_procedimento_cid.setTb_cid(tb_cid);

                rl_procedimento_cid.setST_PRINCIPAL(linha.substring(14, 15));

                rl_procedimento_cid.setDT_COMPETENCIA(fmtData.parse(linha.substring(15, 21)));
                rl_procedimento_cid.setChaveMesAno(chaveMesAno);

                rl_procedimentos_cids.add(rl_procedimento_cid);

            }

            leitor.close();
            reader.close();

        } catch (IOException | ParseException ex) {
            System.out.println("Erro: " + ex.getMessage());
            ex.printStackTrace();

        }
        return rl_procedimentos_cids;
    }

    /**
     * @return the progresso
     */
    public Integer getProgresso() {
        return progresso;
    }

    /**
     * @return the mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * @return the nomeArquivo
     */
    public String getNomeArquivo() {
        if (!nomeArquivo.equals("")) {
            //removendo os caracteres ".zip" do arquivo 
            return nomeArquivo.substring(0, nomeArquivo.length() - 4);
        } else {
            return nomeArquivo;
        }
    }

    //setter necessario para a anotação @ManagedProperty funcionar corretamente
    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

}
