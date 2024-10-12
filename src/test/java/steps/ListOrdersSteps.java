package steps;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrdersSteps {
    public static final String COURIER_PATH = "/api/v1/orders";

    @Step
    public Response getOrderList(int courierId, int nearestStation, int limit, int page) {
        return given()
                .queryParams("courierId", courierId,
                        "nearestStation", nearestStation,
                        "limit", limit,
                        "page", page)
                .get(COURIER_PATH);
    }

    @Step("Проверить успешный код ответа")
    public void compareResponseCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    @Step("Проверка, что заказ не пустой ")
    public void checkSuccessGetOrders(Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }

    @Step("Проверка, что массив заказов не пустой  ")
    public void checkOrdersSize(Response response) {
        response.then().assertThat().body("orders.size()", greaterThan(0));
    }
}
