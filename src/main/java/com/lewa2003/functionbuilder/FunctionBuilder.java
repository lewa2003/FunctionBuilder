package com.lewa2003.functionbuilder;

public class FunctionBuilder {
    public static void main(String args[]) {
        RestService service = YamlParsing.ReadYaml(args[0]);
        MethodBuilder.BuildFunction(service, args[1]);
    }
}



