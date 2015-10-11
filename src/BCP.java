import java.util.*;

//Classe que modela o bloco de controle de Processos
public class BCP implements Comparable<BCP>{
	
	//Contador de programa
	int PC;
	//Indicador de estado do processo
	int state;
	//Prioridade do processo
	int prioridade;
	//N�mero de cr�ditos restantes
	int creditos;
	
	//Registradores de uso gen�rico
	int X;
	int Y;
	
	//Espa�o contendo o c�digo
	List codigo;
	
	//M�todo de compara��o para poder ordenar a lista de processos prontos
	//pela quantidade de cr�ditos
	public int compareTo(BCP o)
	{
		//Invers�o para trazer a maior prioridade primeiro
		int retorno = o.creditos - this.creditos;
		
		//Heur�stica para casos de empate
		//Em caso de empate, tem maior prioridade o processo
		//que j� est� em execucao
		if(retorno==0)
		{
			if(this.state == Processos.EXECUTANDO)
				retorno = -1;
			else
				retorno = 1;
		}
		
		return retorno;
	}
}
