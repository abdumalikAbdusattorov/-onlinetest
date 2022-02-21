package uz.pdp.apponlinetestserver.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.apponlinetestserver.entity.*;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.exception.ResourceNotFoundException;
import uz.pdp.apponlinetestserver.repository.*;
import uz.pdp.apponlinetestserver.service.TwilioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class TelegramBotService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeletingMessageRepository deletingMessageRepository;

    @Autowired
    BotOnlineTest botOnlineTest;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    TwilioService twilioService;

    @Autowired
    TestBlockRepositrory testBlockRepositrory;

    @Autowired
    DeletingMessageWithPhotoRepository deletingMessageWithPhotoRepository;

    @Autowired
    HistoryUserRepository historyUserRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    TestWithScoreRepository testWithScoreRepository;

    public SendMessage getContact(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton().setRequestContact(true);
        keyboardButton.setText("Jo'natish");
        keyboardRow1.add(keyboardButton);
        keyboard.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        sendMessage.setText("Raqamingizni jonating sizni ro'yxatga qo'shib qo'yamiz");
        return sendMessage;
    }

    public SendMessage getTest(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove()); // tegidegi buttonni ochiradi
        sendMessage.setChatId(update.getMessage().getChatId());
        //  String phoneNumber = getCheckPhoneNumber(update.getMessage().getContact().getPhoneNumber());
        Optional<User> optionalPhoneNumber = userRepository.findByPhoneNumber(update.getMessage().getContact().getPhoneNumber());
        if (optionalPhoneNumber.isPresent()) {
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("Test ishlash");
            keyboardRow1.add(keyboardButton);
            keyboardButton = new KeyboardButton();
            keyboardButton.setText("Meng testlarim");
            keyboardRow1.add(keyboardButton);
            keyboard.add(keyboardRow1);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText("Menu tanla");
        } else {
            User user = new User();
            user.setPhoneNumber(update.getMessage().getContact().getPhoneNumber());
            user.setTelegramChatId(update.getMessage().getFrom().getId());
            user.setBotState(BotState.SHARE_FIRST_NAME);
            sendMessage.setText("Ism kirting");
            userRepository.save(user);
        }

        return sendMessage;
    }

    public SendMessage getDarajaTablash(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        Optional<User> byTelegramChatId = userRepository.findByTelegramChatId(update.getMessage().getFrom().getId());
        if (byTelegramChatId.isPresent()) {
            User user = byTelegramChatId.get();
            user.setBotState(BotState.CHOOSE_LEVEL);
            userRepository.save(user);
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("Oson");
            keyboardRow1.add(keyboardButton);
            keyboardButton = new KeyboardButton();
            keyboardButton.setText("O'rta");
            keyboardRow1.add(keyboardButton);
            keyboardButton = new KeyboardButton();
            keyboardButton.setText("Qiyin");

            KeyboardRow keyboardRow11 = new KeyboardRow();
            KeyboardButton keyboardButton1 = new KeyboardButton();
            keyboardButton1.setText("Bosh menu");
            keyboardRow11.add(keyboardButton1);

            keyboardRow1.add(keyboardButton);
            keyboard.add(keyboardRow1);
            keyboard.add(keyboardRow11);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText("Daraja tanla");
        }
        return sendMessage;
    }

    public void sendToClient(SendMessage sendMessage) {
        try {

            List<DeletingMessage> messageList = deletingMessageRepository.findAllByChatId(Long.parseLong(sendMessage.getChatId()));
            for (DeletingMessage deletingMessage : messageList) {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(deletingMessage.getChatId());
                deleteMessage.setMessageId(deletingMessage.getMessageId());
                botOnlineTest.execute(deleteMessage);
                deletingMessageRepository.delete(deletingMessage);
            }
            Message message = botOnlineTest.execute(sendMessage);
            getFutureDeletingMessageData(message.getMessageId(), message.getChatId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void sendToClientWithPhoto(SendMessage sendMessage) {
        try {

            List<DeletingMessage> messageList = deletingMessageRepository.findAllByChatId(Long.parseLong(sendMessage.getChatId()));
            for (DeletingMessage deletingMessage : messageList) {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(deletingMessage.getChatId());
                deleteMessage.setMessageId(deletingMessage.getMessageId());
                botOnlineTest.execute(deleteMessage);
                deletingMessageRepository.delete(deletingMessage);
            }
            List<DeletingMessageWithPhoto> messageWithPhotoList = deletingMessageWithPhotoRepository.findAllByChatId(Long.parseLong(sendMessage.getChatId()));
            for (DeletingMessageWithPhoto deletingMessage : messageWithPhotoList) {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(deletingMessage.getChatId());
                deleteMessage.setMessageId(deletingMessage.getMessageId());
                botOnlineTest.execute(deleteMessage);
                deletingMessageWithPhotoRepository.delete(deletingMessage);
            }
            Message message = botOnlineTest.execute(sendMessage);
            getFutureDeletingMessageData(message.getMessageId(), message.getChatId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void getFutureDeletingMessageData(Integer messageId, Long chatId) {
        deletingMessageRepository.save(new DeletingMessage(messageId, chatId));
    }

    public SendMessage getFirstName(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        Optional<User> byTelegramChatId = userRepository.findByTelegramChatId(update.getMessage().getFrom().getId());
        if (byTelegramChatId.isPresent()) {
            User user = byTelegramChatId.get();
            user.setFirstName(update.getMessage().getText());
            user.setBotState(BotState.SHARE_LAST_NAME);
            userRepository.save(user);
            sendMessage.setText("Familiya kiriting");
        }
        return sendMessage;
    }

    public SendMessage getLastName(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        Optional<User> byTelegramChatId = userRepository.findByTelegramChatId(update.getMessage().getFrom().getId());
        if (byTelegramChatId.isPresent()) {
            User user = byTelegramChatId.get();
            user.setLastName(update.getMessage().getText());
            user.setBotState(BotState.CHOOSE_PASSWORD);
            userRepository.save(user);
            sendMessage.setText("Parol kiriting");
        }
        return sendMessage;
    }

    public SendMessage getPassword(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        Optional<User> byTelegramChatId = userRepository.findByTelegramChatId(update.getMessage().getFrom().getId());
        if (byTelegramChatId.isPresent()) {
            User user = byTelegramChatId.get();
            user.setPassword(passwordEncoder.encode(update.getMessage().getText()));
            userRepository.save(user);
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("Test ishlash");
            keyboardRow1.add(keyboardButton);
            keyboardButton = new KeyboardButton();
            keyboardButton.setText("Meng testlarim");
            keyboardRow1.add(keyboardButton);
            keyboard.add(keyboardRow1);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

            sendMessage.setText("Menu tanla");
        }

        return sendMessage;
    }

    public SendMessage getBlokTanlash(Update update) {

        SendMessage sendMessage = new SendMessage().setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());

        Optional<User> optionalUser = userRepository.findByTelegramChatId(update.getCallbackQuery().getFrom().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setChoosenBlokId(Integer.parseInt(update.getCallbackQuery().getData().substring(8)));
            user.setBotState(BotState.CHOOSE_TEST_BLOCK);
            userRepository.save(user);

            List<TestBlock> testBlockList = testBlockRepositrory.findAllByBlockIdAndBlockLevel(user.getChoosenBlokId(), Level.valueOf(user.getChoosenLevel()));
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            int count = 0;
            for (TestBlock testBlock : testBlockList) {
                count++;
                rowInline.add(new InlineKeyboardButton()
                        .setText(testBlock.getBlock()
                                .getNameUz() + " " + testBlock.getBlock()
                                .getLevel()).setCallbackData("TestBlockId:" + testBlock.getId()));
                if (count % 3 == 0) {
                    rowsInline.add(rowInline);
                    rowInline = new ArrayList<>();
                }
            }
            if (count % 3 != 0) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
            }
            rowInline.add(new InlineKeyboardButton()
                    .setText("Orqaga qaytish")
                    .setCallbackData("back"));
            rowsInline.add(rowInline);
            markupInline.setKeyboard(rowsInline);
            sendMessage.setReplyMarkup(markupInline);
            if (testBlockList.size() > 0) {
                sendMessage.setText("TestBlok tanlang");
            } else {
                sendMessage.setText("Uzur yo'nalishda ma'lumot yo'q");
            }

        }
        return sendMessage;

    }

   /* public SendMessage getSubject(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Yuridichistki").setCallbackData("Yuridichistki:1"));
        rowInline.add(new InlineKeyboardButton().setText("Narxoz").setCallbackData("Narxoz"));
        rowsInline.add(rowInline);

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton().setText("Tatu").setCallbackData("Tatu"));
        rowInline1.add(new InlineKeyboardButton().setText("Inha").setCallbackData("Inha"));
        rowsInline.add(rowInline1);
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);
        sendMessage.setText("Yo'nalish tanlang");
        return sendMessage;
    }

    public SendMessage getTestIshlashniBoshlash(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("A. 1935").setCallbackData("A-1"));
        rowsInline.add(rowInline);

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton().setText("B. 1945").setCallbackData("B-1"));
        rowsInline.add(rowInline1);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("C. 1955").setCallbackData("C-1"));
        rowsInline.add(rowInline2);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(new InlineKeyboardButton().setText("D. 1941").setCallbackData("D-1"));
        rowsInline.add(rowInline3);

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);
        sendMessage.setText("Tarix \n1.Ikkinchi jahon urushi nechanchi yilda boshlangan?");
        return sendMessage;
    }

    public SendMessage getTestIshlashniBoshlashIkki(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("A. 1999").setCallbackData("A-2"));
        rowsInline.add(rowInline);

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton().setText("B. 1854").setCallbackData("B-2"));
        rowsInline.add(rowInline1);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("C. 1965").setCallbackData("C-3"));
        rowsInline.add(rowInline2);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(new InlineKeyboardButton().setText("D. 1962").setCallbackData("D-4"));
        rowsInline.add(rowInline3);

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);
        sendMessage.setText("Tarix \n2.Dunyo bo'ycha eng kuchli zilzila nechanchi yilda bolgan?");
        return sendMessage;

    }

    public SendMessage getTestIshlashniBoshlashUch(Update update) {
        SendMessage sendMessage = new SendMessage().setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());

        sendMessage.setText("Abbosov Meronshox sizning natijangiz\nFan: *" + "Tarix" + "* \nTo'g'ri = 1\nNoto'g'ri = 2\nUmumiy natijangiz *" + "80" + "* bal");
        return sendMessage;
    }*/

    public SendMessage getLevel(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        sendMessage.setChatId(update.getMessage().getChatId());
        Optional<User> byTelegramChatId = userRepository.findByTelegramChatId(update.getMessage().getFrom().getId());
        if (byTelegramChatId.isPresent()) {
            User user = byTelegramChatId.get();
            String text = update.getMessage().getText();
            if (text.equals("Oson")) {
                user.setChoosenLevel(Level.EASY.name());
            }
            if (text.equals("O'rta")) {
                user.setChoosenLevel(Level.MEDIUM.name());
            }
            if (text.equals("Qiyin")) {
                user.setChoosenLevel(Level.HARD.name());
            }
            user.setBotState(BotState.CHOOSE_BLOCK);
            userRepository.save(user);
            List<Block> blockList = blockRepository.findAllByLevel(Level.valueOf(user.getChoosenLevel()));
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            int count = 0;
            for (Block block : blockList) {
                count++;
                rowInline.add(new InlineKeyboardButton().setText(block.getNameUz()).setCallbackData("BlockId:" + block.getId()));
                if (count % 3 == 0) {
                    rowsInline.add(rowInline);
                    rowInline = new ArrayList<>();
                }

            }

            if (count % 3 != 0) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
            }
            rowInline.add(new InlineKeyboardButton()
                    .setText("Orqaga qaytish")
                    .setCallbackData("back"));
            rowsInline.add(rowInline);
            markupInline.setKeyboard(rowsInline);
            sendMessage.setReplyMarkup(markupInline);
            if (blockList.size() > 0) {
                sendMessage.setText("Yo'nalish tanlang");
            } else {
                sendMessage.setText("Uzur bunaqa darajada ma'lumot yoq");
            }
        }
        return sendMessage;
    }

    public SendMessage getMenu(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove()); // tegidegi buttonni ochiradi
        sendMessage.setChatId(update.getMessage().getChatId());

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Test ishlash");
        keyboardRow1.add(keyboardButton);
        keyboardButton = new KeyboardButton();
        keyboardButton.setText("Meng testlarim");
        keyboardRow1.add(keyboardButton);
        keyboard.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("Menu tanla");
        return sendMessage;
    }

    public SendMessage getBlack(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        Optional<User> optionalUser = userRepository.findByTelegramChatId(update.getCallbackQuery().getFrom().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getBotState().equals(BotState.CHOOSE_BLOCK)) {
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setSelective(true);
                replyKeyboardMarkup.setResizeKeyboard(true);
                List<KeyboardRow> keyboard = new ArrayList<>();
                KeyboardRow keyboardRow1 = new KeyboardRow();
                KeyboardButton keyboardButton = new KeyboardButton();
                keyboardButton.setText("Oson");
                keyboardRow1.add(keyboardButton);
                keyboardButton = new KeyboardButton();
                keyboardButton.setText("O'rta");
                keyboardRow1.add(keyboardButton);
                keyboardButton = new KeyboardButton();
                keyboardButton.setText("Qiyin");

                KeyboardRow keyboardRow11 = new KeyboardRow();
                KeyboardButton keyboardButton1 = new KeyboardButton();
                keyboardButton1.setText("Bosh menu");
                keyboardRow11.add(keyboardButton1);

                keyboardRow1.add(keyboardButton);
                keyboard.add(keyboardRow1);
                keyboard.add(keyboardRow11);
                replyKeyboardMarkup.setKeyboard(keyboard);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("Daraja tanla");
                user.setBotState(BotState.CHOOSE_LEVEL);
                userRepository.save(user);
            }
            if (user.getBotState().equals(BotState.CHOOSE_TEST_BLOCK)) {
                List<Block> blockList = blockRepository.findAllByLevel(Level.valueOf(user.getChoosenLevel()));
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                int count = 0;
                for (Block block : blockList) {
                    count++;
                    rowInline.add(new InlineKeyboardButton().setText(block.getNameUz()).setCallbackData("BlockId:" + block.getId()));
                    if (count % 3 == 0) {
                        rowsInline.add(rowInline);
                        rowInline = new ArrayList<>();
                    }
                }
                if (count % 3 != 0) {
                    rowsInline.add(rowInline);
                    rowInline = new ArrayList<>();
                }
                rowInline.add(new InlineKeyboardButton()
                        .setText("Orqaga qaytish")
                        .setCallbackData("back"));
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                sendMessage.setReplyMarkup(markupInline);
                sendMessage.setText("Yo'nalish tanlang");
                user.setBotState(BotState.CHOOSE_BLOCK);
            }
            userRepository.save(user);
        }
        return sendMessage;
    }

    public SendMessage getTestBlock(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());

        Optional<User> optionalUser = userRepository.findByTelegramChatId(update.getCallbackQuery().getFrom().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UUID testBlockId = UUID.fromString(update.getCallbackQuery().getData().substring(12));
            user.setChoosenTestBlokId(testBlockId);
            user.setTestStep(0);
            user.setQuestionStep(0);
            userRepository.save(user);

            Optional<TestBlock> optionalTestBlock = testBlockRepositrory.findById(testBlockId);
            if (optionalTestBlock.isPresent()) {
                TestBlock testBlock = optionalTestBlock.get();
                HistoryUser historyUser = new HistoryUser();
                historyUser.setUser(user);
                historyUser.setTestBlock(testBlock);
                historyUser.setFromBot(true);
                historyUserRepository.save(historyUser);
                List<TestWithScore> testWithScores = testBlock.getTestWithScores();
                List<Question> questionList = testWithScores.get(user.getTestStep()).getTest().getQuestions();
                Question question = questionList.get(user.getQuestionStep());
                sendMessage.setText(question.getQuestion());
                List<Answer> answerList = question.getAnswers();

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                int count = 0;
                for (Answer answer : answerList) {
                    count++;
                    rowInline.add(new InlineKeyboardButton()
                            .setText(answer.getAnswer())
                            .setCallbackData("AnswerId:" + answer.getId()));
                    if (count % 2 == 0) {
                        rowsInline.add(rowInline);
                        rowInline = new ArrayList<>();
                    }

                }

                if (count % 2 != 0) {
                    rowsInline.add(rowInline);
                 //   rowInline = new ArrayList<>();
                }
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                sendMessage.setReplyMarkup(markupInline);

            }


        }
        return sendMessage;
    }

    public SendMessage getAnswer(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        Optional<User> optionalUser = userRepository.findByTelegramChatId(update.getCallbackQuery().getFrom().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<HistoryUser> optionalHistoryUser = historyUserRepository.findByUserAndTestBlockIdAndFinished(user, user.getChoosenTestBlokId(), false);
            if (optionalHistoryUser.isPresent()) {
                HistoryUser historyUser = optionalHistoryUser.get();
                List<TestWithScore> testWithScores = historyUser.getTestBlock().getTestWithScores();
                List<Question> questionsList = testWithScores.get(user.getTestStep()).getTest().getQuestions();
                UUID answerId = UUID.fromString(update.getCallbackQuery().getData().substring(9));
                List<Answer> answers = historyUser.getAnswers();
                answers.add(answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId)));
                historyUser.setAnswers(answers);
                HistoryUser savedHistoryUser = historyUserRepository.save(historyUser);
                user.setQuestionStep(user.getQuestionStep() + 1);
                if (questionsList.size() >= (user.getQuestionStep() + 1)) {
                    Question question = questionsList.get(user.getQuestionStep());
                    sendMessage.setText(question.getQuestion());
                    List<Answer> answerList = question.getAnswers();
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    int count = 0;
                    for (Answer answer : answerList) {
                        count++;
                        rowInline.add(new InlineKeyboardButton()
                                .setText(answer.getAnswer())
                                .setCallbackData("AnswerId:" + answer.getId()));
                        if (count % 2 == 0) {
                            rowsInline.add(rowInline);
                           rowInline = new ArrayList<>();
                        }

                    }

                    if (count % 2 != 0) {
                        rowsInline.add(rowInline);
                      //  rowInline = new ArrayList<>();
                    }
                    rowsInline.add(rowInline);
                    markupInline.setKeyboard(rowsInline);
                    sendMessage.setReplyMarkup(markupInline);


                } else {
                    user.setTestStep(user.getTestStep() + 1);
                    user.setQuestionStep(0);
                    if (testWithScores.size() >= (user.getTestStep() + 1)) {
                        List<Question> questionList = testWithScores.get(user.getTestStep()).getTest().getQuestions();
                        Question question = questionList.get(user.getQuestionStep());
                        sendMessage.setText(question.getQuestion());
                        List<Answer> answerList = question.getAnswers();
                        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                        List<InlineKeyboardButton> rowInline = new ArrayList<>();
                        int count = 0;
                        for (Answer answer : answerList) {
                            count++;
                            rowInline.add(new InlineKeyboardButton()
                                    .setText(answer.getAnswer())
                                    .setCallbackData("AnswerId:" + answer.getId()));
                            if (count % 2 == 0) {
                                rowsInline.add(rowInline);
                                rowInline = new ArrayList<>();
                            }

                        }

                        if (count % 2 != 0) {
                            rowsInline.add(rowInline);
                         //   rowInline = new ArrayList<>();
                        }
                        rowsInline.add(rowInline);
                        markupInline.setKeyboard(rowsInline);
                        sendMessage.setReplyMarkup(markupInline);
                    } else {
                        double totalScore = 0;
                        double maxScore = 0;
                        List<Answer> answerList = savedHistoryUser.getAnswers();
                        for (Answer answer : answerList) {
                            if (answer.isCorrectAnswer()) {
                                totalScore += testWithScoreRepository.getScoreByTestBlockAndAnswer(savedHistoryUser.getTestBlock().getId(), answer.getId());
                            }
                            maxScore += testWithScoreRepository.getScoreByTestBlockAndAnswer(savedHistoryUser.getTestBlock().getId(), answer.getId());
                        }
                        savedHistoryUser.setTotalScore(totalScore);
                        savedHistoryUser.setMaxScore(maxScore);
                        savedHistoryUser.setFinished(true);
                        historyUserRepository.save(savedHistoryUser);
                        sendMessage.setText("Sizning toplagan balingiz: " + savedHistoryUser.getTotalScore() + "\n Maxsimum: " + savedHistoryUser.getMaxScore());
                        user.setTestStep(0);
                        user.setQuestionStep(0);
                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                        replyKeyboardMarkup.setSelective(true);
                        replyKeyboardMarkup.setResizeKeyboard(true);
                        List<KeyboardRow> keyboard = new ArrayList<>();
                        KeyboardRow keyboardRow11 = new KeyboardRow();
                        KeyboardButton keyboardButton1 = new KeyboardButton();
                        keyboardButton1.setText("Bosh menu");
                        keyboardRow11.add(keyboardButton1);
                        keyboard.add(keyboardRow11);
                        replyKeyboardMarkup.setKeyboard(keyboard);
                        sendMessage.setReplyMarkup(replyKeyboardMarkup);
                    }
                }
            }
            userRepository.save(user);
        }
        return sendMessage;
    }

    public SendMessage getHistoryTest(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow11 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setText("Bosh menu");
        keyboardRow11.add(keyboardButton1);
        keyboard.add(keyboardRow11);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        Optional<User> optionalUser = userRepository.findByTelegramChatId(update.getMessage().getFrom().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<HistoryUser> historyUserList = historyUserRepository.findAllByUser(user);
            String str = "";
            for (HistoryUser historyUser : historyUserList) {
                str += String.valueOf(historyUser.getCreatedAt()).substring(0, 16) + " " + historyUser.getMaxScore() + " balldan " + historyUser.getTotalScore() + " ball topladingiz \n";
            }
            sendMessage.setText(str);
        }
        return sendMessage;
    }
}