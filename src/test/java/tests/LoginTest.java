package tests;

import Data.CourierJson;
import Data.LoginJson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import steps.CreateCourierSteps;
import steps.LoginSteps;

import static org.apache.http.HttpStatus.*;

public class LoginTest {

    private String login = "wwwlenin";
    private String password = "122333";
    private String firstname = "grad";
    private String incorrectLogin = "wwwStalin";
    private String incorrectPassword = "123111";
    private static int courierId;
    LoginSteps loginSteps = new LoginSteps();
    CreateCourierSteps courierloginSteps = new CreateCourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @AfterClass
    public static void deleteAccount() {
        LoginSteps.deleteAccount(courierId);
    }

    @Test
    @DisplayName("Курьер может авторизоваться;")
    public void testLogin() {
        CourierJson courierJson = new CourierJson(login, password, firstname);
        Response autoResponse = loginSteps.createCourier(courierJson);//создание учетной записи
        loginSteps.compareResponseCode(autoResponse, SC_CREATED);
        courierloginSteps.checkSuccessResponse(autoResponse, true);

        Response loginResponse = loginSteps.loginCourierAndGetId(courierJson); // Авторизуемся
        courierloginSteps.compareResponseCode(loginResponse, SC_OK);
        loginSteps.checkSuccessResponse(loginResponse);
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("1. для авторизации нужно передать все обязательные поля. " +
            "2 если какого-то поля нет, запрос возвращает ошибку;")
    public void testAuthWithoutRequiredFields() {

        //Попытка авторизации без логина
        LoginJson courierAuthWithoutLogin = new LoginJson(null, password);
        Response authWithoutLogin = loginSteps.loginCourier(courierAuthWithoutLogin);
        loginSteps.compareResponseCode(authWithoutLogin, SC_BAD_REQUEST);
        courierloginSteps.checkErrorMessage(authWithoutLogin, "Недостаточно данных для входа");

        //Попытка авторизации без пароля
        LoginJson courierAuthWithoutPassword = new LoginJson(login, "");
        Response authWithoutPassword = loginSteps.loginCourier(courierAuthWithoutPassword);
        loginSteps.compareResponseCode(authWithoutPassword, SC_BAD_REQUEST);
        courierloginSteps.checkErrorMessage(authWithoutPassword, "Недостаточно данных для входа");

        //Попытка авторизации без логина и пароля
        LoginJson courierAuthWithoutLoginPassword = new LoginJson("", "");
        Response authWithoutFields = loginSteps.loginCourier(courierAuthWithoutLoginPassword);
        loginSteps.compareResponseCode(authWithoutFields, SC_BAD_REQUEST);
        courierloginSteps.checkErrorMessage(authWithoutFields, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("1 .Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку" +
            "2. Cистема вернёт ошибку, если неправильно указать логин или пароль")
    public void testAuthWithIncorrectData() {

        //Попытка авторизации с несуществующими данными
        LoginJson courierAuthWithoutIncorrectData = new LoginJson(incorrectLogin, incorrectPassword);
        Response incorrectData = loginSteps.loginCourier(courierAuthWithoutIncorrectData);
        loginSteps.compareResponseCode(incorrectData, SC_NOT_FOUND);
        courierloginSteps.checkErrorMessage(incorrectData, "Учетная запись не найдена");

        //Попытка авторизации с неверным логином
        LoginJson courierAuthWithoutIncorrectLogin = new LoginJson(incorrectLogin, password);
        Response wrongLogin = loginSteps.loginCourier(courierAuthWithoutIncorrectLogin);
        loginSteps.compareResponseCode(wrongLogin, SC_NOT_FOUND);
        courierloginSteps.checkErrorMessage(wrongLogin, "Учетная запись не найдена");

        //Попытка авторизации с неверным паролем
        LoginJson courierAuthWithoutIncorrectPassword = new LoginJson(login, incorrectPassword);
        Response wrongPassword = loginSteps.loginCourier(courierAuthWithoutIncorrectPassword);
        loginSteps.compareResponseCode(wrongPassword, SC_NOT_FOUND);
        courierloginSteps.checkErrorMessage(wrongPassword, "Учетная запись не найдена");
    }
}
