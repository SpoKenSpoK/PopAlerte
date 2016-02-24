package com.example.yorokobii.popalerte;

public class Alerte {
    private int _id;
    private String _alertename;

    public Alerte(String _alertename) {
        this._alertename = _alertename;
    }

    public int get_id() {
        return _id;
    }

    public String get_alertename() {
        return _alertename;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_alerte_name(String _alertename) {
        this._alertename = _alertename;
    }
}
