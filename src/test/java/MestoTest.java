import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MestoTest {
    String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2Mzk4YjU0NGQzYjg2YTAwM2Q2N2UzNjgiLCJpYXQiOjE2NzY2NDk3MTQsImV4cCI6MTY3NzI1NDUxNH0.Li9gjyUG_I_n5YTJhGD1sKJ9oGal9ukYBg6utsEdgTI";
    private Steps step;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
        step = new Steps();
    }

    @Test
    @DisplayName("Add a new photo")
    @Description("This test is for adding a new photo to Mesto.")
    public void addNewPhoto() {
        given()
                .header("Content-type", "application/json") // Передаём Content-type в заголовке для указания типа файла
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .body("{\"name\":\"Москва\",\"link\":\"https://code.s3.yandex.net/qa-automation-engineer/java/files/paid-track/sprint1/photoSelenium.jpg\"}") // Формируем тело запроса
                .post("/api/cards") // Делаем POST-запрос
                .then().statusCode(201); // Проверяем код ответа
    }

    @Test
    @DisplayName("Like the first photo")
    @Description("This test is for liking the first photo on Mesto.")
    public void likeTheFirstPhoto() {
        String photoId = step.getTheFirstPhotoId();

        step.likePhotoById(photoId);
        step.deleteLikePhotoById(photoId);
    }

    @Test
    @DisplayName("Check user name")
    @Description("This test is for check current user's name.")
    public void checkUserName() {
        given()
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .get("/api/users/me") // Делаем GET-запрос
                .then().assertThat().body("data.name", equalTo("Incorrect Name")); // Проверяем, что имя соответствует ожидаемому
    }


} 