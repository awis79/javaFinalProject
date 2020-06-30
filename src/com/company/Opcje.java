package com.company;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Opcje {
    public final static LocalDate START_GRY = LocalDate.of(2020,1, 1);
    public final static long HASHCODE_GRY = 10; //LocalDateTime.now().hashCode();
    public final static double COMPANY_INITIAL_CASH = 1000.0;
    public final static int MIN_COMPLEX_PROJECT_COUNT = 3;
    public final static int MIN_SALESMAN_PROJECT_COUNT = 1;
    public final static int WORK_ITEM_MAX_DAYS = 10; //28;
    public final static int COMPLEX_WORK_ITEM_DAYS = 5;
    public final static int INITIAL_PROJECT_COUNT = 3;
    public final static int INITIAL_FREE_EMPLOYEES = 3;
    public final static int DAYS_TO_NEW_PROJECT = 5;
    public final static int PROGRAMMER_MAX_BUG_RATE = 10;
    public final static int PROGRAMMER_MAX_DELAY_RATE = 20;
    public final static double EMPLOYEE_MIN_WAGE = 50.0;
    public final static double EMPLOYEE_MAX_WAGE = 200.0;
    public final static double EMPLOYEE_FIXED_COST = 50.0;
    public final static double EMPLOYEE_SOCIAL_TAX_RATE = 0.32;
    public final static double EMPLOYMENT_COST = 25.0;
    public final static double LAY_OFF_COST = 25.0;
    public final static double SALES_TAX = 0.1;
    public final static int SICK_DAYS_PER_YEAR = 14;
    public final static double HEADHUNTER_COST = 50.0;
    public final static double PROJECT_FAIR_COST = EMPLOYEE_MAX_WAGE + EMPLOYEE_FIXED_COST;
    public final static int PROJECT_MAX_MARGIN = 10;
    public final static int PROJECT_MAX_PAYMENT_DELAY = 30;
    public final static double PROJECT_DOWN_PAYMENT = 0.2;
    public final static double PROJECT_PENALTY = 0.1;

    public final static LocalDate[] HOLIDAYS = new LocalDate[] {
            LocalDate.of(2020,5,1),
            LocalDate.of(2020,12,25),
    };

    public static boolean isWorkday(LocalDate date) {
        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return false;
        }
        for (LocalDate h : Opcje.HOLIDAYS) {
            if (h.getMonth().equals(date.getMonth()) && h.getDayOfMonth() == date.getDayOfMonth())
                return false;
        }
        return true;
    }
}