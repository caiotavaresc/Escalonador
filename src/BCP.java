import java.util.*;

//Classe que modela o bloco de controle de Processos
public class BCP{
	
	//Identificador un�voco do processo
	int id;
	//Contador de programa
	int PC;
	//Indicador de estado do processo
	int state;
	//Prioridade do processo
	int prioridade;
	//Numero de cr�ditos restantes
	int creditos;
	//Registradores de uso gen�rico
	int X;
	int Y;
	
	//Espaco contendo o codigo
	List<String> codigo;
	
	public boolean equals(BCP o)
	{
		return o.id==this.id;
	}
}
