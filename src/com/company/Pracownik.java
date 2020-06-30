package com.company;

public class Pracownik {
    public final String name;
    public double salary;

    public Pracownik(String name) {
        this.name = name;
        this.salary = getRandomSalary();
    }

    public Pracownik(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String toString() {
        return name;
    }

    public boolean isSick() {
        return Rozgrywka.nextInt(365) < Opcje.SICK_DAYS_PER_YEAR;
    }

    private static final String[] names = new String[] {
            "Antoni", "Bianca", "Cina", "Daria",
            "Eryk", "Filip", "Greta", "Henryk",
            "Irma", "Józef", "Klara", "Leon", "Mira",
            "Nadia", "Oleg", "Piotr", "Rufus", "Tina",
            "Ula", "Wiktor", "Zenon",
    };
    private static final int[] surnameIndex = new int[names.length];

    public static String getNextName() {
        int index = Rozgrywka.nextInt(names.length);
        return  names[index] + String.valueOf((char)('A' + surnameIndex[index]++));
    }

    public static double getFairSalary() {
        return Opcje.EMPLOYEE_MIN_WAGE + (Opcje.EMPLOYEE_MAX_WAGE - Opcje.EMPLOYEE_MIN_WAGE)/2;
    }

    public static double getRandomSalary() {
        return Math.round(Rozgrywka.nextFairDouble(Opcje.EMPLOYEE_MIN_WAGE, Opcje.EMPLOYEE_MAX_WAGE));
    }
}