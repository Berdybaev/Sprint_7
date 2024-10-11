package steps;

import Data.OrderJson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps {

    public static final String COURIER_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createOrder(OrderJson orderJson) {
        return given()
                .contentType(ContentType.JSON)
                .body(orderJson)
                .post(COURIER_PATH);

    }

    @Step("Отмена заказа")
    public void tearDown(int trackId) {
        // После теста, если trackId не равен 0, отменяем заказ
        if (trackId != 0) {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .queryParam("track", trackId)
                    .put(COURIER_PATH + "/cancel");
        }
    }

    @Step("Проверить успешный код ответа")
    public void compareResponseCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    @Step("Проверка, что трек заказа не null ")
    @Description("Шаг для проверки нужного ответа, пример track: 312546")
    public void checkSuccessGetTrack(Response response) {
        response.then().assertThat().body("track", notNullValue());
    }
}
