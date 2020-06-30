package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String[] gracze = new String[]{ "1# Przedsiebiorca" };
        if (args.length > 0)
            gracze = args;

        Rozgrywka[] rozgrywki = new Rozgrywka[gracze.length];
        for (int i = 0; i < gracze.length; i++) {
            rozgrywki[i] = new Rozgrywka(gracze[i]);
        }

        Scanner scanner = new Scanner(System.in);

        boolean loop = true;
        while (loop) {
            loop = false;
            for (Rozgrywka g : rozgrywki) {
                if (g.isOver())
                    continue;

                System.out.print("\n Podejmij decyzje, wybierz opcje " + g + "--> ");
                if (!scanner.hasNextLine())
                    return;

                String input = scanner.nextLine();
                g.process(input);
                loop = true;
            }
        }
    }
}