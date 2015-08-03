package model;


/**
 * Created by usuario on 10/01/2015.
 */
public class Pedido {

    private String   Nombre, Tipo, Descripcion ,Publicidad ,PublicidadDetalle, Pedido, Estado, Fecha, Dir;
    private int  Id;
    //private ArrayList<String> genre;

    public Pedido() {
    }

    public Pedido(String Tipo, String Dir, String Descripcion , int Id , String Nombre, String Publicidad ,String PublicidadDetalle,String  Pedido, String Estado , String Fecha) {

        this.Tipo = Tipo;
        this.Descripcion = Descripcion;
        this.Id= Id;
        this.Nombre = Nombre;
        this.Publicidad=Publicidad ;
        this.PublicidadDetalle =PublicidadDetalle;
        this.Pedido = Pedido ;
        this.Estado = Estado ;
        this.Fecha = Fecha ;
        this.Dir = Dir;

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

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getPublicidad() {
        return Publicidad;
    }

    public void setPublicidado(String Publicidad) {
        this.Publicidad = Publicidad;
    }

    public String getPublicidadDetalle() {
        return PublicidadDetalle;
    }

    public void setPublicidadDetalle(String PublicidadDetalle) {
        this.PublicidadDetalle = PublicidadDetalle;
    }

    public String getDir() {
        return Dir;
    }

    public void setDir(String Dir) {
        this.Dir = Dir;
    }

    public String getPedido() {
        return Pedido;
    }

    public void setPedido(String Pedido) {
        this.Pedido = Pedido;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }


    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }






}
