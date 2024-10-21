package steps;

import Data.CourierJson;
import Data.LoginJson;
import baseurl.BaseAPI;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginSteps extends BaseAPI {

    public static final String COURIER_PATH = "/api/v1/courier/";

    @Step("Залогинить курьера с логином паролем и получить его ID")
    public Response loginCourierAndGetId(CourierJson courierJson) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierJson)
                .post(COURIER_PATH + "login");
    }

    @Step("Удалить курьера с ID {courierId}")
    public static void deleteAccount(int courierId) {
        if (courierId != 0) {
            given()
                    .contentType(ContentType.JSON)
                    .delete(COURIER_PATH + courierId);
        }
    }

    @Step("Залогинить курьера с логином паролем и получить его ID")
    public Response loginCourier(LoginJson loginJson) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginJson)
                .post(COURIER_PATH + "login");
    }

    @Description("Создать курьера")
    public Response createCourier(CourierJson courierJson) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierJson)
                .post(COURIER_PATH);
    }

    @Step("Проверить успешный код ответа")
    public void compareResponseCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    @Step("Успешный запрос возвращает id: значение ")
    @Description("Шаг для проверки нужного ответа, пример id: 312546")
    public void checkSuccessResponse(Response response) {
        response.then().assertThat().body("id", notNullValue());
    }


}
