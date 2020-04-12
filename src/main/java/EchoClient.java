import org.apache.log4j.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EchoClient {
    public static int PORT;
    public static String HOST;

    public static Socket socket;
    public static BufferedReader r;
    public static PrintWriter w;
    public static BufferedReader con;

    // Инициализация логера
    private static Logger log = Logger.getLogger(EchoClient.class);

    private static String logLevelGlobal;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String command = "";
        command = sc.nextLine();

        setConnection(command);
        try {
            socket = new Socket(HOST, PORT);
            r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            w = new PrintWriter(socket.getOutputStream(), true);
            con = new BufferedReader(new InputStreamReader(System.in));
            String line;
            do {
                line = r.readLine();
                if (line != null)
                    if(line.contains("connect")){
                        socket = new Socket(HOST, PORT);
                        log.info("Подключение к серверу : <" + HOST + "> :: <" + PORT + ">");
                        setConnection(line);
                    }
                if(line.contains("send")) {
                    sendMessage(line);
                } else
                if(line.equals("help")){
                    getHelp();
                } else
                if(line.equals("disconnect")){
                    setDisconnect();
                }
                if(line.equals("quit")){
                    log.info("Выход :: command > quit");
                    System.exit(0);
                    break;
                }
                if(line.equals("logLevel")){
                    setLogLevel();
                }
                line = con.readLine();
                w.println(line);
            }
            while (true);
        }
        catch (Exception err) {
            System.err.println(err);
        }
    }

    public static void defineLog(String logLevelGlobal){
        switch (logLevelGlobal){
            case "fatal":
                log.fatal("Log Level :: fatal");
                break;
            case "error":
                log.error("Log Level :: error");
                break;
            case "warn":
                log.warn("Log Level :: warn");
                break;
            case "info":
                log.info("Log Level :: info");
                break;
            case "debug":
                log.debug("Log Level :: debug");
                break;
            case "trace":
                log.trace("Log Level :: trace");
                break;
        }
    }

    public static void setLogLevel(){
        Scanner sc = new Scanner(System.in);
        System.out.println("What level of logging do you want to choose ? Enter...");
        String logLevel = sc.nextLine();

        switch (logLevel){
            case "off":
                log.setLevel(Level.OFF);
                logLevelGlobal = "off";
                break;
            case "fatal":
                log.setLevel(Level.FATAL);
                logLevelGlobal = "fatal";
                break;
            case "error":
                log.setLevel(Level.ERROR);
                logLevelGlobal = "error";
                break;
            case "warn":
                log.setLevel(Level.WARN);
                logLevelGlobal = "warn";
                break;
            case "info":
                log.setLevel(Level.INFO);
                logLevelGlobal = "info";
                break;
            case "debug":
                log.setLevel(Level.DEBUG);
                logLevelGlobal = "debug";
                break;
            case "trace":
                log.setLevel(Level.TRACE);
                logLevelGlobal = "trace";
                break;
            case "all":
                log.setLevel(Level.ALL);
                logLevelGlobal = "all";
                break;
        }
        defineLog(logLevelGlobal);
    }

    // отправляет сообщения
    public static void sendMessage(String str) {
        List<String> allMessage = new ArrayList<String>();
        for (String s : str.split(" ")) {
            allMessage.add(s);
        }

        String s = "";
        for (String s1 : allMessage.subList(1, allMessage.size())) {// не берем слово send, и отпр. след.слова
            s += s1 + " ";
        }

        if (!socket.isClosed()) {
            System.out.println(s);// выводим сообщение
            log.info("Сообщение от сервера :: > " + s);
        } else {
            System.out.println("Oops.. you need connected to 'Servlet'");
            getHelp();
        }
    }

    // устанавливает соединение
    public static void setConnection(String str) {
        List<String> dataConnection = new ArrayList<String>();
        for (String retval : str.split(" ")) {
            if (str.contains("connect")) {
                dataConnection.add(retval);
            } else {
                System.out.println("Oops...");
                getHelp();
            }
        }
        int i = 0;
        for (String s : dataConnection) {
            if (i == 1) {
                HOST = dataConnection.get(1);
            }
            if (i == 2) {
                PORT = Integer.valueOf(dataConnection.get(2));
            }
            i++;
        }
    }

    public static void getHelp(){
        System.out.println("------------------ * * * * -------------------");
        System.out.println("   connect    ::> connect <address> <port>");
        System.out.println("   disconnect ::> disconnect");
        System.out.println("   send       ::> send <message>");
        System.out.println("   help       ::> help");
        System.out.println("   quit       ::> quit");
        System.out.println("------------------ * * * * -------------------");
    }

    public static void setDisconnect() throws IOException {
        log.info("Отключение :: command > disconnect");
        socket.close();
    }
}

//    public static int PORT = 8180;
//    public static String HOST = "127.0.0.1";


