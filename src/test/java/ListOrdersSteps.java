import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrdersSteps {
    public static final String COURIER_PATH = "/api/v1/orders";

    @Step
    public void getOrderList(int courierId, int nearestStation, int limit, int page) {
        given()
                .queryParams("courierId", courierId,
                        "nearestStation", nearestStation,
                        "limit", limit,
                        "page", page)
                .get(COURIER_PATH)
                .then()
                .statusCode(200) // Проверяем, что статус ответа 200 (ОК)
                .body("orders", notNullValue()) // Проверяем, что поле "orders" не null
                .body("orders.size()", greaterThan(0));
    }


}
