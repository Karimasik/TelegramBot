import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Получаем информацию о фильме
 */
public class Film {
    private Document document; //документ в котором будет хранится страница
    private String url = "";

    public void setUrl(String url) {
        this.url = url;
    }

//    public Film() {
//        connect();
//    }

    public void connect() { //подключаемся к странице
        try {
            document = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 OPR/77.0.4054.80").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() { //получаем название фильма
//         String title = document.title();
//         return title;
        Elements title = document.getElementsByClass("styles_title__2l0HH");
        return title.text();
    }

    public String getCast() { //возвращаем актерский состав
        Elements cast = document.getElementsByClass("styles_actors__2zt1j").first().getElementsByClass("styles_list__I97eu");
        return cast.text();
    }

    public String getGenres() { //вытаскиваем жанры
        Elements genres = document.getElementsByClass("styles_valueLight__3Gl7S styles_value__2F1uj styles_root__XK7Dx")
                .first().getElementsByClass("styles_valueLight__3Gl7S styles_value__2F1uj");
        return genres.text();
    }

    public String getDescription() { //читаем описание фильма
        try {
            Elements description = document.getElementsByClass("styles_paragraph__2Otvx");
            return description.text();
        } catch (NullPointerException e) {
            System.out.println("Описание не найдено!");
        }
        return null;
    }

    public String getRating() { //смотрим рейтинг фильма
        try {
            Elements rating = document.getElementsByClass("film-rating-value styles_rootPositive__ac3xv styles_rootLink__1CSPc");
            return rating.text();
        } catch (NullPointerException e) {
            System.out.println("Описание не найдено!");
        }
        return null;
    }

    public String getMovieLink() { //даем ссылку на фильм
        return url;
    }
}