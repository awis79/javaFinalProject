package com.company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Klient {
    private static int lastId = 0;

    public enum Type {
        EASY, DIFFICULT, HARD
    }
    public final Type type;
    private final int id;
    public final List<Praca> prace = new ArrayList<>();

    private Klient(Type type) {
        this.type = type;
        this.id = ++lastId;
    }

    public String toString() {
        return id + "_" + type.toString();
    }

    public int getExtraPaymentDelay() {
        int extra = 0;

        if (type.equals(Type.EASY)) {
            if (Rozgrywka.nextInt(100) < 30) {
                extra = 7;
            }
        } else if (type.equals(Type.HARD)) {
            if (Rozgrywka.nextInt(100) < 30) {
                extra = 7;
            } else if (Rozgrywka.nextInt(100) < 5) {
                extra = 30;
            }
        }

        if (extra > 0) {
            System.out.println("Niestety termin wydluzono o " + extra + " dni!");
        }
        return extra;
    }

    public void payBills(LocalDate today) {
        for (Praca p : prace) {
            if (p.closed)
                continue;
            if (p.paymentDate.equals(today)) {
                if (p.hasBugs() && !acceptBugs()) {
                    System.out.println("Rozwiazano kontrakt dotyczacy " + p.name);
                    p.closed = true;

                    while (p.next != null) {
                        p = p.next;
                        System.out.println("Rozwiazano kontrakt dotyczacy " + p.name);
                        p.closed = true;
                    }
                    continue;
                }
                double payment = p.getPrice() - p.getDownPayment();
                if (p.deliveryDate.isAfter(p.deadline)) {
                    if (type.equals(Type.EASY) &&
                            p.deliveryDate.isBefore(p.deadline.plusDays(7)) &&
                            (Rozgrywka.nextInt(100) < 20)) {
                        System.out.println("Uniknales kary tym razem! Uwazaj na przyszlosc");
                    } else {
                        payment -= p.getPenalty();
                    }
                }
                if (isReliablePayer()) {
                    p.contractor.receivePayment(payment, p.name);
                    p.payment += payment;
                }
                p.closed = true;
            }
        }
    }

    private boolean isReliablePayer() {
        switch (type) {
            case HARD:
                return Rozgrywka.nextInt(100) < 1;
            default:
                return true;
        }
    }

    private boolean acceptBugs() {
        switch (type) {
            case EASY:
                return true;
            case DIFFICULT:
                return Rozgrywka.nextInt(100) < 50;
            case HARD:
            default:
                return false;
        }
    }

    public static Klient getRandomClient() {
        Type[] ta = Type.values();
        int i = Rozgrywka.nextInt(ta.length);
        Type t = ta[i];
        return new Klient(t);
    }
}