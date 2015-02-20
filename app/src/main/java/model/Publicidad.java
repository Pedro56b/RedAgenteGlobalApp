package model;

/**
 * Created by usuario on 13/02/2015.
 */
public class Publicidad {

    private String     Nombre  ;
    private int  Id, IdPedido;

    //private ArrayList<String> genre;

    public Publicidad() {
    }
    public Publicidad(String Nombre  , int Id , int IdPedido) {

        this.Nombre = Nombre;
        this.IdPedido= IdPedido;
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

    public int getIdPedido() {
        return IdPedido;
    }

    public void setIdPedido(int IdPedido) {
        this.IdPedido = IdPedido;
    }
}
