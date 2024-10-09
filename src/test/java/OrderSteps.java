import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps {

    public static final String COURIER_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public int createOrder(ArrayList<String> color) {
        OrderJson order = new OrderJson(color);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(COURIER_PATH);
                 response
                .then()
                .log()
                .all()
                .statusCode(201) // Проверка, что статус ответа 201 (создано)
                .body("track", notNullValue()); // Проверка, что трек заказа не null
       return response.jsonPath().getInt("track"); // Извлекаем трек заказа
    }

    @Step("Отмена заказа")
    public void tearDown(int trackId) {
        // После теста, если trackId не равен 0, отменяем заказ
        if (trackId != 0) {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .queryParam("track", trackId)
                    .put(COURIER_PATH+"/cancel")
                    .then()
                    .statusCode(200) // Проверка, что статус ответа 200 (успешно)
                    .body("ok", equalTo(true)); // Проверка, что ответ содержит поле "ok" со значением true
        }
    }
}
