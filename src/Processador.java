import java.util.List;

/*Essa classe é uma generalização que simboliza o processador
 * Não dividimos as unidades (UC, ULA ...) nem implementamos os ciclos
 * O gerenciamento de memória é feito dentro do processo e não dentro de
 * uma "memória" em algum lugar */
import java.util.*;

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
	
	public static void processar()
	{
		//Ciclo de busca
		IR = memoria.get(PC);
		PC++;
		
		//Ciclo de execução
	}
	
	public static void interpretarInstrucao() throws Exception
	{
		//Validar a instrução pelo primeiro caractere
		char opt;
		opt = IR.charAt(0);
		
		switch(opt)
		{
		case 'X':
			//Atribuição em X;
			X = Integer.valueOf(IR.substring(1));
			
			break;
		case 'Y':
			//Atribuição em Y;
			Y = Integer.valueOf(IR.substring(1));
			
			break;
		case 'C':
			//Execução de um Comando qualquer;
			break;
		case 'E':
			//Operação de Entrada e Saída;
			//A exceção significa uma chamada ao sistema, passando o código E
			//Que significa tratamento de exceção.
			throw new Exception("E");
			
		case 'S':
			//Saida do programa
			//A exceção significa uma chamada ao sistema, passando o código S
			//Que significa fim de programa.
			throw new Exception("S");
		}
	}
	
}
