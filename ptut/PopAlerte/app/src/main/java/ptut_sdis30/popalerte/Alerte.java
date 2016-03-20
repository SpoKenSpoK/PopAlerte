package ptut_sdis30.popalerte;

public class Alerte {
    private int _id;
    private String _totale; //boolean pour si c'est juste les données de loc ou tout
    private String _alertename;
    private String _description;
    private String _enCours; // chaine a true ou false
    private String _type;
    private String _date;
    private String _longitude;
    private String _latitude;
    private String _rayon;

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

    public String get_totale() {
        return _totale;
    }

    public void set_totale(String _totale) {
        this._totale = _totale;
    }

    public String get_enCours() {
        return _enCours;
    }

    public void set_enCours(String _enCours) {
        this._enCours = _enCours;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_longitude() {
        return _longitude;
    }

    public void set_longitude(String _longitude) {
        this._longitude = _longitude;
    }

    public String get_latitude() {
        return _latitude;
    }

    public void set_latitude(String _latitude) {
        this._latitude = _latitude;
    }

    public String get_rayon() {
        return _rayon;
    }

    public void set_rayon(String _rayon) {
        this._rayon = _rayon;
    }
}
