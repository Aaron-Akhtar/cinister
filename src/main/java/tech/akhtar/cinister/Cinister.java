package tech.akhtar.cinister;

import tech.akhtar.cinister.runnables.Scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/***
 * @author Aaron Akhtar - Akhtar Technologies
 */
public class Cinister {

    public static int CONNECT_TIMEOUT = 0;
    private static File file = null;

                            // I reverted to using this kind of colouring system rather then using a enum
                            // to store all the values because having to type EnumClass.COLOUR_VAL.get() is
                            // too much of a hassle.
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static void save(String h){
        try(PrintWriter writer = new PrintWriter(new FileWriter(file, true), true)){
            writer.println(h);
        }catch (IOException e){

        }
    }

    public static void main(String[] args){
        if (args.length != 3){
            System.out.println(RED + "Invalid Parameters: java -jar Cinister.jar <threads> <connection_timeout> <results-file>");
            System.out.println(RESET + "    threads: number of concurrent scanner threads you want running.");
            System.out.println(RESET + "    connection_timeout: how many milliseconds until scanner gives up on a connection (Recommended: 1300 [1.3s]).");
            System.out.println(RESET + "    results-file: what file should cinister save all successful targets.");
            return;
        }

        int THREADS = 0;

        try{
            THREADS = Integer.parseInt(args[0]) * 1000;
            CONNECT_TIMEOUT = Integer.parseInt(args[1]);
        }catch (Exception e){
            System.out.println(RED + "Un-parsable Parameter...");
            return;
        }

        file = new File(args[2]);
        if (!file.exists()){
            System.out.println(RED + "Please enter a file that already exists...");
            return;
        }
        System.out.println(YELLOW + "Cinister - A Akhtar Technologies Production.");
        System.out.println("  domain: akhtar.tech");
        System.out.println("  github: https://github.com/aaron-akhtar/cinister");
        System.out.println(" ");
        List<Thread> ts = new ArrayList<>();
        for (int x = 0; x < THREADS; x++){
            ts.add(new Thread(new Scanner()));
            ts.get(x).start();
        }

        System.out.println(RESET);           //sets colour back to default terminal colour upon exit.
    }

}
