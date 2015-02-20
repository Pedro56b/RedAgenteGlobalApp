package model;



/**
 * Created by usuario on 10/01/2015.
 */
public class Agentes {

    private String thumbnailUrl, NombreAgente, Direccion  ;
    private int  Id;
    //private ArrayList<String> genre;

    public Agentes() {
    }

    public Agentes(String thumbnailUrl,String NombreAgente,  String Direccion, int Id) {
        this.thumbnailUrl = thumbnailUrl;
        this.Direccion = Direccion;
        this.NombreAgente = NombreAgente;
        this.Id= Id;

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

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getNombreAgente() {
        return NombreAgente;
    }

    public void setNombreAgente(String NombreAgente) {
        this.NombreAgente = NombreAgente;
    }
}
