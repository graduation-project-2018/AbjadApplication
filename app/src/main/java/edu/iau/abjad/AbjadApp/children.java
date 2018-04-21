package edu.iau.abjad.AbjadApp;

public class children {


   //Remove child_ID
     String first_name;

    String photo_URL;
    public children() {

    }

    public children(String photo_URL, String first_name) {
        this.photo_URL = photo_URL;
        this.first_name = first_name;

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


}
