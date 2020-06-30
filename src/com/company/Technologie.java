package com.company;

import java.util.ArrayList;
import java.util.List;

public enum Technologie {
    FRONT_END, BACK_END, WORDPRESS, BAZYDANYCH, PROGRAMOWANIE_APP_MOBILNYCH, PRESTASHOP;

    public static Technologie[] getRandomTechnologies() {
        List<Technologie> list = new ArrayList<>();
        int count = 1 + Rozgrywka.nextInt(Technologie.values().length);
        for (int i = 0; i < count; i++) {
            Technologie t = Technologie.values()[Rozgrywka.nextInt(Technologie.values().length)];
            if (list.contains(t))
                continue;
            list.add(t);
        }
        return list.toArray(new Technologie[0]);
    }
}