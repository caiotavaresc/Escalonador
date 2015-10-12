import java.util.*;

//Comparador de créditos para ordenar os processos na fila
public class CompareCreditos<T> implements Comparator<BCP>{

	public int compare(BCP ori, BCP dest)
	{
		int retorno = dest.creditos - ori.creditos;
		
		//Se os processos tiverem a mesma quantidade de créditos,
		//vale aquele que está em execução no momento
		if(retorno==0)
		{
			if(ori.state==Processos.EXECUTANDO)
				retorno = -1;
			else if(dest.state==Processos.EXECUTANDO)
				retorno = 1;
			else
				retorno = 0;
		}
		
		return retorno;
	}
	
}
