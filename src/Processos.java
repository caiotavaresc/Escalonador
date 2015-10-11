import java.util.*;

//Classe que modela a tabela de processos
public class Processos {
	
	//Tabela de Processos
	public static Map<String, BCP> tabelaDeProcessos;
	
	//Constantes de controle dos estados do processo
	public static final int PRONTO = 0;
	public static final int EXECUTANDO = 1;
	public static final int BLOQUEADO = 2;
	
	//Método que inicializa a tabela
	public static void inicializaTabela()
	{
		tabelaDeProcessos = new TreeMap<String, BCP>();
	}
	
	//Método para criar um novo processo
	public static BCP createProcess(String titulo, List<String> instrucoes, int prioridade)
	{
		System.out.println("Carregando " + titulo);
		
		//Criar o BCP
		BCP bcp = new BCP();
		bcp.PC = 0;
		bcp.state = PRONTO;
		bcp.codigo = instrucoes;
		bcp.prioridade = prioridade;
		bcp.creditos = prioridade;
		
		//Colocar o processo na tabela
		tabelaDeProcessos.put(titulo, bcp);
		
		return bcp;
	}
}