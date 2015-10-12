import java.util.List;

/*Essa classe eh uma generalizacao que simboliza o processador
 * Nao dividimos as unidades (UC, ULA ...) nem implementamos os ciclos
 * O gerenciamento de memoria eh feito dentro do processo e n�o dentro de
 * uma "memoria" em algum lugar */

public class Processador {

	public static int PC, X, Y;
	public static String IR;
	public static List<String> memoria;
	
	public static void setContexto(BCP contexto)
	{
		PC = contexto.PC;
		X = contexto.X;
		Y = contexto.Y;
		memoria = contexto.codigo;
	}
	
	public static void getContexto(BCP destino)
	{
		destino.PC = PC;
		destino.X = X;
		destino.Y = Y;
		destino.codigo = memoria;
	}
	
	public static void processar() throws Exception
	{
		//Ciclo de busca
		IR = memoria.get(PC);
		PC++;
		
		//Ciclo de execucao
		interpretarInstrucao();
	}
	
	public static void interpretarInstrucao() throws Exception
	{
		//Validar a instrucao pelo primeiro caractere
		char opt;
		opt = IR.charAt(0);
		
		switch(opt)
		{
		case 'X':
			//Atribuicao em X;
			X = Integer.valueOf(IR.substring(2));
			
			break;
		case 'Y':
			//Atribuido em Y;
			Y = Integer.valueOf(IR.substring(2));
			
			break;
		case 'C':
			//Execucao de um Comando qualquer;
			break;
		case 'E':
			//Operacao de Entrada e Saida;
			//A excecaoo significa uma chamada ao sistema, passando o codigo E
			//Que significa tratamento de excecao.
			throw new Exception("E");
			
		case 'S':
			//Saida do programa
			//A excecao significa uma chamada ao sistema, passando o c�digo S
			//Que significa fim de programa.
			throw new Exception("S");
		}
	}
	
}
