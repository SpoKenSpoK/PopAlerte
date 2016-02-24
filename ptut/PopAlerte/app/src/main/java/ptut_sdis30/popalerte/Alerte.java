package ptut_sdis30.popalerte;

public class Alerte {
    private int _id;
    private String _alertename;
    private String _description;

    public Alerte(String _alertename, String _description) {
        this._alertename = _alertename;
        this._description = _description;
    }

    public int get_id() {
        return _id;
    }

    public String get_alertename() {
        return _alertename;
    }

    public String get_description() {
        return _description;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_alerte_name(String _alertename) {
        this._alertename = _alertename;
    }

    public void set_description(String _description) {
        this._description = _description;
    }
}
