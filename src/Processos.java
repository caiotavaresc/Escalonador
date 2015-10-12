import java.util.*;

//Classe que modela a tabela de processos
public class Processos {
	
	//Tabela de Processos
	public static Map<BCP, String> tabelaDeProcessos;
	
	//Constantes de controle dos estados do processo
	public static final int PRONTO = 0;
	public static final int EXECUTANDO = 1;
	public static final int BLOQUEADO = 2;
        public static final int FINALIZADO = 3;
        
	
	//Identificador crescente e �nico do processo
	public static int lastInd;
	
	//M�todo que inicializa a tabela
	public static void inicializaTabela()
	{
		tabelaDeProcessos = new HashMap<BCP, String>();
	}
	
	//M�todo para criar um novo processo
	public static BCP createProcess(String titulo, List<String> instrucoes, int prioridade)
	{				
		//Criar o BCP
		BCP bcp = new BCP();
		bcp.id = lastInd++;
		bcp.PC = 0;
		bcp.state = PRONTO;
		
		bcp.codigo = new ArrayList<String>();
		bcp.codigo.addAll(instrucoes);
		
		bcp.prioridade = prioridade;
		bcp.creditos = prioridade;
		
		//Colocar o processo na tabela
		tabelaDeProcessos.put(bcp, titulo);
		
		return bcp;
	}
	
	public static String getTitulo(BCP processo)
	{
		String titulo;
		
		titulo = tabelaDeProcessos.get(processo);
		
		return titulo;
	}
}