package de.devfelix.util;

import de.devfelix.MySQLManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Employee {

    private String firstname, name, birthday, hiringDate;
    private int id;

    public Employee(String firstName, String name, String birthday, String hiringDate, int id) {
        this.firstname = firstName;
        this.name = name;
        this.birthday = birthday;
        this.hiringDate = hiringDate;
        this.id = id;
    }

    public static ArrayList<Employee> getCurrentEmployees() {
        ArrayList<Employee> returnValue = new ArrayList<>();

        String sqlStatement = "SELECT * FROM mitarbeiter";
        List<Map<String, Object>> unformattedEmployees = MySQLManager.executeQuery(sqlStatement);

        for(Map<String, Object> employees : unformattedEmployees) {
            Employee employee = new Employee(null, null, null, null, 0);
            for (Map.Entry<String, Object> entry : employees.entrySet()) {
                switch (entry.getKey()) {
                    case "Vorname":
                        employee.setFirstname(entry.getValue().toString());
                        break;
                    case "Nachname":
                        employee.setName(entry.getValue().toString());
                        break;
                    case "Geburtstag":
                        employee.setBirthday(entry.getValue().toString());
                        break;
                    case "Anstelldatum":
                        employee.setHiringDate(entry.getValue().toString());
                        break;
                    case "ID":
                        employee.setId(Integer.parseInt(entry.getKey().toString()));
                        break;
                }
            }
            returnValue.add(employee);
        }
        return returnValue;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHiringDate() {
        return hiringDate;
    }

    public void setHiringDate(String hiringDate) {
        this.hiringDate = hiringDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
