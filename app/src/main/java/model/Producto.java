package model;

/**
 * Created by usuario on 08/02/2015.
 */
public class Producto {

    private String     Nombre  ;
    private int  Id,IdPedido;
    //private ArrayList<String> genre;

    public Producto() {
    }
    public Producto(String Nombre  , int Id, int IdPedido) {

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

