import java.util.*;

//Classe que modela o bloco de controle de Processos
public class BCP{
	
	//Identificador unívoco do processo
	int id;
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
	List<String> codigo;
	
	public boolean equals(BCP o)
	{
		return o.id==this.id;
	}
}
