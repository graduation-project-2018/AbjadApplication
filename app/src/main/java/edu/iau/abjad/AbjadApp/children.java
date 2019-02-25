package edu.iau.abjad.AbjadApp;

public class children {


   //Remove child_ID
    public String first_name;
    public String child_ID;
    public String photo_URL;
    public String destination;
    public children() {

    }

    public children(String photo_URL, String first_name, String id, String des) {
        this.photo_URL = photo_URL;
        this.first_name = first_name;
        this.child_ID = id;
        this.destination = des;
    }

    public String getChild_ID() {
        return child_ID;
    }

    public void setChild_ID(String child_ID) {
        this.child_ID = child_ID;
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
    public String getDestination() {
        return destination;
    }

}
