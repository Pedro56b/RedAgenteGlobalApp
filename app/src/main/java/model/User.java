package model;

/**
 * Created by usuario on 23/03/2015.
 */
public class User {

    private int  Id;
    private String Nombre, Password, thumbnailUrl, Email, Position, IdInterbank;

    public User() {
    }

    public User( int Id, String thumbnailUrl, String Nombre, String Password, String Email, String Position, String IdInterbank) {
        this.Id= Id;
        this.Nombre = Nombre;
        this.Password = Password;
        this.thumbnailUrl = thumbnailUrl;
        this.Email = Email;
        this.Position = Position;
        this.IdInterbank = IdInterbank;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getIdInterbank() {
        return IdInterbank;
    }

    public void setIdInterbank(String IdInterbank) {
        this.IdInterbank = IdInterbank;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
