
public class Bloqueado {
    //Processo bloqueado
    BCP p;
    //Tempo que ele deve esperar até voltar aos prontos
    int tempoEspera;
    
    public Bloqueado(BCP b)
    {
        p = b;
        tempoEspera = 2;
    }
    
}
