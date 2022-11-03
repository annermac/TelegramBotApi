import java.util.ArrayList;

public class Store {

    private String nameStore;
    private String urlWebSite;
    private String urlFilter;
    private String parentClass;
    private String idClass;
    private String titleClass;
    private String priceClass;
    private String imgClass;
    private String linkClass;
    private ArrayList<Integer> blackList;

    public Store(String nameStore, String urlWebSite, String urlFilter, String parentClass, String idClass, String titleClass, String priceClass, String imgClass, String linkClass) {
        this.nameStore = nameStore;
        this.urlWebSite = urlWebSite;
        this.urlFilter = urlFilter;
        this.parentClass = parentClass;
        this.idClass = idClass;
        this.titleClass = titleClass;
        this.priceClass = priceClass;
        this.imgClass = imgClass;
        this.linkClass = linkClass;
        this.blackList = new ArrayList<>();
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public String getUrlWebSite() {
        return urlWebSite;
    }

    public void setUrlWebSite(String urlWebSite) {
        this.urlWebSite = urlWebSite;
    }

    public String getUrlFilter() {
        return urlFilter;
    }

    public void setUrlFilter(String urlFilter) {
        this.urlFilter = urlFilter;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String getTitleClass() {
        return titleClass;
    }

    public void setTitleClass(String titleClass) {
        this.titleClass = titleClass;
    }

    public String getPriceClass() {
        return priceClass;
    }

    public void setPriceClass(String priceClass) {
        this.priceClass = priceClass;
    }

    public String getImgClass() {
        return imgClass;
    }

    public void setImgClass(String imgClass) {
        this.imgClass = imgClass;
    }

    public String getLinkClass() {
        return linkClass;
    }

    public void setLinkClass(String linkClass) {
        this.linkClass = linkClass;
    }

    public ArrayList<Integer> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<Integer> blackList) {
        this.blackList = blackList;
    }

    public void addBlackList(int i) {
        this.blackList.add(i);
    }

}
