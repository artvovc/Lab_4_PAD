package com.database;

import com.model.Empl;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static DataBase ourInstance = new DataBase();

    public static DataBase getInstance() {
        return ourInstance;
    }

    private List<Empl> empls = new ArrayList<>();

    private DataBase() {
    }

    public List<Empl> getEmpls() {
        return empls;
    }

    public void setEmpls(List<Empl> empls) {
        this.empls = empls;
    }
}
