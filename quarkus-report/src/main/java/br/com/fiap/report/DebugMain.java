package br.com.fiap.report;

import io.quarkus.runtime.Quarkus;

public class DebugMain {
    public static void main(String[] args) {
        Quarkus.run(args);  // sobe CDI
//        LambdaClient client = new LambdaClient("http://localhost:8080");

        // Monta o evento
//        String eventJson = "{\"name\":\"Gabriel\"}";
//
//        // Chama a Lambda
//        String result = client.invoke("FeedbackLambdaHandler", eventJson);
//
//        System.out.println("Resultado: " + result);
    }
}
