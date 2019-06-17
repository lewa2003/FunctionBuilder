package com.lewa2003.functionbuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FunctionBuilder {
    public static void main(String args[]) {
        RestService service = YamlParsing.readYaml(args[0]);
        String output = MethodBuilder.buildFunction(service);
        try {
            Files.write(Paths.get(args[1]), output.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



