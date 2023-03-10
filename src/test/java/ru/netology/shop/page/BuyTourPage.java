package ru.netology.shop.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.netology.shop.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.shop.page.ElementsTexts.Notifications;
import static ru.netology.shop.page.ElementsTexts.Buttons;

public class BuyTourPage {
    final SelenideElement heading = $("div[id=root] > div > h3.heading");
    private final SelenideElement form = $("form fieldset");
    // *** Селекторы элементов формы ***
    private final SelenideElement numberField = findFieldByText("Номер карты");
    private final SelenideElement monthField = findFieldByText("Месяц");
    private final SelenideElement yearField = findFieldByText("Год");
    private final SelenideElement holderField = findFieldByText("Владелец");
    private final SelenideElement codeField = findFieldByText("CVC/CVV");
    private final SelenideElement continueButton = form.$("button.button");
    // *** Селекторы всплывающих сообщений ***
    private final SelenideElement notificationSuccess = $(".notification_status_ok");
    private final SelenideElement notificationError = $(".notification_status_error");

    private SelenideElement findFieldByText(String fieldName) {
        return form.$x(".//span[@class='input__top'][contains(text(),'" + fieldName + "')]//..");
    }

    // *** Заполнение полей ***
    @Step("Вводим номер карты: {0}")
    public void inputNumber(String cardNumber) {
        numberField.$("input").val(cardNumber);
    }

    @Step("Вводим месяц: {0}")
    public void inputMonth(String month) {
        monthField.$("input").val(month);
    }

    @Step("Вводим год: {0}")
    public void inputYear(String year) {
        yearField.$("input").val(year);
    }

    @Step("Вводим имя и фамилию владельца: {0}")
    public void inputHolder(String holder) {
        holderField.$("input").val(holder);
    }

    @Step("Вводим CVV-код: {0}")
    public void inputCode(String code) {
        codeField.$("input").setValue(code);
    }

    @Step("Нажимаем на кнопку «Продолжить»")
    public void clickContinueButton() {
        continueButton.shouldHave(text(Buttons.submit), Duration.ofMillis(30)).click();
    }

    public void inputCardInfoAndClickContinue(DataGenerator.CardInfo cardInfo) {
        this.inputNumber(cardInfo.getNumber());
        this.inputMonth(cardInfo.getMonth());
        this.inputYear(cardInfo.getYear());
        this.inputHolder(cardInfo.getHolder());
        this.inputCode(cardInfo.getCvc());
        this.clickContinueButton();
    }

    // *** Проверки состояния кнопки подтверждения ***
    @Step("Проверяем изменение текста кнопки и появление иконки загрузки")
    public void checkButtonIsLoading() {
        continueButton.shouldHave(cssClass("button_disabled"), Duration.ofMillis(30));
        continueButton.$(".button__text").shouldHave(text(Buttons.loading), Duration.ofMillis(30));
        continueButton.$(".button__text .spin").shouldBe(visible, Duration.ofMillis(30));
    }

    @Step("Проверяем неизменность текста кнопки и отсутствие иконки загрузки")
    public void checkButtonIsNormal() {
        continueButton.shouldNotHave(cssClass("button_disabled"), Duration.ofMillis(30));
        continueButton.$(".button__text").shouldHave(text(Buttons.submit), Duration.ofMillis(30));
        continueButton.$(".button__text .spin").shouldNotBe(visible, Duration.ofMillis(30));
    }

    // *** Проверки сообщений о результате ***
    @Step("Проверяем появилось ли сообщение с результатом «{title}»")
    public void checkNotification(SelenideElement notification, int waitingTimeInSeconds, String title, String content) {
        notification.shouldBe(visible, Duration.ofSeconds(waitingTimeInSeconds));
        notification.$(".notification__title").shouldHave(text(title));
        notification.$(".notification__content").shouldHave(text(content));
    }

    public void checkSuccessNotification(int waitingTimeInSeconds) {
        checkNotification(notificationSuccess, waitingTimeInSeconds, Notifications.success[0], Notifications.success[1]);
    }

    public void checkErrorNotification(int waitingTimeInSeconds) {
        checkNotification(notificationError, waitingTimeInSeconds, Notifications.error[0], Notifications.error[1]);
    }

    // *** Проверки ошибок заполнения полей ***
    @Step("Проверяем, что под полем появляется ошибка «{1}»")
    private void checkFieldError(SelenideElement field, String errorText) {
        field.parent().shouldHave(cssClass("input_invalid"), Duration.ofMillis(10));
        field.$(".input__sub")
                .shouldBe(visible, Duration.ofMillis(10))
                .shouldHave(text(errorText), Duration.ofMillis(10));
    }

    public void checkNumberFieldError(String errorText) {
        checkFieldError(numberField, errorText);
    }

    public void checkMonthFieldError(String errorText) {
        checkFieldError(monthField, errorText);
    }

    public void checkYearFieldError(String errorText) {
        checkFieldError(yearField, errorText);
    }

    public void checkHolderFieldError(String errorText) {
        checkFieldError(holderField, errorText);
    }

    public void checkCodeFieldError(String errorText) {
        checkFieldError(codeField, errorText);
    }

    // *** Проверка допустимой длины полей ***
    @Step("Получаем допустимую длину поля")
    private int getFieldMaxLength(SelenideElement field) {
        var maxLength = field.$("input.input__control")
                .shouldHave(attribute("maxlength"), Duration.ofMillis(10))
                .getAttribute("maxlength");
        return Integer.parseInt(maxLength);
    }

    public int getNumberFieldMaxLength() {
        return getFieldMaxLength(numberField);
    }

    public int getMonthFieldMaxLength() {
        return getFieldMaxLength(monthField);
    }

    public int getYearFieldMaxLength() {
        return getFieldMaxLength(yearField);
    }

    public int getHolderFieldMaxLength() {
        return getFieldMaxLength(holderField);
    }

    public int getCodeFieldMaxLength() {
        return getFieldMaxLength(codeField);
    }

    // *** Проверка отображения плейсхолдера ***
    @Step("Проверяем, появляется ли плейсхолдер в пустом поле и получаем его значение")
    private String getFieldPlaceholder(SelenideElement field) {
        field.parent().shouldHave(cssClass("input_focused"), Duration.ofMillis(20));
        return field.$("input.input__control")
                .shouldBe(visible, Duration.ofMillis(20))
                .getAttribute("placeholder");
    }

    public String getNumberFieldPlaceholder() {
        return getFieldPlaceholder(numberField);
    }

    public String getMonthFieldPlaceholder() {
        return getFieldPlaceholder(monthField);
    }

    public String getYearFieldPlaceholder() {
        return getFieldPlaceholder(yearField);
    }

    public String getHolderFieldPlaceholder() {
        return getFieldPlaceholder(holderField);
    }

    public String getCodeFieldPlaceholder() {
        return getFieldPlaceholder(codeField);
    }
}
