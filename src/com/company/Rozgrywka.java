package com.company;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class Rozgrywka {

    private static Random r = new Random(Opcje.HASHCODE_GRY);

    public static int nextInt(int n) {
        return r.nextInt(n);
    }

    public static double nextFairDouble(double min, double max) {
        return Double.max(min, Double.min(max, min + (max - min) / 2 + r.nextGaussian() * (max - min) / 2));
    }

    private interface Action {
        boolean matches(String input);
        boolean action();
        String help();
    }

    private abstract class AbstractAction implements Action {
        protected final String regex;
        protected final String help;
        protected String[] words;

        protected AbstractAction(String regex, String help) {
            this.regex = regex;
            this.help = help;
        }

        public boolean matches(String input) {
            if (!Pattern.matches(regex, input)) {
                return false;
            }
            words = input.split(" ");
            return true;
        }

        public String help() {
            return help;
        }
    }

    private class Help extends AbstractAction {
        public Help() {
            super("help", "help: wyświetla listę poleceń do wykonania");
        }
        public boolean action() {
            for (Action a : moves) {
                System.out.println(a.help());
            }
            return false;
        }
    }

    private class EndGame extends AbstractAction {
        public EndGame() {
            super("quit", "quit : zamyka rozgrywke");
        }
        public boolean action() {
            gameOver("Wyjscie z gry");
            return false;
        }
    }

    private class ShowAvailableProjects extends AbstractAction {
        public ShowAvailableProjects() {
            super("market","market : wyświetla listę projektow do realizacji");
        }
        public boolean action() {
            market.showAvailableProjects();
            return false;
        }
    }

    private class SearchProject extends AbstractAction {
        public SearchProject() {
            super("search", "search : przeznacza dzień na wyszukiwanie potecjalnych nowych projektów");
        }
        public boolean action() {
            market.searchForNewProject();
            return true;
        }
    }

    private class SignContract extends AbstractAction {
        public SignContract() {
            super("sign[ \t\n\r].*", "sign NAME : podpisanie kontraktu na projekt NAME");
        }
        public boolean action() {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String projectName = words[1];
            Praca praca = market.findProject(projectName);
            if (praca == null) {
                System.out.println("Taki projekt nie istnieje: " + projectName);
                return false;
            }
            if (praca.isComplex() && !firma.hasEmployees()) {
                System.out.println(projectName + "jest nie do zrealizowania przez ciebie i twoj zespol");
                return false;
            }
            market.prace.remove(praca);
            firma.prace.add(praca);
            praca.contractor = firma;
            praca.deadline = today.plusDays(praca.daysForDelivery());
            System.out.println("Podpisales kontrakt na projekt o nazwie " + praca);
            if (praca.getDownPayment() != 0.0) {
                firma.receivePayment(praca.getDownPayment(), projectName);
                praca.payment = praca.getDownPayment();
            }
            return true;
        }
    }

    private class ShowProjects extends AbstractAction {
        public ShowProjects() {
            super("projects", "projects : pokazuje liste twoich projektow");
        }
        public boolean action() {
            firma.showProjects();
            return false;
        }
    }

    private class ShowCash extends AbstractAction {
        public ShowCash() {
            super("cash", "cash : pokazuje stan twojego konta");
        }
        public boolean action() {
            firma.showCash();
            return false;
        }
    }

    private class WorkOnProject extends AbstractAction {
        public WorkOnProject() {
            super("work", "work : wykonujesz projekt");
        }
        public boolean action() {
            for (Praca p : firma.prace) {
                if (p.doTheJob(firma.owner, false)) {
                    System.out.println(firma.owner.name + " pracował nad " + p.name);
                    if (p.isDone()) {
                        System.out.println("Projekt " + p.name + " zostal UKONCZONY");
                    }
                    break;
                }
            }
            return true;
        }
    }

    private class TestSoftware extends AbstractAction {
        public TestSoftware() {
            super("test", "test : przeznacza dzień na testowanie programu");
        }
        public boolean action() {
            for (Praca p : firma.prace) {
                if (p.hasBugs()) {
                    p.debug();
                    break;
                }
            }
            return true;
        }
    }

    private class DeliverProject extends AbstractAction {
        public DeliverProject() {
            super("deliver[ \t\n\r].*", "deliver : oddanie gukonczonego projektu");
        }
        public boolean action() {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String projectName = words[1];
            Praca praca = firma.findProject(projectName);
            if (praca == null) {
                System.out.println("Taki projekt nie istnieje: --> " + projectName);
                return false;
            }
            if (!praca.isDone()) {
                System.out.println("Projekt " + projectName + " nie zostal ukonczony!");
                return false;
            }
            firma.prace.remove(praca);
            firma.projectsDone.add(praca);
            praca.owner.prace.add(praca);
            praca.deliveryDate = today;
            praca.paymentDate = today.plusDays(praca.paymentDelay + praca.owner.getExtraPaymentDelay());
            System.out.println("Zdano projekt: " + projectName);

            if (praca.next != null) {
                praca = praca.next;

                firma.prace.add(praca);
                praca.contractor = firma;
                praca.deadline = today.plusDays(praca.daysForDelivery());
                System.out.println("wdrazanie kolejnej czesci " + praca.name);
                if (praca.getDownPayment() != 0.0) {
                    firma.receivePayment(praca.getDownPayment(), projectName);
                    praca.payment = praca.getDownPayment();
                }
            }
            return true;
        }
    }

    private class ShowAvailableEmployees extends AbstractAction {
        public ShowAvailableEmployees() {
            super("interview", "interview : pokaż listę wolnych pracowników");
        }
        public boolean action() {
            market.showAvailableEmployees();
            return false;
        }
    }

    private class SearchAvailableEmployees extends AbstractAction {
        public SearchAvailableEmployees() {
            super("pay headhunter", "pay headhunter : zapłać zpecjaliscie " + Opcje.HEADHUNTER_COST + " za przeszukiwanie rynku pracy");
        }
        public boolean action() {
            firma.cash -= Opcje.HEADHUNTER_COST;
            market.addNewEmployee();
            return true;
        }
    }

    private class HireNewEmployee extends AbstractAction {
        public HireNewEmployee() { super("hire[ \t\n\r].*", "hire NAME : zatrudnia pracownika NAME"); }
        public boolean action() {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String employeeName = words[1];
            Pracownik pracownik = market.findEmployee(employeeName);
            if (pracownik == null) {
                pracownik = firma.findStudent(employeeName);
                if (pracownik == null) {
                    System.out.println("Pracownika " + employeeName + " nie istnieje");
                    return false;
                }
                firma.pracownicy.add(pracownik);
                System.out.println("Zleciles zadanie pracownikowi: " + employeeName);
                return false; // same day ?
            }
            market.pracownicy.remove(pracownik);
            firma.pracownicy.add(pracownik);
            firma.cash -= Opcje.EMPLOYMENT_COST;
            System.out.println("Zatrudniles pracownika: " + employeeName);
            return true;
        }
    }

    private class FireEmployee extends AbstractAction {
        public FireEmployee() {
            super("fire[ \t\n\r].*", "fire NAME : zwalnia pracownika NAME");
        }
        public boolean action() {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String employeeName = words[1];
            Pracownik pracownik = firma.findEmployee(employeeName);
            if (pracownik == null) {
                System.out.println("Pracownika " + employeeName + " nie istnieje ");
                return false;
            }
            firma.pracownicy.remove(pracownik);
            if (firma.studenci.contains(pracownik)) {
                System.out.println("Zwolniles pracownika: " + employeeName);
                return false; // same day
            }
            market.pracownicy.add(pracownik);
            firma.cash -= Opcje.LAY_OFF_COST;
            System.out.println("Zwolniles pracownika: " + employeeName);
            return true;
        }
    }

    private class ShowStaff extends AbstractAction {
        public ShowStaff() {
            super("staff", "staff : wyswietla twoich pracownikow");
        }
        public boolean action() {
            firma.showStaff();
            return false;
        }
    }

    private class ShowEmployees extends AbstractAction {
        public ShowEmployees() {
            super("employees", "employees : wyswietla twoich pracownikow");
        }
        public boolean action() {
            firma.showEmployees();
            return false;
        }
    }

    private class DoTaxes extends AbstractAction {
        public DoTaxes() {
            super("taxes", "taxes : przeznacza dzień na rozrachunki firmy");
        }
        public boolean action() {
            firma.taxDays ++;
            return true;
        }
    }

    private LocalDate today = Opcje.START_GRY;
    private String finalScore = "";
    private List<Action> moves = new ArrayList<>();
    private Market market = Market.getInstance();
    private final Firma firma;

    public Rozgrywka(String playerName) {
        System.out.println("nowa gra " + playerName);
        firma = new Firma(playerName);

        moves.add(new Help());
        moves.add(new ShowAvailableProjects());
        moves.add(new ShowAvailableEmployees());
        moves.add(new ShowStaff());
        moves.add(new ShowEmployees());
        moves.add(new ShowProjects());
        moves.add(new ShowCash());

        moves.add(new SearchAvailableEmployees());
        moves.add(new SearchProject());
        moves.add(new SignContract());
        moves.add(new WorkOnProject());
        moves.add(new TestSoftware());
        moves.add(new DeliverProject());
        moves.add(new HireNewEmployee());
        moves.add(new FireEmployee());
        moves.add(new DoTaxes());
        moves.add(new EndGame());
    }

    public String toString() {
        return firma.owner.name;
    }

    public boolean isOver() {
        return !finalScore.isEmpty();
    }

    public void gameOver(String reason) {
        finalScore = "ZAKONCZENIE GRY: " + reason;
        System.out.println(finalScore);
    }

    public void process(String input) {

        for (Action a : moves) {
            if (a.matches(input)) {
                if (a.action()) {
                    endOfDay();
                }
                return;
            }
        }

        System.out.println("NIEPRAWIDLOWE POSUNIECIE");
    }

    private void endOfDay() {
        LocalDate tomorrow = today.plusDays(1);

        if (Opcje.isWorkday(today)) {
            employeesAtWork();
        }

        payFixedCosts();
        paySalaries();

        if (tomorrow.getDayOfMonth() == 1) {
            if (firma.taxDays < 2) {
                gameOver("kontrola skarbowa, przegrales, poniewaz sie nie rozliczyles");
                return;
            }
            firma.taxDays = 0;

            double tax = firma.monthlyIncome * Opcje.SALES_TAX;
            System.out.println("Podatek sprzedazowy za " + today.getMonth() + " o wartosci: " + tax);
            firma.cash -= tax;
        }

        if (firma.cash < 0.0) {
            gameOver("ZBANKRUTOWALES! Przegrywasz");
            return;
        }

        clientsPayBills();

        if (isEndOfGame()) {
            gameOver("Jestes niesamowity, wygrałeś gre!");
            return;
        }

        today = tomorrow;
        System.out.println("Witam " + today + " " + today.getDayOfWeek());
    }

    private boolean isEndOfGame() {
        int complexProjectCount = 0;
        int salesmanProjectCount = 0;

        for (Praca p : firma.projectsDone) {
            if (p.isComplex() && p.wasFullyPaid() && !p.wasHandmade()) {
                complexProjectCount ++;
                if (p.sprzedawca != null && firma.pracownicy.contains(p.sprzedawca)) {
                    salesmanProjectCount ++;
                }
            }
        }

        return complexProjectCount >= Opcje.MIN_COMPLEX_PROJECT_COUNT &&
                salesmanProjectCount >= Opcje.MIN_SALESMAN_PROJECT_COUNT &&
                firma.cash > Opcje.COMPANY_INITIAL_CASH;
    }

    private void employeesAtWork() {
        for (Pracownik e : firma.pracownicy) {
            if (e.isSick()) {
                System.out.println(e.name + " nie może pracować!");
                continue;
            }
            if (e instanceof Sprzedawca) {
                Sprzedawca sprzedawca = (Sprzedawca) e;
                System.out.println(e.name + " szuka nowych przedsiewziec");
                sprzedawca.searchForProjects();
                continue;
            }
            if (e instanceof Testy) {
                System.out.println(e.name + " przeprowadza testy produktu");
                continue;
            }
            if (e instanceof Programista) {
                Programista programista = (Programista) e;
                boolean tested = firma.hasTesterCoverage();
                for (Praca p : firma.prace) {
                    if (p.doTheJob(programista, tested)) {
                        System.out.println(e.name + " poswiecal czas na " + p.name);
                        if (p.isDone()) {
                            System.out.println("Projekt " + p.name + " zostal UKONCZONY");
                        }
                        break;
                    }
                }
            }
        }
    }

    private void payFixedCosts() {
        for (Pracownik e : firma.pracownicy) {
            firma.cash -= Opcje.EMPLOYEE_FIXED_COST;
        }
    }

    private void paySalaries() {
        List<Pracownik> leavingpracownicy = new ArrayList<>();
        for (Pracownik e : firma.pracownicy) {
            if (firma.cash > e.salary) {
                firma.cash -= e.salary;
            } else {
                firma.cash = 0.0;
                leavingpracownicy.add(e);
            }
        }
        for (Pracownik e : leavingpracownicy) {
            firma.pracownicy.remove(e);
            market.pracownicy.add(e);
            System.out.println(e.name + " zlozyl wypowiedzenie");
        }
        for (Pracownik e : firma.pracownicy) {
            firma.cash -= e.salary * Opcje.EMPLOYEE_SOCIAL_TAX_RATE;
        }
    }

    private void clientsPayBills() {
        for (Klient c : market.klienci) {
            c.payBills(today);
        }
    }
}