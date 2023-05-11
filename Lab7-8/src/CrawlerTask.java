import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class CrawlerTask implements Runnable{
    URLPool upool;
    public static final String URL_PREFIX = "<a href=\"http";
    @Override
    public void run() {
        do {
            try {
                URLDepthPair pair = upool.getPair();
                int currDepth = pair.getDepth();
                try {
                    Socket s = new Socket(pair.getHost(), 80);//создание сокета
                    s.setSoTimeout(1000);//устанавливает время ожидания сокета
                    PrintWriter out = new PrintWriter(s.getOutputStream(), true);//получение данных с другой стороны соединения
                    BufferedReader in =  new BufferedReader(new InputStreamReader(s.getInputStream()));//отправление данных на другую сторону соединения
                    request(out,pair);//запрос
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.contains(URL_PREFIX)) buildNewUrl(line, currDepth, upool);
                    }
                    s.close();//закрыть сокет
                } catch (IOException e){ }
            } catch (NullPointerException e){ }
        }
        while (true);
    }
    public CrawlerTask(URLPool pool) {
        this.upool = pool;
    }//конструктор
    public static void request(PrintWriter out, URLDepthPair pair) {//запрос
        out.println("GET " + pair.getPath() + " HTTP/1.1");
        out.println("Host: " + pair.getHost());
        out.println("Connection: close");
        out.println();
        out.flush();//очистка модуля записи
    }
    public static void buildNewUrl(String str, int depth,URLPool pool) {//строим адрес
        try {
            String currentLink = str.substring(str.indexOf(URL_PREFIX) +
                    URL_PREFIX.indexOf("\"") + 1, str.indexOf("\"", str.indexOf(URL_PREFIX) +
                    URL_PREFIX.indexOf("\"") + 2));
            pool.addPair(new URLDepthPair(currentLink, depth + 1));
        }
        catch (StringIndexOutOfBoundsException e){ }
    }
}