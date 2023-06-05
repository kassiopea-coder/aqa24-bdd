package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class TransferPage {
  private SelenideElement heading = $x("//h1[contains(text(), 'Пополнение')]");
  private SelenideElement amountInput = $("[data-test-id='amount'] input");
  private SelenideElement fromCard = $("[data-test-id='from'] input");
  private SelenideElement transferButton = $("[data-test-id='action-transfer']");
  private SelenideElement errorMessage = $("[data-test-id='error-message']");

  public TransferPage() {
    heading.shouldBe(visible);
  }

  public void getTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
    amountInput.setValue(amountToTransfer);
    fromCard.setValue(cardInfo.getCardNumber());
    transferButton.click();
  }

  public DashboardPage getValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
    getTransfer(amountToTransfer, cardInfo);
    return new DashboardPage();
  }

  public void findErrorMessage(String expectedText) {
    errorMessage.shouldBe(visible, Duration.ofSeconds(15)).
            shouldHave(text(expectedText));
  }
}
