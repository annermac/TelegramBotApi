import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Ann Bez
 * @version 1.0
 */
public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    Store storeCitilink = new Store("Ситилинк", "https://www.citilink.ru/", "/catalog/monitory/?pf=discount.any%2Crating.any%2C2767_162%2C2772_162&f=discount.any%2Crating.any%2C2767_162%2C2772_162%2Cavailable.all&price_min=66065&price_max=408120&pprice_min=66065&pprice_max=408120", ".ProductGroupList .product_data__gtm-js", ".ProductCardHorizontal__vendor-code", ".ProductCardHorizontal__title", ".ProductCardHorizontal__price_current-price", ".ProductCardHorizontal__image", ".ProductCardHorizontal__title");
    Store storeRegard = new Store("Регард", "https://www.regard.ru", "/catalog/1035/monitory?q=eyJieVByaWNlIjp7Im1pbiI6NjYwMDAsIm1heCI6NTkyNTQwfX0", ".ListingRenderer_row__jqZol .Card_wrap__2fsLE", ".CardId_listing__1-Jny", ".CardText_title__FG-Zy", ".CardPrice_price__1t0QB", ".CardImageSlider_image__qSSu1", ".CardText_link__2H3AZ");

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(Message message, LinkedHashMap<Integer, Object> products, int currentPage, Store store) throws UnsupportedEncodingException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.enableHtml(true);
        sendMessage.setChatId(message.getChatId().toString());

        int countProduct = products.size();

        if (countProduct == 0) {
            sendMessage.setText("Больше товаров нет!");

            try {
                execute(sendMessage);
                return;
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> ButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> ButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        InlineKeyboardButton prevButton = new InlineKeyboardButton();
        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        InlineKeyboardButton blacklistButton = new InlineKeyboardButton();

        if (currentPage != 0) {
            nextButton.setText("← Назад");
            nextButton.setCallbackData("prev:" + (currentPage - 1) + ":" + store);
            ButtonsRow1.add(nextButton);
        }

        if (currentPage != (countProduct - 1)) {
            prevButton.setText("Вперед →");
            prevButton.setCallbackData("next:" + (currentPage + 1) + ":" + store);
            ButtonsRow1.add(prevButton);
        }

        keyboard.add(ButtonsRow1);

        ArrayList<Integer> listId = new ArrayList<>(products.keySet());

        blacklistButton.setText("Не показывать ❌");

        if (countProduct == 1) {
            blacklistButton.setCallbackData("blacklist:" + countProduct + ":" + store + ":" + listId.get(currentPage));
        } else if (currentPage != (countProduct - 1)) {
            blacklistButton.setCallbackData("blacklist:" + (currentPage + 1) + ":" + store + ":" + listId.get(currentPage));
        } else {
            blacklistButton.setCallbackData("blacklist:" + (currentPage) + ":" + store + ":" + listId.get(currentPage));
        }

        ButtonsRow2.add(blacklistButton);
        keyboard.add(ButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        ArrayList<Object> list = new ArrayList<>(products.values());

        sendMessage.setText("По вашему запросу найдено " + countProduct + " товаров. \r\n" + "Показана страница " + (currentPage + 1) + " из " + countProduct + ". \r\n\n" + list.get(currentPage).toString());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "citilink" -> {
                    try {
                        sendMsg(message, ParseStore.getParseStore(storeCitilink), 0, storeCitilink);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "regard" -> {
                    try {
                        sendMsg(message, ParseStore.getParseStore(storeRegard), 0, storeRegard);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> sendMsg(message, "Привет! Выберите магазин");
            }

        } else if (update.hasCallbackQuery()) {
            hundlerCallback(update.getCallbackQuery());
        }
    }

    public void hundlerCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] parameter = callbackQuery.getData().split(":");
        String action = parameter[0];
        String page = parameter[1];
        String storeParam = parameter[2];
        Store store = null;

        if (Objects.equals(storeParam, storeCitilink.toString())) {
            store = storeCitilink;
        } else if (Objects.equals(storeParam, storeRegard.toString())) {
            store = storeRegard;
        }

        if (Objects.equals(action, "blacklist")) {
            int productId = Integer.parseInt(parameter[3]);
            store.addBlackList(productId);
            try {
                if (Integer.parseInt(page) == 0) {
                    sendMsg(message, "Больше товаров нет!");
                } else {
                    sendMsg(message, ParseStore.getParseStore(store), Integer.parseInt(page) - 1, store);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                sendMsg(message, ParseStore.getParseStore(store), Integer.parseInt(page), store);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("citilink"));
        keyboardFirstRow.add(new KeyboardButton("regard"));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    @Override
    public String getBotUsername() {
        return "NameBot";
    }

    @Override
    public String getBotToken() {
        return "1234567890:AbCdF";
    }
}
