package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/12/14.
 */
public class User {
    private final String id;
    private final String facebook_id;
    private final String googleplus_id;
    private final String created_on;
    private final String updated_on;
    private final String deleted_on;

    public User() {
        this.id= null;
        this.facebook_id = null;
        this.googleplus_id = null;
        this.created_on = null;
        this.updated_on = null;
       this. deleted_on = null;
    }

    public User(String id, String facebook_id, String googleplus_id, String created_on, String updated_on, String deleted_on) {
        this.id= id;
        this.facebook_id = facebook_id;
        this.googleplus_id = googleplus_id;
        this.created_on = created_on;
        this.updated_on = updated_on;
        this. deleted_on = deleted_on;
    }
    
    public String getId() {
        return id;
    }
    public String getfacebook_id() {
        return facebook_id;
    }
    public String getgoogleplus_id() {
        return googleplus_id;
    }
    public String getcreated_on(){
    	return created_on;
    }
    public String getupdated_on(){
    	return updated_on;
    }
    public String getdeleted_on(){
    	return deleted_on;
    }
}
