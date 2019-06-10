package com.lewa2003.functionbuilder;

public class FunctionBuilder {
    public static void main(String args[]) {
        RestService service = YamlParsing.ReadYaml(args[0]);
        Builder.BuildFunction(service, args[1]);
    }
}



