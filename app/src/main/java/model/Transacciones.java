package model;

/**
 * Created by user on 09/02/2015.
 */
public class Transacciones {

    private String Id, Reclamo, Comentario, desc_mes_1, desc_mes_2, desc_mes_3, desc_mes_4, desc_mes_5, desc_mes_6 ;
    private int  trxs_mes_1, trxs_mes_2, trxs_mes_3, trxs_mes_4, trxs_mes_5, trxs_mes_6, trxs_lun, trxs_mar, trxs_mie, trxs_jue, trxs_vie, trxs_sab, trxs_dom;
    //private ArrayList<String> genre;

    public Transacciones() {
    }

    public Transacciones(String Id, String desc_mes_1, String desc_mes_2, String desc_mes_3,
                         String desc_mes_4, String desc_mes_5, String desc_mes_6, int trxs_mes_1, int trxs_mes_2,
                         int trxs_mes_3, int trxs_mes_4, int trxs_mes_5, int trxs_mes_6, int trxs_lun, int trxs_mar,
                         int trxs_mie, int trxs_jue, int trxs_vie, int trxs_sab, int trxs_dom) {
        this.Id= Id;
        this.desc_mes_1 = desc_mes_1 ;
        this.desc_mes_2 = desc_mes_2 ;
        this.desc_mes_3 = desc_mes_3 ;
        this.desc_mes_4 = desc_mes_4 ;
        this.desc_mes_5 = desc_mes_5 ;
        this.desc_mes_6 = desc_mes_6 ;
        this.trxs_mes_1 = trxs_mes_1 ;
        this.trxs_mes_2 = trxs_mes_2 ;
        this.trxs_mes_3 = trxs_mes_3 ;
        this.trxs_mes_4 = trxs_mes_4 ;
        this.trxs_mes_5 = trxs_mes_5 ;
        this.trxs_mes_6 = trxs_mes_6 ;
        this.trxs_lun = trxs_lun ;
        this.trxs_mar = trxs_mar ;
        this.trxs_mie = trxs_mie ;
        this.trxs_jue = trxs_jue ;
        this.trxs_vie = trxs_vie ;
        this.trxs_sab = trxs_sab ;
        this.trxs_dom = trxs_dom ;

    }
    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getdesc_mes_1() {
        return desc_mes_1;
    }

    public void setdesc_mes_1(String desc_mes_1) {
        this.desc_mes_1 = desc_mes_1;
    }

    public String getdesc_mes_2() {
        return desc_mes_2;
    }

    public void setdesc_mes_2(String desc_mes_2) {
        this.desc_mes_2 = desc_mes_2;
    }


    public String getdesc_mes_3() {
        return desc_mes_3;
    }

    public void setdesc_mes_3(String desc_mes_3) {
        this.desc_mes_3 = desc_mes_3;
    }

    public String getdesc_mes_4() {
        return desc_mes_4;
    }

    public void setdesc_mes_4(String desc_mes_4) {
        this.desc_mes_4 = desc_mes_4;
    }

    public String getdesc_mes_5() {
        return desc_mes_5;
    }

    public void setdesc_mes_5(String desc_mes_5) {
        this.desc_mes_5 = desc_mes_5;
    }

    public String getdesc_mes_6() {
        return desc_mes_6;
    }

    public void setdesc_mes_6(String desc_mes_6) {
        this.desc_mes_6 = desc_mes_6;
    }

    public int gettrxs_mes_1() {
        return trxs_mes_1;
    }

    public void settrxs_mes_1(int trxs_mes_1) {
        this.trxs_mes_1 = trxs_mes_1;
    }

    public int gettrxs_mes_2() {
        return trxs_mes_2;
    }

    public void settrxs_mes_2(int trxs_mes_2) {
        this.trxs_mes_2 = trxs_mes_2;
    }

    public int gettrxs_mes_3() {
        return trxs_mes_3;
    }

    public void settrxs_mes_3(int trxs_mes_3) {
        this.trxs_mes_3 = trxs_mes_3;
    }

    public int gettrxs_mes_4() {
        return trxs_mes_4;
    }

    public void settrxs_mes_4(int trxs_mes_4) {
        this.trxs_mes_4 = trxs_mes_4;
    }

    public int gettrxs_mes_5() {
        return trxs_mes_5;
    }

    public void settrxs_mes_5(int trxs_mes_5) {
        this.trxs_mes_5 = trxs_mes_5;
    }

    public int gettrxs_mes_6() {
        return trxs_mes_6;
    }

    public void settrxs_mes_6(int trxs_mes_6) {
        this.trxs_mes_6 = trxs_mes_6;
    }

    public int gettrxs_lun() {
        return trxs_lun;
    }

    public void settrxs_lun(int trxs_lun) {
        this.trxs_lun = trxs_lun;
    }

    public int gettrxs_mar() {
        return trxs_mar;
    }

    public void settrxs_mar(int trxs_mar) {
        this.trxs_mar = trxs_mar;
    }

    public int gettrxs_mie() {
        return trxs_mie;
    }

    public void settrxs_mie(int trxs_mie) {
        this.trxs_mie = trxs_mie;
    }

    public int gettrxs_jue() {
        return trxs_jue;
    }

    public void settrxs_jue(int trxs_jue) {
        this.trxs_jue = trxs_jue;
    }

    public int gettrxs_vie() {
        return trxs_vie;
    }

    public void settrxs_vie(int trxs_vie) {
        this.trxs_vie = trxs_vie;
    }

    public int gettrxs_sab() {
        return trxs_sab;
    }

    public void settrxs_sab(int trxs_sab) {
        this.trxs_sab = trxs_sab;
    }

    public int gettrxs_dom() {
        return trxs_dom;
    }

    public void settrxs_dom(int trxs_dom) {
        this.trxs_dom = trxs_dom;
    }


}
