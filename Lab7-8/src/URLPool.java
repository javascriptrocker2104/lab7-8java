import java.util.LinkedList;

/** класс хранит список всех URL-адресов для поиска и глубину*/
public class URLPool {

    LinkedList<URLDepthPair> urlFind = new LinkedList();//найденные и необработанные
    LinkedList<URLDepthPair> urlRes = new LinkedList();//отсканированные
    int maxDepth;//максимальная глубина
    int waitN;//сколько потоков ожидает новый адрес

    public URLPool(int maxDepth) {//конструктор
        this.maxDepth = maxDepth;
        waitN = 0;
    }
    public synchronized URLDepthPair getPair() {
        while (urlFind.size() == 0) {
            waitN++;
            try {
                wait();
            }
            catch (InterruptedException e) {//исключение
                System.out.println("Ignoring Interrupted Exception");
            }
            waitN--;
        }
        URLDepthPair nextPair = urlFind.getFirst();//первый элемент найденного адреса
        urlFind.removeFirst();//удаление первого элемента
        return nextPair;
    }
    public synchronized void addPair(URLDepthPair pair) {
        if(check(urlRes,pair)){
            urlRes.add(pair);
            if (pair.getDepth() < maxDepth){
                urlFind.add(pair);
                notify();
            }
        }
    }
    public synchronized int getWait() {
        return waitN;
    }
    private static boolean check(LinkedList<URLDepthPair> resultLink, URLDepthPair pair) {
        boolean chekin = true;
        for (URLDepthPair c : resultLink) if (c.toString().equals(pair.toString())) chekin = false;
        return chekin;
    }
    public LinkedList<URLDepthPair> getResult() {
        return urlRes;
    }
}