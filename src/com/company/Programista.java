package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Programista extends Pracownik {
    private final List<Technologie> umiejetnosci = new ArrayList<>();
    private final int raportBledow;
    private final int raportDniowy;

    public Programista(String name, Technologie[] umiejetnosci, int raportBledow, int raportDniowy) {
        super(name);
        Collections.addAll(this.umiejetnosci, umiejetnosci);
        this.raportBledow = raportBledow;
        this.raportDniowy = raportDniowy;
    }

    public Programista(String name, double salary, Technologie[] umiejetnosci, int raportBledow, int raportDniowy) {
        super(name, salary);
        Collections.addAll(this.umiejetnosci, umiejetnosci);
        this.raportBledow = raportBledow;
        this.raportDniowy = raportDniowy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" PROGRAMISTA");
        for (Technologie t : umiejetnosci) {
            sb.append(" ");
            sb.append(t);
        }
        return sb.toString();
    }

    public boolean hasSkill(Technologie t) {
        return umiejetnosci.contains(t);
    }

    public boolean bugFree() {
        return Rozgrywka.nextInt(100) >= raportBledow;
    }

    public boolean onTime() {
        return Rozgrywka.nextInt(100) >= raportDniowy;
    }

    public static Programista getNewProgrammer(int bugRate, int delayRate) {
        String name = getNextName();
        Technologie[] skills = Technologie.getRandomTechnologies();
        return new Programista(name, skills, bugRate, delayRate);
    }
}