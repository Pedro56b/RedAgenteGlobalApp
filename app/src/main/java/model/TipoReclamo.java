package model;

/**
 * Created by usuario on 18/02/2015.
 */
public class TipoReclamo  {

    private String     Nombre  ;
    private int  Id;
    //private ArrayList<String> genre;
    public TipoReclamo() {
    }
    public TipoReclamo(int Id,String Nombre ) {
        this.Nombre = Nombre;
        this.Id= Id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

}
