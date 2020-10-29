package com.example.dbms_app;

class Subjects {
    private String sub_name;
    private int total_lectures;
    private int attended_lectures;

    public Subjects(String sub_name, int total_lectures, int attended_lectures) {
        this.sub_name = sub_name;
        this.total_lectures = total_lectures;
        this.attended_lectures = attended_lectures;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public int getTotal_lectures() {
        return total_lectures;
    }

    public void setTotal_lectures(int total_lectures) {
        this.total_lectures = total_lectures;
    }

    public int getAttended_lectures() {
        return attended_lectures;
    }

    public void setAttended_lectures(int attended_lectures) {
        this.attended_lectures = attended_lectures;
    }
}
