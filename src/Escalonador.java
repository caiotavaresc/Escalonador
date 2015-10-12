import java.io.*;
import java.util.*;

//Classe respons�vel por escalonar os processos
public class Escalonador {

	//Filas de processos prontos e bloqueados
	private static List<BCP> filaProntos;
	private static List<BCP> filaBloqueados;
	
	//Vari�vel que especifica o tamanho do quantum
	private static int _quantum;
	
	//M�todo para inicializar as filas
	public static void inicializaFilas()
	{
		filaProntos = new ArrayList<BCP>();
		filaBloqueados = new ArrayList<BCP>();
	}
	
	//M�todo que carrega os processos na mem�ria
	public static void carregaProcessos() throws IOException
	{
		//Array de arquivos que ser� retornado pelo m�todo que l� todos os arquivos do diret�rio
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
		
		//Lista de instru��es e bcp gen�ricos
		List<String> instrucoes;
		BCP bcp_processo;
		
		//T�tulo gen�rico
		String titulo;
		String texto;
		
		//integer de prioridade gen�rico
		int prioridade;
		
		diretorio = new File("processos");
		prioridades = new File("processos\\prioridades.txt");
		
		//Listar arquivos do diretorio
		arquivos = diretorio.listFiles();
		
		instrucoes = new ArrayList<String>();
		
		priorityReader = new FileReader(prioridades);
		bufferedPriorityReader = new BufferedReader(priorityReader);
		
		for(int i = 0; i <= arquivos.length-1; i++)
		{
			//Se o arquivo for o quantum, guard�-lo
			if(arquivos[i].getName().equals("quantum.txt"))
			{
				quantum = arquivos[i];
			}
			else
			{
				if(arquivos[i].getName().equals("prioridades.txt"))
				{
					//O arquivo de prioridades j� foi buscado no in�cio
					//Isso � s� para n�o deixar o leitor entrar nele
				}
				else
				{
					//Carregar os processos em mem�ria, um a um
					myReader = new FileReader(arquivos[i]);
					myBuffer = new BufferedReader(myReader);

					//A primeira linha � o t�tulo do processo
					titulo = myBuffer.readLine();
					
					//Pegar a prioridade do processo no arquivo de prioridades
					prioridade = Integer.valueOf(bufferedPriorityReader.readLine());
							
					//Carregar todas as instru��es na mem�ria
					while((texto = myBuffer.readLine()) != null)
					{
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
		while(it.hasNext())
		{
			bcp_processo = it.next();
			titulo = Processos.getTitulo(bcp_processo);
			
			System.out.println("Carregando "+titulo);
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
	
	public static void escalonar()
	{
		int indQuantum;
		String titulo;;
		
		//1 - Pegar o processo de maior prioridade - Menor da fila
		Iterator<BCP> i = filaProntos.iterator();
		if(i.hasNext())
		{
			//Pegar o primeiro da fila, se ela n�o estiver vazia
			BCP prox = i.next();
			
			//Pegar o t�tulo do processo
			titulo = Processos.getTitulo(prox);
			
			System.out.println("Executando " + titulo);
			
			//Transferir o contexto do BCP para o processador
			Processador.setContexto(prox);
			
			//Ao come�ar a rodar, o processo perde um cr�dito
			prox.creditos--;
			
			//Mudar o estado para EXECUTANDO
			prox.state = Processos.EXECUTANDO;
			
			//Em seguida executar um quantum de instru��es
			for(indQuantum = 0; indQuantum < _quantum; indQuantum++)
			{
				try
				{
					Processador.processar();
				}
				catch(Exception e)
				{
					if(e.getMessage().equals("E"))
					{
						System.out.println("E/S iniciada em " + titulo);
						System.out.println("Interrompendo "+titulo+" ap�s "+ (indQuantum+1) +" instru��es");
						
						//Salvar o contexto
						Processador.getContexto(prox);
						
						//Trocar o estado do processo para bloqueado
						prox.state = Processos.BLOQUEADO;
						
						//Tirar o processo da fila de prontos e p�r na fila de bloqueados
						filaProntos.remove(prox);
						filaBloqueados.add(prox);
						
						//Interromper o quantum
						break;
					}
					
					if(e.getMessage().equals("S"))
					{
						System.out.println(titulo + " terminado. X = "+Processador.X+". Y = "+Processador.Y);
						
						//Remover o processo da tabela de processos
						Processos.tabelaDeProcessos.remove(prox);
						
						//Remover o processo da fila de prontos
						filaProntos.remove(prox);
					}
				}
			}
			//Se a execu��o parou porque o quantum terminou, ent�o
			if(indQuantum==_quantum)
			{
				System.out.println("Interrompendo "+titulo+" ap�s "+_quantum+" instru��es");
				
				//Salvar o contexto
				Processador.getContexto(prox);
			}
			//reordenar a fila
			Collections.sort(filaProntos, new CompareCreditos<BCP>());
		}
		else
		{
			//Se a fila estiver vazia, tomar outra decis�o
			System.out.println("S� tem bloqueados");
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		
		//Inicializar conjuntos
		inicializaFilas();
		Processos.inicializaTabela();
		
		//Leitura e carregamento dos processos em mem�ria
		carregaProcessos();
		
		//Escalonar enquanto houver processos na tabela de processos
		while(!Processos.tabelaDeProcessos.isEmpty())
			escalonar();
	}
}