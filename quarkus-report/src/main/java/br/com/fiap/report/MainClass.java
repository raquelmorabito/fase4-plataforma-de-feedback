package br.com.fiap.report;

public class MainClass {

    public static void main(String[] args) {
        System.out.println("EMAIL_USER_NAME = " + System.getenv("EMAIL_USER_NAME"));
        System.out.println("EMAIL_PASSWORD = " + System.getenv("EMAIL_PASSWORD"));

    }
}
