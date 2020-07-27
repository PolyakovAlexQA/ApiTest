package test;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import data.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.Locale;


import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.newUser;

public class ApiGetPostTest {
    Faker faker = new Faker(new Locale("en"));


    void loginForm(String login, String password) {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();

    }


    @Test
    public void shouldLoginWithValidActiveUser() {
        DataGenerator.UserInfo user = newUser(false);
        loginForm(user.getLogin(), user.getPassword());
        $(withText("Личный кабинет")).waitUntil(Condition.visible, 15000);
    }


    @Test
    public void shouldGetErrorIfNotRegisteredUser() {
        DataGenerator.UserInfo user = newUser(false);
        loginForm(faker.name().username(), faker.internet().password());
        $(withText("Неверно указан логин или пароль")).waitUntil(Condition.visible,15000);
    }
}