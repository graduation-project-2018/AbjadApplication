package edu.iau.abjad.AbjadApp;

import java.io.Serializable;
@SuppressWarnings("serial")

public class child_info_new implements Serializable {

   public String first_name;
   public String last_name;
   public String gender;
   public String photo_URL;
   public String first_signIn;

    public child_info_new(String fname, String lname, String cgender, String photo, String f_signIn){

        this.first_name =fname;
        this.last_name=lname;
        this.gender=cgender;
        this.photo_URL=photo;
        this.first_signIn = f_signIn;


    }//end of constructor
    public void setPhoto(String photo)
    {
        //include more logic
        this.photo_URL = photo;
    }

}
