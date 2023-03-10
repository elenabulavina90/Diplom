package ru.netology.shop.test;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.shop.db.Order;

import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.shop.page.ElementsTexts.Errors;
import static ru.netology.shop.data.DataGenerator.*;

@DisplayName("Покупка «Путешествия дня» c оплатой по карте")
public class BuyTourPaymentPageTest extends BasePageTest {
    private int price;

    @BeforeEach
    public void getPrice() {
        setup();
        price = tourOfTheDayPage.getTourPrice();
        buyTourPage = tourOfTheDayPage.clickBuy();
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте с валидным реквизитами: {0}")
    @MethodSource("ru.netology.shop.data.TestData#validCardInfo")
    @Severity(value = SeverityLevel.BLOCKER)
    @TmsLinks({@TmsLink(value = "ui-01"), @TmsLink(value = "ui-03")})
    @Override
    public void shouldBuyTourIfValidCardInfo(String testName, CardInfo card) {
        var expectedOrder = new Order("APPROVED", price);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkSuccessNotification(15);
        var actualOrder = db.getPaymentOrder();
        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @DisplayName("Переход со страницы покупки тура на страницу покупки тура в кредит")
    @Test
    @Severity(value = SeverityLevel.MINOR)
    @TmsLink(value = "ui-05")
    @Override
    public void shouldChangePageIfClickButton() {
        tourOfTheDayPage.clickBuy();
        tourOfTheDayPage.clickCredit();
    }

    @DisplayName("Покупка тура по «DECLINED» карте с валидными реквизитами")
    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @TmsLink(value = "ui-07")
    @Issue(value = "2")
    @Override
    public void shouldShowErrorIfValidDeclinedCard() {
        var card = Cards.generateValidDeclinedCard();
        var expectedOrder = new Order("DECLINED", price);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkErrorNotification(15);
        var actualOrder = db.getPaymentOrder();
        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте со сроком действия {0}")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableMonth")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-09")
    @Issue(value = "11")
    @Override
    public void shouldShowErrorIfInputtedMonthIsNotAllowable(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkMonthFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте со сроком действия {0}")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableYear")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-09")
    @Override
    public void shouldShowErrorIfInputtedYearIsNotAllowable(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkYearFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным номером: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidCardNumber")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-11"), @TmsLink(value = "ui-21")})
    @Issues({@Issue(value = "15"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidNumber(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkNumberFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным месяцем: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidMonth")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-13"), @TmsLink(value = "ui-22")})
    @Issues({@Issue(value = "12"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidMonth(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkMonthFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным годом: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidYear")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-15"), @TmsLink(value = "ui-23")})
    @Issue(value = "10")
    @Override
    public void shouldShowErrorIfInputtedInvalidYear(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkYearFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным именем владельца: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidHolderName")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-17"), @TmsLink(value = "ui-24")})
    @Issue(value = "5")
    @Override
    public void shouldShowErrorIfInputtedInvalidHolder(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkHolderFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным CVC-кодом: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidCode")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-19"), @TmsLink(value = "ui-25")})
    @Issues({@Issue(value = "4"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidCode(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkCodeFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Отправка пустой формы покупки тура по карте")
    @Test
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-26")
    @Issue(value = "10")
    @Override
    public void shouldShowErrorIfSendEmptyForm() {
        buyTourPage.clickContinueButton();

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkNumberFieldError(Errors.emptyField);
        buyTourPage.checkMonthFieldError(Errors.emptyField);
        buyTourPage.checkYearFieldError(Errors.emptyField);
        buyTourPage.checkHolderFieldError(Errors.emptyField);
        buyTourPage.checkCodeFieldError(Errors.emptyField);
        assertNull(db.getPaymentOrderId());
    }
}
