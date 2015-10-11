import java.util.*;

//Classe que modela o bloco de controle de Processos
public class BCP implements Comparable<BCP>{
	
	//Contador de programa
	int PC;
	//Indicador de estado do processo
	int state;
	//Prioridade do processo
	int prioridade;
	//Número de créditos restantes
	int creditos;
	
	//Registradores de uso genérico
	int X;
	int Y;
	
	//Espaço contendo o código
	List codigo;
	
	//Método de comparação para poder ordenar a lista de processos prontos
	//pela quantidade de créditos
	public int compareTo(BCP o)
	{
		//Inversão para trazer a maior prioridade primeiro
		int retorno = o.creditos - this.creditos;
		
		//Heurística para casos de empate
		//Em caso de empate, tem maior prioridade o processo
		//que já está em execucao
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
