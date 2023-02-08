package ru.netology.shop.db;

import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllArgsConstructor
@Value
public class Order {
    String id;
    String status;
    int amount;

    public Order(String status, int amount) {
        this.id = "1";
        this.status = status;
        this.amount = amount;
    }

    public Order(String status) {
        this.id = "1";
        this.status = status;
        this.amount = 0;
    }

    @Step("Проверяем, что заказ внесен в БД корректно, его статус «{expectedOrder.status}», а сумма {expectedOrder.amount}.")
    public void assertPaymentOrder(Order expectedOrder) {
        assertEquals(expectedOrder.getStatus(), this.status);
        assertEquals(expectedOrder.getAmount(), this.amount);
        assertNotNull(this.getId());
    }

    @Step("Проверяем, что заказ внесен в БД корректно, а его статус «{expectedOrder.status}».")
    public void assertCreditOrder(Order expectedOrder) {
        assertEquals(expectedOrder.getStatus(), this.status);
        assertNotNull(this.getId());
    }
}
