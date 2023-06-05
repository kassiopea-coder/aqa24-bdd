package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {
  LoginPage loginPage;
  DashboardPage dashboardPage;

  @BeforeEach
  void setUp() {
    loginPage = open("http://localhost:9999", LoginPage.class);
    var authInfo = getAuthInfo();
    var verificationPage = loginPage.validLogin(authInfo);
    var verificationCode = getVerificationCode();
    dashboardPage = verificationPage.validVerify(verificationCode);
  }



  @Test
  void shouldTransferFromCard1ToCard2() {
    var cardInfo1 = getCard1();
    var cardInfo2 = getCard2();
    var balanceCard1 = dashboardPage.getCardBalance(cardInfo1);
    var balanceCard2 = dashboardPage.getCardBalance(cardInfo2);
    var amount = getValidAmount(balanceCard1);
    var expectedBalanceCard1 = balanceCard1 - amount;
    var expectedBalanceCard2 = balanceCard2 + amount;
    var transferPage = dashboardPage.cardTransfer(cardInfo2);
    dashboardPage = transferPage.getValidTransfer(String.valueOf(amount), cardInfo1);
    var actualBalanceCard1 = dashboardPage.getCardBalance(cardInfo1);
    var actualBalanceCard2 = dashboardPage.getCardBalance(cardInfo2);

    assertEquals(expectedBalanceCard1, actualBalanceCard1);
    assertEquals(expectedBalanceCard2, actualBalanceCard2);
  }

  @Test
  void insufficientFundsToTransfer() {
    var cardInfo1 = getCard1();
    var cardInfo2 = getCard2();
    var balanceCard1 = dashboardPage.getCardBalance(cardInfo1);
    var balanceCard2 = dashboardPage.getCardBalance(cardInfo2);
    var amount = getInvalidAmount(balanceCard2);
    var transferPage = dashboardPage.cardTransfer(cardInfo1);
    transferPage.getTransfer(String.valueOf(amount), cardInfo2);
    transferPage.findErrorMessage("Недостаточно средств для перевода");
    var actualBalanceCard1 = dashboardPage.getCardBalance(cardInfo1);
    var actualBalanceCard2 = dashboardPage.getCardBalance(cardInfo2);

    assertEquals(balanceCard1, actualBalanceCard1);
    assertEquals(balanceCard2, actualBalanceCard2);
  }
}