package model;

/**
 * Created by PEDRO QUISPE ALVAREZ on 08/07/2015.
 */
public class Contacto {

    private String Contacto, Celular, Email, Id  ;
    private int  IdAgente;
    public Contacto() {
    }
    public Contacto(String Contacto, String Email  , String Id, int IdAgente, String Celular) {

        this.Celular = Celular;
        this.IdAgente= IdAgente;
        this.Contacto= Contacto;
        this.Email= Email;
        this.Id= Id;

    }
    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public int getIdAgente() {
        return IdAgente;
    }

    public void setIdAgente(int IdAgente) {
        this.IdAgente = IdAgente;
    }

    public String getContacto() {
        return Contacto;
    }

    public void setContato(String Contacto) {
        this.Contacto = Contacto;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }
}