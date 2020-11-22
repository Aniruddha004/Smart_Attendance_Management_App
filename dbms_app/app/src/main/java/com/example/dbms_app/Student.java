package com.example.dbms_app;

public class Student {
    private String name;
    private String _class;
    private String add;
    private String uid;
    private String username;
    private String image_path;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Student(String name, String _class, String add, String uid, String username, String password, String image_path) {
        this.name = name;
        this._class = _class;
        this.add = add;
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.image_path = image_path;
    }

}
