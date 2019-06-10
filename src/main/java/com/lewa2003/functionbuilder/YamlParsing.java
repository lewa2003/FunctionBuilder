package com.lewa2003.functionbuilder;

import org.yaml.snakeyaml.Yaml;

import java.io.*;

class YamlParsing {
    static RestService ReadYaml (String inputYamlPath) {
        Yaml yaml = new Yaml();

        String restServiceClassPath = "!!com.lewa2003.functionbuilder.RestService\n";

        InputStream inputStream = null;
        try {
            inputStream = new SequenceInputStream(new ByteArrayInputStream(restServiceClassPath.getBytes()),
                    new FileInputStream(new File(inputYamlPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RestService service = yaml.load(inputStream);
        return service;
    }
}