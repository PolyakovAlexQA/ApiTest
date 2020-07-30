package data;

import com.github.javafaker.Faker;
import domain.UserInfo;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;


public class DataGenerator {
    private DataGenerator() {
    }

    public static void setUpUser(UserInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .addFilter(new ResponseLoggingFilter())
            .log(LogDetail.ALL)
            .build();


    public static class RegistrationInfo {
        private RegistrationInfo() {
        }

        public static String userPassword(String Locale) { // генерация пароля с помощью faker
            Faker faker = new Faker(new Locale(Locale));
            return faker.internet().password();
        }

        public static String userName(String Locale) { // генерация имени с помощью фейкер
            Faker faker = new Faker(new Locale(Locale));
            return faker.name().username();
        }

        public static UserInfo generateUserInfo(String locale, boolean isBlocked) { // устанавливаем значение blocked при неверном имени и пароли которые мы сгенерирвали
            return new UserInfo(
                    userName(locale),
                    userPassword(locale),
                    (isBlocked) ? "blocked" : "active"); // значение статуса при возвращаемых данных(true,false)
        }

        public static UserInfo generateValidUserInfo(String locale, boolean isBlocked) { // создаем статический метод
            UserInfo user = generateUserInfo(locale, isBlocked); // берем данные  с класса UserInfo присваеваем значение blocked
            setUpUser(user);
            return user; // возвращаем тело
        }

        public static UserInfo generateInvalidLoginUserInfo(String locale, boolean isBlocked) { // метод невалидных значений Логина
            String password = userPassword(locale);
            UserInfo user = new UserInfo( // берем данные с класса UserInfo
                    "vasya",// присваеваем им логин vasya
                    password, //генерируем пароль автоматически
                    (isBlocked) ? "blocked" : "active"); // статус
            setUpUser(user);
            return new UserInfo(
                    "petya",
                    password,
                    (isBlocked) ? "blocked" : "active");
        }

        public static UserInfo generateInvalidPasswordUserInfo(String locale, boolean isBlocked) { // метод невалидных значений пароля
            String login = userName(locale);
            UserInfo user = new UserInfo(
                    login,
                    "validpass",
                    (isBlocked) ? "blocked" : "active");
            setUpUser(user);
            return new UserInfo(
                    login,
                    "invalidpass",
                    (isBlocked) ? "blocked" : "active");
        }
    }
}