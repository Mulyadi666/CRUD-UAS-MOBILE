package uday.tech;

public class ItemList {
    private String id;
    private String judul;
    private String deskripsi;
    private String imgUrl;


    public ItemList(String judul, String deskripsi, String imgUrl) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
