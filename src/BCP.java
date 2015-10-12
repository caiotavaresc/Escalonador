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
	//N�mero de cr�ditos restantes
	int creditos;
	
	//Registradores de uso gen�rico
	int X;
	int Y;
	
	//Espa�o contendo o c�digo
	List<String> codigo;
	
	public boolean equals(BCP o)
	{
		return o.id==this.id;
	}
}
