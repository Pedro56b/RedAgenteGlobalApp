package model;



/**
 * Created by usuario on 10/01/2015.
 */
public class Reclamos {

    private String Reclamo, Comentario , Estado, Tipo ;
    private int  Id;
    //private ArrayList<String> genre;

    public Reclamos() {
    }

    public Reclamos(int Id,String Reclamo,  String Comentario,String Estado, String Tipo ) {

        this.Id= Id;
        this.Reclamo = Reclamo;
        this.Comentario = Comentario;
        this.Estado = Estado;
        this.Tipo = Tipo;

    }
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getReclamo() {
        return Reclamo;
    }

    public void setReclamo(String Reclamo) {
        this.Reclamo = Reclamo;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String Comentario) {
        this.Comentario = Comentario;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

}
