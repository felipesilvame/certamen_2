package cl.usm.telematica.certamen2.model;

/**
 * Created by Pipos on 23-10-2016.
 */

public class Repository {
    private String name,description,updated_at, url;
    public Repository(){
        name=null;
        description=null;
        updated_at=null;
        url = null;
    }
    public Repository(String name, String description, String updated_at, String url){
        this.name = name;
        this.description = description;
        this.updated_at = updated_at;
        this.url = url;
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public String getUpdated_at(){
        return this.updated_at;
    }
    public String getUrl(){
        return url;
    }
    public void setName(String value){
        name = value;
    }
    public void setDescription(String value){
        description = value;
    }
    public void setUpdated_at(String value){
        updated_at = value;
    }
    public void setUrl(String value){
        url = value;
    }


}