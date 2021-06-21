import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Play {
    public static void main(String[] args) {
        ApiContextInitializer.init(); //инициализация API Telegram
        TelegramBotsApi telegram = new TelegramBotsApi(); // класс для обращения к боту

        try {
            telegram.registerBot(new FilmBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
