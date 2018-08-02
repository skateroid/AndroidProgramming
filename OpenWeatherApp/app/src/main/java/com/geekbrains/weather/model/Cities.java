package com.geekbrains.weather.model;

public class Cities {
    private int id;
    private String city;
    private Boolean checked;

    public Cities(int id, String city) {
        this.id = id;
        this.city = city;
        this.checked = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
