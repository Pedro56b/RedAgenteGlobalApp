package model;

/**
 * Created by usuario on 18/02/2015.
 */
public class PublicidadDetalle {

    private String     Nombre  ;
    private int  Id, IdPublicidad;

    //private ArrayList<String> genre;

    public PublicidadDetalle() {
    }
    public PublicidadDetalle(String Nombre  , int Id , int IdPublicidad) {

        this.Nombre = Nombre;
        this.IdPublicidad= IdPublicidad;
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

    public int getIdPublicidad() {
        return IdPublicidad;
    }

    public void setIdPublicidad(int IdPublicidad) {
        this.IdPublicidad = IdPublicidad;
    }
}
