package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Market {

    private static Market instance = new Market();

    public static Market getInstance() {
        return instance;
    }

    public List<Klient> klienci = new ArrayList<>();
    public List<Praca> prace = new ArrayList<>();
    public List<Pracownik> pracownicy = new ArrayList<>();
    private int counter = 0;

    private Market() {
        for (int i = 0; i < Opcje.INITIAL_PROJECT_COUNT; i++)
            addNewProject(null);
        for (int i = 0; i < Opcje.INITIAL_FREE_EMPLOYEES; i++) {
            addNewEmployee();
        }
    }

    private Klient getRandomClient() {
        Klient c;

        int i = new Random().nextInt(klienci.size() + 1);
        if (i < klienci.size()) {
            c = klienci.get(i);
        } else {
            c = Klient.getRandomClient();
            klienci.add(c);
        }
        return c;
    }

    public void addNewProject(Sprzedawca sprzedawca) {
        Klient cl = getRandomClient();
        Praca p = Praca.generateNewProject(cl, sprzedawca);
        prace.add(p);
    }

    public void showAvailableProjects() {
        for (Praca p : prace)
            System.out.println(p.toLongString());
    }

    public void searchForNewProject() {
        if (++counter % Opcje.DAYS_TO_NEW_PROJECT == 0) {
            addNewProject(null);
        }
    }

    public Praca findProject(String projectName) {
        for (Praca p : prace) {
            if (p.name.equals(projectName)) {
                return p;
            }
        }
        return null;
    }

    public void addNewEmployee() {
        Pracownik e = null;
        switch (Rozgrywka.nextInt(3)) {
            case 0:
                e = Programista.getNewProgrammer(
                        Rozgrywka.nextInt(Opcje.PROGRAMMER_MAX_BUG_RATE),
                        Rozgrywka.nextInt(Opcje.PROGRAMMER_MAX_DELAY_RATE));
                break;
            case 1:
                e = new Testy(Pracownik.getNextName());
                break;
            case 2:
                e = new Sprzedawca(Pracownik.getNextName());
                break;
            default:
                return;
        }
        pracownicy.add(e);
        System.out.println("Zatrudniles  " + e);
    }

    public void showAvailableEmployees() {
        for (Pracownik e : pracownicy)
            System.out.println(e + " wynagrodzenie: " + e.salary);
    }

    public Pracownik findEmployee(String name) {
        for (Pracownik e : pracownicy) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
