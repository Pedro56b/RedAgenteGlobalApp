package model;



/**
 * Created by usuario on 10/01/2015.
 */
public class Agentes {

    private String thumbnailUrl, NombreAgente, Direccion ;
    private String RazonSocial;
    private int  Id;
    private String cta_cte;
    private int idUser;
    private String inicio;
    private String fin;
    private Integer status;


//private ArrayList<String> genre;

    public Agentes() {
    }

    public Agentes(String thumbnailUrl,String NombreAgente,  String Direccion, int Id, String RazonSocial, String cta_cte, int idUser, int status, String inicio, String fin) {
        this.thumbnailUrl = thumbnailUrl;
        this.Direccion = Direccion;
        this.NombreAgente = NombreAgente;
        this.Id= Id;
        this.RazonSocial = RazonSocial;
        this.cta_cte = cta_cte;
        this.idUser = idUser;
        this.status = status;
        this.inicio = inicio;
        this.fin = fin;

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

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getCta_cte() {
        return cta_cte;
    }

    public void setCta_cte(String cta_cte) {
        this.cta_cte = cta_cte;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
