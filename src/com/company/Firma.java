package com.company;

import java.util.ArrayList;
import java.util.List;

public class Firma {
    public double cash = Opcje.COMPANY_INITIAL_CASH;
    public double monthlyIncome = 0.0;
    public Programista owner;
    public List<Programista> studenci = new ArrayList<>();
    public List<Pracownik> pracownicy = new ArrayList<>();
    public List<Praca> prace = new ArrayList<>();
    public List<Praca> projectsDone = new ArrayList<>();
    public int taxDays = 0;

    public Firma(String playerName) {
        owner = new Programista(playerName, 0.0, new Technologie[] {
                Technologie.BAZYDANYCH,
                Technologie.FRONT_END,
                Technologie.WORDPRESS,
                Technologie.PRESTASHOP },
                0, 0);
        studenci.add(new Programista(Pracownik.getNextName(), 0.8 * Pracownik.getFairSalary(), Technologie.getRandomTechnologies(), 5, 0));
        studenci.add(new Programista(Pracownik.getNextName(), 0.4 * Pracownik.getFairSalary(), Technologie.getRandomTechnologies(), 10, 5));
        studenci.add(new Programista(Pracownik.getNextName(), 0.2 * Pracownik.getFairSalary(), Technologie.getRandomTechnologies(), 10, 10));
    }

    public void showCash() {
        System.out.println("Pieniadze na koncie: " + cash);
    }

    public void showStaff() {
        System.out.println("Zespol pracujacy nad projektem:");
        System.out.println(owner);
        for (Pracownik e : studenci)
            System.out.println(e);
    }

    public Programista findStudent(String name) {
        for (Programista p : studenci) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    public boolean hasEmployees() {
        return !pracownicy.isEmpty();
    }

    public Pracownik findEmployee(String name) {
        for (Pracownik e : pracownicy) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }

    public void showEmployees() {
        if (pracownicy.isEmpty()) {
            System.out.println("W zespole nie ma pracownikow, musisz ich zatrudnic!");
        } else {
            System.out.println("Zespol:");
            for (Pracownik e : pracownicy)
                System.out.println(e + " wyplata " + e.salary);
        }
    }

    private int countTesters() {
        int count = 0;
        for (Pracownik e : pracownicy) {
            if (e instanceof Testy)
                count++;
        }
        return count;
    }

    private int countProgrammers() {
        int count = 0;
        for (Pracownik e : pracownicy) {
            if (e instanceof Programista)
                count++;
        }
        return count;
    }

    public boolean hasTesterCoverage() {
        int testers = countTesters();
        if (testers == 0)
            return false;
        return countProgrammers() / testers <= 3;
    }

    public void showProjects() {
        System.out.println("Wykonane przedsiewziecia:");
        for (Praca p : prace)
            System.out.println(p + " koniec czasu " + p.deadline);
    }

    public Praca findProject(String name) {
        for (Praca p : prace) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    public void receivePayment(double bill, String name) {
        System.out.println("Wplacono " + bill + " za " + name);
        cash += bill;
        monthlyIncome += bill;
        showCash();
    }
}