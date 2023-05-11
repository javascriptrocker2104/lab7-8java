import java.net.MalformedURLException;
import java.net.URL;

/** Класс для хранения пар URL-адресов и значений глубины, а также для их возврата по запросу **/
public class URLDepthPair {
    private URL url;//адрес
    private int depth;//глубина
    public URLDepthPair(String URL, int depth){//конструктор
        this.depth = depth;
        try {
            this.url = new URL(URL);
        }
        catch (MalformedURLException e) {//исключение
            this.url = null;
        }
    }
    public String getURL(){
        return this.url.toString();
    }
    public int getDepth(){
        return this.depth;
    }
    public String toString(){//вывод содержимого пары
        String depths = Integer.toString(this.depth);
        return depths + '\t' + this.url;
    }
    public String getPath(){
        return this.url.getPath();
    }//возвращает путь к заданному объекту файла
    public String getHost(){
        return this.url.getHost();
    }//возвращает хост с указанным универсальным кодом ресурса
}