package org.acme;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GreetingLamba implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String input, Context context) {
        
        return "Hello do Alessandro  -> " + input;
    }
}
