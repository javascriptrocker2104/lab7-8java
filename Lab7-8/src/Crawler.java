import java.util.LinkedList;
import java.util.Scanner;

public class Crawler {
    //"http://www.dangdang.com/"
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String a1 = sc.next();//адрес
        String a2 = sc.next();//глубина
        String a3 = sc.next();//количество порождаемых потоков веб-сканера.
        String[] arg = new String[]{a1, a2, a3};
        if (arg.length == 3 && checkNum(arg[1]) && checkNum(arg[2])) {//проверка ввода
            String surl = arg[0];
            int treadNum = Integer.parseInt(arg[2]);

            URLPool pool = new URLPool(Integer.parseInt(arg[1]));
            pool.addPair(new URLDepthPair(surl, 0));

            for (int i = 0; i < treadNum; i++) {
                CrawlerTask c = new CrawlerTask(pool);
                Thread t = new Thread(c);//поток
                t.start();//запуск потока
            }

            while (pool.getWait() != treadNum) {
                try {
                    Thread.sleep(500);//остановка выполнения текущего потока на время
                } catch (InterruptedException e) {//исключение
                    System.out.println("Ignoring Interrupted Exception");
                }
            }
            try {
                showResult(pool.getResult());//вывод результата
            } catch (NullPointerException e) {//исключение
                System.out.println("Not Link");
            }
            System.exit(0);//завершение работы


        } else System.out.println("Error");
    }


    public static void showResult(LinkedList<URLDepthPair> resultLink) {//вывод результата
        for (URLDepthPair c : resultLink)
            System.out.println("Depth: " + c.getDepth() + "\tLink: " + c.toString());
    }

    public static boolean checkNum(String num) {
        boolean ok = true;
        for (int i = 0; i < num.length() && ok; i++) ok = Character.isDigit(num.charAt(i));
        return ok;
    }
}

//Character.isDigit(char ch) определяет, является ли указанный символ цифрой или нет.