
import java.io.*;
import java.util.*;

//Classe responsavel por escalonar os processos
public class Escalonador {

    //Filas de processos prontos e bloqueados
    private static List<BCP> filaProntos;
    private static List<Bloqueado> filaBloqueados;

    //Variavel que especifica o tamanho do quantum
    private static int _quantum;

    //Metodo para inicializar as filas
    public static void inicializaFilas() {
        filaProntos = new ArrayList<>();
        filaBloqueados = new ArrayList<>();
    }

    //Metodo que carrega os processos na mem�ria
    public static void carregaProcessos() throws IOException {
        //Array de arquivos que sera retornado pelo metodo que lista todos os arquivos do diretorio
        File[] arquivos;
        //Caminho onde est�o os arquivos do processo, prioridades e quantum
        File diretorio;
        //Arquivo do quantum
        //Isso do null foi o Java que fez, se algu�m tiver algo mais bonito...
        File quantum = null;
        //Arquivo de prioridades
        File prioridades;

        //Leitor do arquivo de prioridades
        FileReader myReader, priorityReader;
        BufferedReader myBuffer, bufferedPriorityReader;

        //Lista de instrucoes e bcp genericos
        List<String> instrucoes;
        BCP bcp_processo;

        //Titulo generico
        String titulo;
        String texto;

        //integer de prioridade generico
        int prioridade;

        diretorio = new File("processos");
        prioridades = new File("processos\\prioridades.txt");

        //Listar arquivos do diretorio
        arquivos = diretorio.listFiles();

        instrucoes = new ArrayList<String>();

        priorityReader = new FileReader(prioridades);
        bufferedPriorityReader = new BufferedReader(priorityReader);

        for (int i = 0; i <= arquivos.length - 1; i++) {
            //Se o arquivo for o quantum, guarda-lo
            if (arquivos[i].getName().equals("quantum.txt")) {
                quantum = arquivos[i];
            } else {
                if (arquivos[i].getName().equals("prioridades.txt")) {
                    //O arquivo de prioridades ja foi buscado no in�cio
                    //Isso eh soh para nao deixar o leitor entrar nele
                } else {
                    //Carregar os processos em mem�ria, um a um
                    myReader = new FileReader(arquivos[i]);
                    myBuffer = new BufferedReader(myReader);

                    //A primeira linha � o t�tulo do processo
                    titulo = myBuffer.readLine();

                    //Pegar a prioridade do processo no arquivo de prioridades
                    prioridade = Integer.valueOf(bufferedPriorityReader.readLine());

                    //Carregar todas as instru��es na mem�ria
                    while ((texto = myBuffer.readLine()) != null) {
                        //1 - Colocar o texto na mem�ria
                        instrucoes.add(texto);
                    }

                    //O createProcss retorna o bcp do processo criado
                    bcp_processo = Processos.createProcess(titulo, instrucoes, prioridade);
                    //O bcp vai para a fila de processos prontos
                    filaProntos.add(bcp_processo);
                    //Limpar instru��es e fechar o buffer
                    instrucoes.clear();
                    myBuffer.close();
                }
            }
        }
        //Fechar o buffer de leitura de prioridades
        bufferedPriorityReader.close();

        //Ordenar a lista por prioridade
        Collections.sort(filaProntos, new CompareCreditos<BCP>());
        //Imprimir os processos conforme a prioridade
        Iterator<BCP> it = filaProntos.iterator();
        while (it.hasNext()) {
            bcp_processo = it.next();
            titulo = Processos.getTitulo(bcp_processo);

            System.out.println("Carregando " + titulo);
        }

        //Ler o quantum
        FileReader quantumFileReader;
        BufferedReader buffQuantumFileReader;

        quantumFileReader = new FileReader(quantum);
        buffQuantumFileReader = new BufferedReader(quantumFileReader);

        //Atribuir o valor do quantum e fechar o buffer de leitura
        _quantum = Integer.valueOf(buffQuantumFileReader.readLine());
        buffQuantumFileReader.close();
    }

    public static void escalonar() {
        int indQuantum;
        String titulo = "";
        BCP prox = null;

        //1 - Pegar o processo de maior prioridade - Menor da fila
        Iterator<BCP> i = filaProntos.iterator();
        if (i.hasNext()) {
            //Pegar o primeiro da fila, se ela n�o estiver vazia
            prox = i.next();
            //Verifica os creditos do processo sendo executado, se ele ja era 0, verifica se eh necessaria a redistribuicao
            if (prox.creditos < 0) {
                //Verifica se ha processos com creditos na fila de bloqueados
                Iterator<Bloqueado> b = filaBloqueados.iterator();
                boolean zerou = true;
                while (b.hasNext()) {
                    Bloqueado proc = b.next();
                    if (proc.p.creditos > 0) {
                        zerou = false;
                    }
                }
                //Caso nao existam, faz a redistribuicao
                if (zerou){
                    redistribuirCreditos();
                    return;
                }  
            }
            //Pegar o titulo do processo
            titulo = Processos.getTitulo(prox);

            System.out.println("Executando " + titulo + ", saldo de creditos:" + prox.creditos);

            //Transferir o contexto do BCP para o processador
            Processador.setContexto(prox);

            //Ao comecar a rodar, o processo perde um credito
            prox.creditos--;

            //Mudar o estado para EXECUTANDO
            prox.state = Processos.EXECUTANDO;

                //Decrementa o tempo de espera dos processos bloqueados e se terminou, devolve-os a fila de prontos
            //Isso deve acontecer antes que o processo em execucao seja terminado, pois ele pode acabar na fila de espera
            decrementarEspera();

            //Em seguida executar um quantum de instrucoes
            for (indQuantum = 0; indQuantum < _quantum; indQuantum++) {
                try {
                    Processador.processar();
                } catch (Exception e) {
                    if (e.getMessage().equals("E")) {
                        System.out.println("E/S iniciada em " + titulo);
                        System.out.println("Interrompendo " + titulo + " apos " + (indQuantum + 1) + " instrucoes");

                        //Salvar o contexto
                        Processador.getContexto(prox);

                        //Trocar o estado do processo para bloqueado
                        prox.state = Processos.BLOQUEADO;

                        //Tirar o processo da fila de prontos e p�r na fila de bloqueados
                        filaProntos.remove(prox);
                        filaBloqueados.add(new Bloqueado(prox));

                        //Interromper o quantum
                        break;
                    }

                    if (e.getMessage().equals("S")) {
                        System.out.println("Interrompendo " + titulo + " apos " + (indQuantum + 1) + " instrucoes");
                        System.out.println(titulo + " terminado. X = " + Processador.X + ". Y = " + Processador.Y);

                        //Remover o processo da tabela de processos
                        Processos.tabelaDeProcessos.remove(prox);

                        //Remover o processo da fila de prontos
                        filaProntos.remove(prox);
                        //Mudar o estado para diferencia-los dos terminos de quantum enquanto o processo ainda nao terminou
                        prox.state = Processos.FINALIZADO;
                    }
                }
            }
            //Se a execucao parou porque o quantum terminou e o processo nao foi finalizado, entao
            if (indQuantum == _quantum && prox.state != Processos.FINALIZADO) {
                System.out.println("Interrompendo " + titulo + " apos " + _quantum + " instrucoes");

                //Salvar o contexto
                Processador.getContexto(prox);
            }
        } else {
            //Quando nao ha processos Prontos
            //Decrementa o tempo de espera dos metodos bloqueados
            decrementarEspera();
        }

        //reordenar a fila
        Collections.sort(filaProntos, new CompareCreditos<BCP>());
        
        //Voltando o estado do processo a pronto
        if(prox != null && prox.state == Processos.EXECUTANDO)
            prox.state = Processos.PRONTO;
        

    }

    //Metodo responsavel por decrementar o tempo de espera dos processos bloqueados
    public static void decrementarEspera() {
        Iterator<Bloqueado> b = filaBloqueados.iterator();
        while (b.hasNext()) {
            Bloqueado proxB = b.next();
            proxB.tempoEspera--;

            if (proxB.tempoEspera == 0) {
                proxB.p.state = Processos.PRONTO;
                b.remove();
                filaProntos.add(proxB.p);
            }
        }

    }

    //Metodo para redistribuir creditos aos processos
    public static void redistribuirCreditos() {
        Iterator<BCP> p = filaProntos.iterator();

        while (p.hasNext()) {
            BCP prox = p.next();
            prox.creditos = prox.prioridade;
        }

        Iterator<Bloqueado> b = filaBloqueados.iterator();
        while (b.hasNext()) {
            Bloqueado prox = b.next();
            prox.p.creditos = prox.p.prioridade;
        }

    }

    public static void main(String[] args) throws IOException {

        //Inicializar conjuntos
        inicializaFilas();
        Processos.inicializaTabela();

        //Leitura e carregamento dos processos em mem�ria
        carregaProcessos();

        //Escalonar enquanto houver processos na tabela de processos
        while (!Processos.tabelaDeProcessos.isEmpty()) {
            escalonar();
        }
    }
}
