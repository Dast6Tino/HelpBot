import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import res.LineСonstants;
import res.LoginBot;

public class FirstTry extends TelegramLongPollingBot {
    private String path;
    private static String reply = null;
    private String infoNew = null;

    private int flag;
    private boolean flag2 = false;
    private ToDoObject object = new ToDoObject();

    private static Matcher matcher;

    @Override
    public void onUpdateReceived(Update update) {
        String msg = update.getMessage().getText();

        if (update.getMessage().hasText()) {
            if (update.hasMessage()) {
                if (msg.equals("/start")) {
                    reply = "Привет! Я бот который поможет тебе собрать в кучу свои дела и просматривать весь список.\n Но пока меня только разрабатывают, так что можно переодически половить баги от создателя!))";
                    sendFirsCustomKeyboard(reply, update.getMessage().getChatId().toString());
                    flag = 0;
                    flag2 = false;
                }

                if (msg.equals("/test")) {
                    Pattern date = Pattern.compile("^(([0]?[1-9]|[1-2][0-9]|3[01])\\.([0]?[13578]|1[02]))|" +
                            "(([0]?[1-9]|[1-2][0-9]|30)\\.([0]?[469]|11))|" +
                            "(([0]?[1-9]|[1-2][0-9])\\.([0]?2))$");
                    matcher = date.matcher(update.getMessage().getText());
                    reply = String.valueOf(matcher.matches());
                    sendMessage(reply, update.getMessage().getChatId().toString());
                    flag = 0;
                    flag2 = false;
                }
            }

            if (update.hasMessage() && (flag % 2) != 0 && !msg.equals(LineСonstants.thirdBottom) && !msg.equals(LineСonstants.fourthBottom)) {
                infoNew = update.getMessage().getText();
                flag2 = true;
            }

            if (update.hasMessage() && msg.equals(LineСonstants.firstBottom) && flag == 0) {
                flag++;
                reply = "Введите название";
                sendMessage(reply, update.getMessage().getChatId().toString());
            }

            if (update.hasMessage() && flag2 && !msg.equals(LineСonstants.thirdBottom) && !msg.equals(LineСonstants.fourthBottom)) {
                reply = "Правильно введено?\n" + infoNew;
                sendSecondCustomKeyboard(reply, update.getMessage().getChatId().toString());
            }

            if (update.hasMessage() && msg.equals(LineСonstants.thirdBottom) && flag2){
                flag++;
                switch (flag) {
                    case 2: {
                        object.name = infoNew;
                        break;
                    }
                    case 4: {
                        Pattern date = Pattern.compile("^(([0]?[1-9]|[1-2][0-9]|3[01])\\.([0]?[13578]|1[02]))|" +
                                "(([0]?[1-9]|[1-2][0-9]|30)\\.([0]?[469]|11))|" +
                                "(([0]?[1-9]|[1-2][0-9])\\.([0]?2))$");
                        matcher = date.matcher(update.getMessage().getText());
                        if (matcher.matches()) {
                            object.time = infoNew;
                        } else {
                            reply = "Проверьте корректность введенных данных";
                            sendMessage(reply, update.getMessage().getChatId().toString());
                            flag--;
                        }
                        break;
                    }
                    case 6: {
                        object.description = infoNew;
                        break;
                    }
                    default:
                        flag--;
                        flag2 = false;
                }
            }

            if (update.hasMessage() && flag == 2) {
                reply = "Введите время(формат: дд.мм/ч:м)";
                sendMessage(reply, update.getMessage().getChatId().toString());
                flag++;
            }

            if (update.hasMessage() && flag == 4) {
                reply = "Введите описание:";
                sendMessage(reply, update.getMessage().getChatId().toString());
                flag++;
            }

            if (update.hasMessage() && flag == 6) {
                path = "Data\\" + update.getMessage().getChatId().toString() + "\\" + update.getMessage().getDate() + ".json";
                JsonWork.setJson(object, path);
                reply = "Готово!\n" + object.name + "\n" + object.time + "\n" + object.description;
                sendMessage(reply, update.getMessage().getChatId().toString());
                flag = 0;
            }

            if (update.hasMessage() && msg.equals(LineСonstants.secondBottom)) {
                reply = "ok_2";
                flag = 0;
            }

        } else {
            reply = "Я понимаю только текст.(";
            sendFirsCustomKeyboard(reply, update.getMessage().getChatId().toString());
        }
    }

    public void sendFirsCustomKeyboard(String sent, String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(sent);

        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add(LineСonstants.firstBottom);
        row.add(LineСonstants.secondBottom);
        // Add the first row to the keyboard
        keyboard.add(row);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);

        try {
            // Send the message
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendSecondCustomKeyboard(String sent, String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(sent);

        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add(LineСonstants.thirdBottom);
        row.add(LineСonstants.fourthBottom);
        // Add the first row to the keyboard
        keyboard.add(row);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);

        try {
            // Send the message
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String sent, String chatId) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(sent);
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return LoginBot.getUsername();
    }

    @Override
    public String getBotToken() {
        return LoginBot.getToken();
    }

}