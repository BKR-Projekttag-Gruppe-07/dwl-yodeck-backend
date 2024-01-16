package de.devfelix.util;

public enum FrontendVariables {

    YEAR("year"),
    EMPLOYEE("name");
    private String variableID;

    private FrontendVariables(String variableID) {
        this.variableID = variableID;
    }

}
