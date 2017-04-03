import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class FirstTry extends TelegramLongPollingBot {
	private String firstBottom = "Новая задача";
	private String secondBottom = "Показать список задач";
	private String thirdBottom = "Да";
	private String fourthBottom = "Нет";
	private String path;
	private int flag;
	private boolean flag2 = false;
	private ToDoObject object = new ToDoObject();
	
	
	@Override
	public void onUpdateReceived(Update update) {
		String reply = null;
    	String infoNew = null;
    	String msg = update.getMessage().getText();
    	
    	// We check if the update has a message and the message has text
    	if (update.getMessage().hasText()) {
		    if (update.hasMessage()) {
		    	if (msg.equals("/start")) {
		    		reply = "Привет! Я бот который поможет тебе собрать в кучу свои дела и просматривать весь список.\n Но пока меня только разрабатывают, так что можно переодически половить баги от создателя!))";
		    		sendFirsCustomKeyboard(reply, update.getMessage().getChatId().toString());
		    		flag = 0;
		    	}
		    	
		    	if (msg.equals("/test")) {
		    		
		    		flag = 0;
		    	}
		    }

	    	if (update.hasMessage() && (flag % 2) != 0) {
	    		infoNew = update.getMessage().getText();
	        	flag2 = true;
	        }
	    	
	    	if (update.hasMessage() && msg.equals(firstBottom) && flag == 0) {
	        	flag++;
	        	reply = "Введите название";
	        	sendMessage(reply, update.getMessage().getChatId().toString());
	        }
	    	
	    	if (update.hasMessage() && flag2 && !msg.equals(thirdBottom) && !msg.equals(fourthBottom)) {
	        	reply = "Правильно введено?\n" + infoNew;
		    	sendSecondCustomKeyboard(reply, update.getMessage().getChatId().toString());
	        }
	    	
	    	if (update.hasMessage() && msg.equals(thirdBottom) && flag2){
	    		flag++;
	    		if (flag == 2) object.name = infoNew;
	    		if (flag == 4) object.time = infoNew;
	    		if (flag == 6) object.description = infoNew;
	    		flag2 = false;
	    	} else if (update.hasMessage() && msg.equals(fourthBottom) && flag2) {
	    		flag--;
	    		flag2 = false;
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
	        
	        if (update.hasMessage() && msg.equals(secondBottom)) {
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
        row.add(firstBottom);
        row.add(secondBottom);
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
        row.add(thirdBottom);
        row.add(fourthBottom);
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
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
    			.setChatId(chatId)
    			.setText(sent);//setText(update.getMessage().getText());
        try {
            sendMessage(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}
	
	@Override
    public String getBotUsername() {
        return LoginBot.username;
    }
	
	@Override
    public String getBotToken() {
        return LoginBot.token;
    }
	
}