package com.lewa2003.functionbuilder;

public class FunctionBuilder {
    public static void main(String args[]) {
        RestService service = YamlParsing.readYaml(args[0]);
        MethodBuilder.buildFunction(service, args[1]);
    }
}



