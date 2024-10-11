package tests;

import Data.CourierJson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import steps.CreateCourierSteps;

import static org.apache.http.HttpStatus.*;

public class CreateCourierTest {

    private String login;
    private String password;
    private String firstName;
    private int courierId; // переменная для хранения ID созданного курьера
    CreateCourierSteps courierSteps = new CreateCourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        login = "Lio";
        password = "122333";
        firstName = "Tapolski";
    }

    @After
    public void deleteCourier() {
        courierSteps.deleteCourier(courierId);
    }

    @Test
    @DisplayName("1. Курьера можно создать")
    @Description("Создание курьера")
    public void createCourierTest() {
        CourierJson courierJson = new CourierJson(login, password, firstName);
        Response response = courierSteps.createCourier(courierJson);
        courierSteps.compareResponseCode(response, SC_CREATED);

        courierSteps.checkSuccessResponse(response, true);
        courierId = courierSteps.loginCourierAndGetId(courierJson); // Получаем ID курьера для последующего удаления
    }

    @Test
    @DisplayName("2. Нельзя создать двух одинаковых курьеров")
    @Description("Отправка тех же данных при котором создался курьер для проверки дубляжа")
    public void duplicateAccountTest() {
        CourierJson courierJson = new CourierJson(login, password, firstName);
        courierSteps.createCourier(courierJson);
        courierId = courierSteps.loginCourierAndGetId(courierJson);

        Response duplicateCourier = courierSteps.createCourier(courierJson);
        courierSteps.compareResponseCode(duplicateCourier, SC_CONFLICT);
        courierSteps.checkErrorMessage(duplicateCourier, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("3.Чтобы создать курьера, нужно передать в ручку все обязательные поля")
    @Description("Попытка создания аккаунта без логина затем без пароля")
    public void requiredFieldsTest() {
        // Попытка создания курьера без логина
        CourierJson courierJsonWithoutLogin = new CourierJson(null, password, firstName);
        Response withoutLogin = courierSteps.createCourier(courierJsonWithoutLogin);
        courierSteps.compareResponseCode(withoutLogin, SC_BAD_REQUEST);
        courierSteps.checkErrorMessage(withoutLogin, "Недостаточно данных для создания учетной записи");

        CourierJson courierJsonWithoutPassword = new CourierJson(login, null, firstName);
        Response withoutPassword = courierSteps.createCourier(courierJsonWithoutPassword);
        courierSteps.compareResponseCode(withoutPassword, SC_BAD_REQUEST);
        courierSteps.checkErrorMessage(withoutPassword, "Недостаточно данных для создания учетной записи");
    }

    // Попытка создания курьера без пароля
    @Test
    @DisplayName("4. Если создать пользователя с логином, который уже есть, возвращается ошибка.")
    @Description("Создание курьера c тем же логином")
    public void createCourierDuplicateLoginTest() {
        CourierJson courierJson = new CourierJson(login, password, firstName);
        courierSteps.createCourier(courierJson);
        courierId = courierSteps.loginCourierAndGetId(courierJson); // Получаем ID курьера для последующего удаления
        // Попытка создания курьера c тем же логином
        CourierJson courierJsonDuplicate = new CourierJson(login, "54321", "Forest Gump");
        Response duplicateLogin = courierSteps.createCourier(courierJsonDuplicate);
        courierSteps.compareResponseCode(duplicateLogin, SC_CONFLICT);
        courierSteps.checkErrorMessage(duplicateLogin, "Этот логин уже используется. Попробуйте другой.");
    }
}