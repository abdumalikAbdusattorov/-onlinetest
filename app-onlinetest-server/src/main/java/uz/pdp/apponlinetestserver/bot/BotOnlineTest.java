package uz.pdp.apponlinetestserver.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.repository.UserRepository;

import java.util.Optional;


@Component
public class BotOnlineTest extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUserName;

    @Autowired
    TelegramBotService telegramBotService;

    @Autowired
    UserRepository userRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasContact()) {
                telegramBotService.getFutureDeletingMessageData(update.getMessage().getMessageId(), update.getMessage().getChatId());
                telegramBotService.sendToClient(telegramBotService.getTest(update));
            } else if (update.getMessage().hasText()) {
                if (update.getMessage().getText().equals("/start")) {
                    telegramBotService.sendToClient(telegramBotService.getContact(update));
                } else if (update.getMessage().getText().equals("Test ishlash")) {
                    telegramBotService.getFutureDeletingMessageData(update.getMessage().getMessageId(), update.getMessage().getChatId());
                    telegramBotService.sendToClient(telegramBotService.getDarajaTablash(update));
                }else if (update.getMessage().getText().equals("Meng testlarim")) {
                    telegramBotService.getFutureDeletingMessageData(update.getMessage().getMessageId(), update.getMessage().getChatId());
                    telegramBotService.sendToClient(telegramBotService.getHistoryTest(update));
                }else if (update.getMessage().getText().equals("Bosh menu")) {
                    telegramBotService.getFutureDeletingMessageData(update.getMessage().getMessageId(), update.getMessage().getChatId());
                    telegramBotService.sendToClient(telegramBotService.getMenu(update));
                }  else {
                    telegramBotService.getFutureDeletingMessageData(update.getMessage().getMessageId(), update.getMessage().getChatId());
                    Optional<User> byTelegramChatId = userRepository.findByTelegramChatId(update.getMessage().getFrom().getId());
                    if (byTelegramChatId.isPresent()) {
                        User user = byTelegramChatId.get();
                        if (user.getBotState().equals(BotState.SHARE_FIRST_NAME)) {
                            telegramBotService.sendToClient(telegramBotService.getFirstName(update));
                        }
                        if (user.getBotState().equals(BotState.SHARE_LAST_NAME)) {
                            telegramBotService.sendToClient(telegramBotService.getLastName(update));
                        }
                        if (user.getBotState().equals(BotState.CHOOSE_PASSWORD)) {
                            telegramBotService.sendToClient(telegramBotService.getPassword(update));
                        }
                        if (user.getBotState().equals(BotState.CHOOSE_LEVEL)) {
                            telegramBotService.sendToClient(telegramBotService.getLevel(update));
                        }
                    }
                }
            }
        } else if (update.getCallbackQuery() != null) {
            if (update.getCallbackQuery().getData().startsWith("BlockId:")) {
                telegramBotService.sendToClient(telegramBotService.getBlokTanlash(update));
            }
            if (update.getCallbackQuery().getData().startsWith("TestBlockId:")) {
                telegramBotService.sendToClient(telegramBotService.getTestBlock(update));
            }if (update.getCallbackQuery().getData().startsWith("AnswerId:")) {
                telegramBotService.sendToClient(telegramBotService.getAnswer(update));
            }
            if (update.getCallbackQuery().getData().startsWith("back")) {
                telegramBotService.sendToClientWithPhoto(telegramBotService.getBlack(update));
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
