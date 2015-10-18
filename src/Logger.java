
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
public class Logger {
    private FileWriter l;
    public Logger(){
        try {
            l = new FileWriter(String.format("%02d", Escalonador._quantum)+".txt");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void write(String x) throws IOException
    {
        l.write(x + "\n");
    }
    
    public void close()
    {
    	try
    	{
    		l.close();
    	}
    	catch(IOException e)
    	{
    		System.out.println("Nao foi possivel fechar o arquivo de log.");
    	}
    }
}
