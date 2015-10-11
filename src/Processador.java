import java.util.List;

/*Essa classe � uma generaliza��o que simboliza o processador
 * N�o dividimos as unidades (UC, ULA ...) nem implementamos os ciclos
 * O gerenciamento de mem�ria � feito dentro do processo e n�o dentro de
 * uma "mem�ria" em algum lugar */
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
		
		//Ciclo de execu��o
	}
	
	public static void interpretarInstrucao() throws Exception
	{
		//Validar a instru��o pelo primeiro caractere
		char opt;
		opt = IR.charAt(0);
		
		switch(opt)
		{
		case 'X':
			//Atribui��o em X;
			X = Integer.valueOf(IR.substring(1));
			
			break;
		case 'Y':
			//Atribui��o em Y;
			Y = Integer.valueOf(IR.substring(1));
			
			break;
		case 'C':
			//Execu��o de um Comando qualquer;
			break;
		case 'E':
			//Opera��o de Entrada e Sa�da;
			//A exce��o significa uma chamada ao sistema, passando o c�digo E
			//Que significa tratamento de exce��o.
			throw new Exception("E");
			
		case 'S':
			//Saida do programa
			//A exce��o significa uma chamada ao sistema, passando o c�digo S
			//Que significa fim de programa.
			throw new Exception("S");
		}
	}
	
}
