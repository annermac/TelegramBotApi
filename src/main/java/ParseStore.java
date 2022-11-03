import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;

public class ParseStore {
    public static LinkedHashMap<Integer, Object> getParseStore(Store store) throws IOException {

        Connection.Response connect = Jsoup.connect(store.getUrlWebSite() + store.getUrlFilter()).execute();
        Document document = connect.parse();
        Elements htmlElements = document.select(store.getParentClass());
        LinkedHashMap<Integer, Object> listProducts = new LinkedHashMap<>();

        for (org.jsoup.nodes.Element htmlElement : htmlElements) {
            int productId = Integer.parseInt(htmlElement.select(store.getIdClass()).text().replaceAll("\\D+", ""));
            String title = htmlElement.select(store.getTitleClass()).text();
            int price = Integer.parseInt(htmlElement.select(store.getPriceClass()).text().replaceAll("\\D+", ""));
            String image = store.getUrlWebSite() + htmlElement.select(store.getImgClass()).attr("src");
            String link = store.getUrlWebSite() + htmlElement.select(store.getLinkClass()).attr("href");

            if (!store.getBlackList().contains(productId)) {
                listProducts.put(productId, new Product(productId, title, price, image, link));
            }

        }
        return listProducts;
    }
}
