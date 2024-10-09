import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginSteps {

    public static final String COURIER_PATH = "/api/v1/courier/";

    @Step("Залогинить курьера с логином паролем и получить его ID")
    public static Response loginCourierAndGetId(String login, String password) {
        CourierJson courier = new CourierJson(login, password, null);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_PATH + "login");                ;

        response.then().assertThat().statusCode(200);
        return response;
    }

    @Step("Удалить курьера с ID {courierId}")
    public static void deleteAccount(int courierId) {
        if (courierId != 0) {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(COURIER_PATH + courierId)
                    .then().assertThat().statusCode(200);
        }
    }

    @Step("Залогинить курьера с логином паролем и получить его ID")
    public static Response loginCourier(String login, String password) {
        LoginJson courierTest = new LoginJson(login, password);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(courierTest)
                .when()
                .post(COURIER_PATH + "login");                ;
        return response;
    }

    @Description("Создать курьера")
    public static Response createCourier(String login, String password, String firstName) {
        CourierJson courier = new CourierJson(login, password, firstName);
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }


    @Step("Проверить успешный код ответа")
    public static void compareResponseCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    @Step("Успешный запрос возвращает id: значение ")
    @Description("Шаг для проверки нужного ответа, пример id: 312546")
    public static void checkSuccessResponse(Response response) {
        response.then().assertThat().body("id", notNullValue());
    }


}
