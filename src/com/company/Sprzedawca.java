package com.company;

public class Sprzedawca extends Pracownik {
    private int daysAtWork = 0;

    public Sprzedawca(String name) {
        super(name);
    }

    public String toString() {
        return name + " Sprzedawca";
    }

    public void searchForProjects() {
        if (++daysAtWork % Opcje.DAYS_TO_NEW_PROJECT == 0) {
            Market.getInstance().addNewProject(this);
        }
    }
}