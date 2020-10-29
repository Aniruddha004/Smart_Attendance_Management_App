package com.example.dbms_app;

class Stundent {
    private String name, _class, add, uid;

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

    public Stundent(String name, String _class, String add, String uid) {
        this.name = name;
        this._class = _class;
        this.add = add;
        this.uid = uid;
    }
}
