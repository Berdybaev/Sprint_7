import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class LoginTest {

    private String login;
    private String password;
    private String firstname;
    private String incorrectLogin;
    private String incorrectPassword;

    private static int courierId;

    CreateCourierSteps courierLoginSteps = new CreateCourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        login = "wwwlenin";
        password = "122333";
        firstname = "grad";
        incorrectLogin = "wwwStalin";
        incorrectPassword = "123111";
    }

    @AfterClass
    public static void deleteAccount() {
        LoginSteps.deleteAccount(courierId);
    }

    @Test
    @DisplayName("Курьер может авторизоваться;")
    public void testLogin() {
        Response autoResponse = LoginSteps.createCourier(login, password, firstname);//создание учетной записи
        LoginSteps.compareResponseCode(autoResponse, 201);
        courierLoginSteps.checkSuccessResponse(autoResponse, true);
        Response loginResponse = LoginSteps.loginCourierAndGetId(login, password); // Авторизуемся
        courierLoginSteps.compareResponseCode(loginResponse, 200);
        LoginSteps.checkSuccessResponse(loginResponse);
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("1. для авторизации нужно передать все обязательные поля. " +
            "2 если какого-то поля нет, запрос возвращает ошибку;")
    public void testAuthWithoutRequiredFields() {
        //Попытка авторизации без логина
        Response authWithoutLogin = LoginSteps.loginCourier(null, password);
        LoginSteps.compareResponseCode(authWithoutLogin, 400);
        courierLoginSteps.checkErrorMessage(authWithoutLogin, "Недостаточно данных для входа");

        //Попытка авторизации без пароля
        Response authWithoutPassword = LoginSteps.loginCourier(login, "");
        LoginSteps.compareResponseCode(authWithoutPassword, 400);
        courierLoginSteps.checkErrorMessage(authWithoutPassword, "Недостаточно данных для входа");

        //Попытка авторизации без логина и пароля
        Response authWithoutFields = LoginSteps.loginCourier("", "");
        LoginSteps.compareResponseCode(authWithoutFields, 400);
        courierLoginSteps.checkErrorMessage(authWithoutFields, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("1 .Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку" +
            "2. Cистема вернёт ошибку, если неправильно указать логин или пароль")
    public void testAuthWithIncorrectData() {
        //Попытка авторизации с несуществующими данными
        Response incorrectData = LoginSteps.loginCourier(incorrectLogin, incorrectPassword);
        LoginSteps.compareResponseCode(incorrectData, 404);
        courierLoginSteps.checkErrorMessage(incorrectData, "Учетная запись не найдена");
        //Попытка авторизации с неверным логином
        Response wrongLogin = LoginSteps.loginCourier(incorrectLogin, password);
        LoginSteps.compareResponseCode(wrongLogin, 404);
        courierLoginSteps.checkErrorMessage(wrongLogin, "Учетная запись не найдена");
        //Попытка авторизации с неверным паролем
        Response wrongPassword = LoginSteps.loginCourier(login, incorrectPassword);
        LoginSteps.compareResponseCode(wrongPassword, 404);
        courierLoginSteps.checkErrorMessage(wrongPassword, "Учетная запись не найдена");
    }


}
