package model;



/**
 * Created by usuario on 10/01/2015.
 */
public class Reclamos {

    private String Reclamo, Comentario , Estado, Tipo, Fecha, Dir ;
    private int  Id;
    //private ArrayList<String> genre;

    public Reclamos() {
    }

    public Reclamos(int Id,String Reclamo,  String Comentario,String Estado, String Tipo, String Fecha, String Dir ) {

        this.Id= Id;
        this.Reclamo = Reclamo;
        this.Comentario = Comentario;
        this.Dir = Dir;
        this.Estado = Estado;
        this.Tipo = Tipo;
        this.Fecha = Fecha;

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

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }


    public String getDir() {
        return Dir;
    }

    public void setDir(String Dir) {
        this.Dir = Dir;
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
