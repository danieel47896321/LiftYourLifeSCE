package com.example.liftyourlife.Class;

import java.io.Serializable;

public class Plan implements Serializable {
    private String PlanName;
    private String Date;
    public Plan(String planName, String date) {
        PlanName = planName;
        Date = date;
    }
    public String getPlanName() {
        return PlanName;
    }
    public void setPlanName(String planName) {
        PlanName = planName;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
}
