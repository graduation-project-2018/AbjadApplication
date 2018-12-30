package edu.iau.abjad.AbjadApp;

import java.io.Serializable;
@SuppressWarnings("serial")

public class child_info_new implements Serializable {

    String first_name;
    String last_name;
    String gender;
    String photo_URL;

    public child_info_new(String fname, String lname, String cgender, String photo){

        this.first_name =fname;
        this.last_name=lname;
        this.gender=cgender;
        this.photo_URL=photo;

    }//end of constructor
    public void setPhoto(String photo)
    {
        //include more logic
        this.photo_URL = photo;
    }
}