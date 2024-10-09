import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierSteps {

    public static final String COURIER_PATH = "/api/v1/courier/";

    // Метод для создания курьера
    @Step("Создать курьера")
    public Response createCourier(String login, String password, String firstName) {
        CourierJson courierJson = new CourierJson(login, password, firstName);
        return given()
                .contentType(ContentType.JSON)
                .body(courierJson)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Создать курьера без поля логина")
    public Response createCourierWithoutLogin(String password, String firstName) {
        CourierJson courierJson = new CourierJson( null,password, firstName);
        return given()
                .contentType(ContentType.JSON)
                .body(courierJson)
                .when()
                .post(COURIER_PATH);
    }
    @Step("Создать курьера без поля пароль")
    public Response createCourierWithoutPassword(String login, String firstName) {
        CourierJson courierJson = new CourierJson( login,null, firstName);
        return given()
                .contentType(ContentType.JSON)
                .body(courierJson)
                .when()
                .post(COURIER_PATH);
    }

    // Метод для логина курьера и получения его ID
    @Step("Залогинить курьера с логином паролем и получить его ID")
    public int loginCourierAndGetId(String login, String password) {
        CourierJson courierLogin = new CourierJson(login, password, null);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(courierLogin)
                .when()
                .post(COURIER_PATH + "login");

        response.then().assertThat().statusCode(200);
        return response.jsonPath().getInt("id");
    }

    // Метод для удаления курьера
    @Step("Удалить курьера с ID {courierId}")
    public void deleteCourier(int courierId) {
        if (courierId != 0) {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(COURIER_PATH + courierId)
                    .then().assertThat().statusCode(200);
        }
    }


    @Step("Проверить успешный код ответа")
    public void compareResponseCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    @Step("Успешный запрос возвращает ok: true")
    @Description("Шаг для проверки нужного ответа ok:true")
    public void checkSuccessResponse(Response response, boolean expected) {
        response.then().assertThat().body("ok", equalTo(expected));
    }

    // Шаг для проверки сообщения об ошибке
    @Step("Проверить сообщение об ошибке: {expectedMessage}")
    public void checkErrorMessage(Response response, String expectedMessage) {
        response.then().assertThat().body("message", equalTo(expectedMessage));
    }
}
