package edu.iau.abjad.AbjadApp;

public class children {


     String photo_URL;
     String first_name;
     String child_id;

    public children() {

    }

    public children(String photo_URL, String first_name, String child_id) {
        this.photo_URL = photo_URL;
        this.first_name = first_name;
        this.child_id = child_id;
    }

    public String getPhoto_URL() {
        return photo_URL;
    }

    public void setPhoto_URL(String photo_URL) {
        this.photo_URL = photo_URL;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }
}
