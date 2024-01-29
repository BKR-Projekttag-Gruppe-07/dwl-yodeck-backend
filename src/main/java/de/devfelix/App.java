package de.devfelix;

import de.devfelix.util.Employee;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    static WebSocketServer server = new WebSocketServer(8887);
    static List<String> displayedObjects = new ArrayList<>();
    static final int MILLISECONDS_TO_SLEEP = 2500;

    public static void main(String[] args) {
        server.start();
        clearObjectsAtWeek();
        getDisplayedObjects();

        while (true) {
            if (isMidnight()) {
                clearObjectsAtWeek();
                getDisplayedObjects();
            }
            sendObjects();
        }
    }

    public static void clearObjectsAtWeek() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

        if (dayOfWeek.compareTo(DayOfWeek.TUESDAY) >= 0 && dayOfWeek.compareTo(DayOfWeek.FRIDAY) <= 0) {
            displayedObjects.clear();
            System.out.println("Die Liste wurde gelöscht");
        } else {
            System.out.println("Die Liste wurde nicht gelöscht");
        }
    }

    public static void sendObjects() {
        for (String displayedObject : displayedObjects) {
            System.out.println(displayedObject);
            if (displayedObjects.isEmpty()) {
                server.broadcast("Hier Könnte;Ihre;werbung;stehen");
            } else {
                System.out.println(displayedObject);
                server.broadcast(displayedObject);
            }

            try {
                Thread.sleep(MILLISECONDS_TO_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void getDisplayedObjects() {
        for (Employee employee : Employee.getCurrentEmployees()) {
            if (isBirthdayToday(employee)) {
                displayedObjects.add("Herzlichen Glückwunsch;"+employee.getFirstname() + ";" + employee.getName() +";zum Geburtstag");
            }

            LocalDate hiringDate = LocalDate.parse(employee.getHiringDate());

            if (isJubileeToday(hiringDate)) {
                Duration duration = Duration.between(hiringDate.atStartOfDay(), LocalDate.now().atStartOfDay());
                long years = duration.toDays() / 365;

                displayedObjects.add("Herzlichen Glückwunsch;"+employee.getFirstname() + ";" + employee.getName() + ";zum " +
                        years + " Jährigem Jubiläum");
            }
        }

        if (displayedObjects.isEmpty()) {
            System.out.println("!!! Heute kommt nichts !!!\n");
            server.broadcast("Heute stehen;Keine ;Ereignisse oder;Geburtstage an");
        } else {
            System.out.println("Die Liste wurde Beschrieben\n");
        }
    }

    private static boolean isBirthdayToday(Employee employee) {
        LocalDate birthday = LocalDate.parse(employee.getBirthday());
        return birthday.getMonth() == LocalDate.now().getMonth() &&
                birthday.getDayOfMonth() == LocalDate.now().getDayOfMonth();
    }

    private static boolean isJubileeToday(LocalDate hiringDate) {
        return hiringDate.getMonth() == LocalDate.now().getMonth() &&
                hiringDate.getDayOfMonth() == LocalDate.now().getDayOfMonth();
    }

    private static boolean isMidnight() {
        LocalDateTime now = LocalDateTime.now();
        return now.toLocalTime().equals(LocalTime.MIDNIGHT);
    }
}