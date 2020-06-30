package com.company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Praca {

    public static class WorkItem {
        public final Technologie t;
        public final int days;
        public int remainingDays;

        public WorkItem(Technologie t, int days) {
            this.t = t;
            this.days = days;
            this.remainingDays = days;
        }

        public String toString() {
            return t + "_" + days + "d" + remainingDays;
        }

        public boolean isDone() {
            return remainingDays == 0;
        }

        public void work() { remainingDays --; }
    }

    public final Klient owner;
    public final Sprzedawca sprzedawca;
    public final String name;
    public final int margin;
    public final int paymentDelay;
    public boolean closed = false;
    public double payment = 0.0;
    private final List<WorkItem> workItems;
    public Praca next;
    public int bugs = 0;
    public int debugDays = 0;
    public Firma contractor;
    public LocalDate deadline;
    public LocalDate deliveryDate;
    public LocalDate paymentDate;
    private boolean handmade = false;

    private Praca(Klient owner, Sprzedawca sprzedawca, String name, int margin, int paymentDelay) {
        this.owner = owner;
        this.sprzedawca = sprzedawca;
        this.name = name;
        this.margin = margin;
        this.paymentDelay = paymentDelay;
        this.workItems = new ArrayList<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        for (WorkItem wi : workItems) {
            sb.append(" ");
            sb.append(wi);
        }
        if (hasBugs()) {
            sb.append(" BLEDY ");
            sb.append(bugs);
            sb.append("/");
            sb.append(debugDays);
        }
        return sb.toString();
    }

    public String toLongString() {
        String phases = "";
        if (this.next != null)
            phases = " + " + this.next.toLongString();
        return toString() + " wartość " + getPrice() + " (" + getMargin() + "%) płatność " + paymentDelay + " dni termin zdania projektu " + daysForDelivery() + " dni" + phases;
    }

    public boolean isSimple() {
        return workItems.size() == 1;
    }

    public boolean isMedium() {
        return !isSimple() && !isComplex();
    }

    public boolean isComplex() {
        return workItems.size() > 2 && getWorkDays() > Opcje.COMPLEX_WORK_ITEM_DAYS;
    }

    public int getWorkDays() {
        int workdays = 0;
        for (WorkItem wi : workItems) {
            workdays += wi.days;
        }
        return workdays;
    }

    public int daysForDelivery() {
        if (isComplex()) {
            return getWorkDays() * 2;
        } else if (isMedium()) {
            return getWorkDays() * 3 / 2;
        } else {
            return getWorkDays();
        }
    }

    public int getMargin() {
        return margin * workItems.size();
    }

    public double getPrice() {
        return Math.round(getWorkDays() * Opcje.PROJECT_FAIR_COST * (100.0 + getMargin()) / 100);
    }

    public double getDownPayment() {
        if (isComplex()) {
            return getPrice() * Opcje.PROJECT_DOWN_PAYMENT;
        }
        return 0.0;
    }

    public double getPenalty() {
        return getPrice() * Opcje.PROJECT_PENALTY;
    }

    public boolean isDone() {
        for (WorkItem wi : workItems)
            if (!wi.isDone())
                return false;
        return true;
    }

    public boolean hasBugs() {
        return bugs > debugDays;
    }

    public void debug() {
        if (hasBugs()) {
            debugDays++;
        }
    }

    public boolean hasTechnology(Technologie t) {
        for (WorkItem wi : workItems)
            if (wi.t.equals(t))
                return true;
        return false;
    }

    public boolean doTheJob(Programista programista, boolean tested) {
        if (closed)
            return false;
        if (programista.equals(contractor.owner)) {
            handmade = true;
        }
        for (WorkItem wi : workItems) {
            if (wi.isDone())
                continue;
            if (programista.hasSkill(wi.t)) {
                if (programista.onTime())
                    wi.work();
                if (!tested && !programista.bugFree()) {
                    this.bugs ++;
                }
                return true;
            }
        }
        return false;
    }

    public boolean wasFullyPaid() {
        return payment >= getPrice();
    }

    public boolean wasHandmade() {
        return handmade;
    }

    private static final String[] codenames = new String[] {
            "Acrux", "Bosona", "Cursa", "Diya", "Emiw", "Franz",
            "Ginan", "Heze", "Itonda", "Jabbah", "Kang", "Lema",
            // ...
    };
    private static final int[] codenameVersions = new int[codenames.length];

    public static Praca generateNewProject(Klient klient, Sprzedawca sprzedawca) {
        int margin = Rozgrywka.nextInt(Opcje.PROJECT_MAX_MARGIN);
        int paymentDelay = Rozgrywka.nextInt(Opcje.PROJECT_MAX_PAYMENT_DELAY);
        int codenameIndex = Rozgrywka.nextInt(codenames.length);
        int phases = 1 + Rozgrywka.nextInt(3);
        String n = codenames[codenameIndex] +  ++codenameVersions[codenameIndex];
        Praca p = new Praca(klient, sprzedawca, n, margin, paymentDelay);

        Technologie[] technologies = Technologie.getRandomTechnologies();
        for (Technologie t : technologies) {
            int days = 1 + Rozgrywka.nextInt(Opcje.WORK_ITEM_MAX_DAYS / phases);
            WorkItem wi = new WorkItem(t, days);
            p.workItems.add(wi);
        }

        Praca next = p;
        for (int i = 2; i <= phases; i++) {
            next.next = new Praca(klient, sprzedawca, n + "_" + i, margin, paymentDelay);
            for (Technologie t : technologies) {
                int days = 1 + Rozgrywka.nextInt(Opcje.WORK_ITEM_MAX_DAYS / phases);
                WorkItem wi = new WorkItem(t, days);
                next.next.workItems.add(wi);
            }
            next = next.next;
        }

        System.out.println("nowy projekt " + p.toLongString());
        return  p;
    }
}