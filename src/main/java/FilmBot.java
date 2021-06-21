import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;

public class FilmBot extends TelegramLongPollingBot {
    //Данные для подключения к боту
    private long chat_id; //берет id чата
    private static final String BOT_TOKEN = "1853904972:AAFZMlClsjFyMhs6_f9maIOkM3-l56BRbHA";
    private static final String BOT_NAME = "MovieSearchBot";

    //создание объекта класса Film
    Film film = new Film();

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); //создаем клавиатуру

    /**
     *
     * @param update входящее сообщение. Прикрепим к методу нашу клавиатуру
     */
    @Override
    public void onUpdateReceived(Update update) {
        update.getUpdateId(); //обновляем информацию о пользователе
        chat_id = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage().setChatId(chat_id);
        /**
         * SendMessage - класс для отправки сообщений
         * setChatId - выставляет идентификатор(id) человека, который написал боту
         * update.getMessage().getChatId() - идентификатор(id) того же человека
         */

        String text = update.getMessage().getText(); //принимаем текст входящего сообщения от пользователя
        sendMessage.setReplyMarkup(replyKeyboardMarkup); //устанавливаем клавиатуру для пользователя

        try {
            sendMessage.setText(getMessage(text));
            execute(sendMessage); //отправка сообщения
        } catch (TelegramApiException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String msg) throws SQLException, ClassNotFoundException {
        DatabaseFilms df = new DatabaseFilms(); //соединение с БД

        List<KeyboardRow> keyboard = new ArrayList<>(); //список кнопок

        //создаем несколько кнопок в ряд с помощью KeyboardRow
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        //корректируем некоторые параметры клавиатуры
        replyKeyboardMarkup.setSelective(true);
        /**
         * этот параметр нужен, чтобы показывать клавиатуру только определнным пользователям.
         */
        replyKeyboardMarkup.setResizeKeyboard(true);
        /**
         * указывает клиенту подогнать высоту клавиатуры под кол-во кнопок
         */
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        /**
         * указывает клиенту скрыть клавиатуру после использования (после нажатия на кнопку)
         */

        //создадим условие для перехода в меню. Если пользователь напишет Привет, то ему отобразится кнопка:
        //1)ТОП-250 по версии IMDb
        if(msg.equals("Hello") || msg.equals("Hi") || msg.equals("Привет")) {
            keyboard.clear();
            keyboardFirstRow.clear(); //чистим клавиатуру
            keyboardFirstRow.add("ТОП-250 по версии IMDb"); //добавляем кнопку
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard); //обновляем клавиатуру
            return "Привет любитель кино\uD83D\uDCFA Скажи мне название фильма, который хочешь посмотреть и я найду его тебе\uD83D\uDD0E Либо, " +
                    "если ты в растерянности, нажми на кнопку \"ТОП-250 по версии IMDb\" и наслаждайся лучшими фильмами\uD83D\uDE0E\uD83D\uDC4D\uD83C\uDFFD";

        } else if(df.findFilm(msg)) {
            film.setUrl(df.addLink(msg));
            film.connect();
            return getInfoFilm();

        } else if(msg.equals("ТОП-250 по версии IMDb")) {
            return "https://www.kinopoisk.ru/lists/top250/";

        } else {
            return "Не слышал об этом\uD83D\uDE31 Дай мне название фильма\uD83E\uDD28 или поищи тут https://www.kinopoisk.ru";
        }
    }


    public void connectDatabase() throws ClassNotFoundException, SQLException {
        //коннектимся c БД
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
//            Statement statement = connection.createStatement()) {
//            ResultSet resultSet = statement.executeQuery("SELECT Title FROM Movie");
//            while(resultSet.next()) {
//                title.add(resultSet.getString("Title"));
//            }
//        } catch(Exception e) {
//            System.out.println("Connection failed");
//            e.printStackTrace();
//        }

    }

    public String getInfoFilm() {

        String info = film.getTitle()
                + "\nАктерский состав: " + film.getCast()
                + "\nЖанр: " + film.getGenres()
                + "\nОписание: " + film.getDescription()
                + "\nРейтинг: " + film.getRating()
                + "\nСсылка на фильм: " + film.getMovieLink();
        return info;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
