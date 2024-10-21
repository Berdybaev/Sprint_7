package baseurl;

import io.restassured.RestAssured;

public abstract class BaseAPI {
     public BaseAPI(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
}
