package de.devfelix;

import de.devfelix.util.Employee;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class App {

    static WebSocketServer server = new WebSocketServer(8887);
    static List<String> displayedObjects = new ArrayList<>();
    static final int MILLISECONDS_TO_SLEEP = 3500;

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

    private static void clearObjectsAtWeek() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        if (isWeekday(dayOfWeek) || isHoliday(today)) {
            displayedObjects.clear();
            System.out.println("Die Liste wurde gelöscht");
        } else {
            System.out.println("Die Liste wurde nicht gelöscht");
        }
    }

    private static boolean isWeekday(DayOfWeek dayOfWeek) {
        return dayOfWeek.compareTo(DayOfWeek.TUESDAY) >= 0 && dayOfWeek.compareTo(DayOfWeek.FRIDAY) <= 0;
    }


    private static boolean isHoliday(LocalDate date) {
        HolidayManager manager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.GERMANY));
        Set<LocalDate> holidays = manager.getHolidays(date.getYear()).stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());
        return holidays.contains(date);
    }

    public static void sendObjects() {
        if (displayedObjects.isEmpty()) {
            server.broadcast("Heute stehen keine;Geburtstage oder Jubileen;an");
            System.out.println("Heute stehen keine;Geburtstage oder Jubileen;an");
        } else {
            for (String displayedObject : displayedObjects) {
                System.out.println(displayedObject);
                server.broadcast(displayedObject);
                try {
                    Thread.sleep(MILLISECONDS_TO_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }

    }


    public static void getDisplayedObjects() {
        for (Employee employee : Employee.getCurrentEmployees()) {
            if (isBirthdayToday(employee)) {
                displayedObjects.add("Herzlichen Glückwunsch;"+employee.getFirstname() + " " + employee.getName() +";zum Geburtstag");
            }

            LocalDate hiringDate = LocalDate.parse(employee.getHiringDate());

            if (isJubileeToday(hiringDate)) {
                Duration duration = Duration.between(hiringDate.atStartOfDay(), LocalDate.now().atStartOfDay());
                int years = (int) (duration.toDays() / 365);
                String tempYears = checkIfIsSmaller(years);
                displayedObjects.add("Herzlichen Glückwunsch;"+employee.getFirstname() + " " + employee.getName() + ";zum " +
                        tempYears + "jährigen Jubiläum");
            }
        }

        if (displayedObjects.isEmpty()) {
            System.out.println("!!! Heute kommt nichts !!!\n");
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

    private static String checkIfIsSmaller(int years) {
        String[] numbers = {"Null","eins", "zwei", "drei", "vier", "fünf", "sechs", "sieben", "acht", "neun", "zehn", "elf", "zwölf"};
        String year;
        if (years <= numbers.length) {
            year = numbers[years];
        } else {
            year = Integer.toString(years)+" ";
        }
        return year;
    }
}