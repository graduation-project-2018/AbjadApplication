package edu.iau.abjad.AbjadApp;
import java.io.Serializable;
@SuppressWarnings("serial")
public class childInformation implements Serializable{

    String first_name;
    String last_name;
    String gender;
    String photo_URL;
    String email;


    public childInformation( String fname, String lname, String cgender, String photo, String email){

       this.first_name =fname;
       this.last_name=lname;
       this.gender=cgender;
       this.photo_URL=photo;
       this.email = email;

    }//end of constructor
    public void setPhoto(String photo)
    {
        //include more logic
        this.photo_URL = photo;
    }




}//end of childInformation class
