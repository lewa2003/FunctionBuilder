package com.lewa2003.functionbuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class MethodBuilder {
    private static List<String> lines = new ArrayList<>();

    static void buildFunction(RestService service, String outputPath) {

        List<Parameter> parameters = service.getParameters();

        addHeadPart(service, parameters);

        List<Parameter> bodyParameters = parseParameters(parameters, "body");
        List<Parameter> headerParameters = parseParameters(parameters, "header");
        List<Parameter> parameterParameters = parseParameters(parameters, "parameter");
        List<Parameter> urlParameters = parseParameters(parameters, "url");

        addUrl(service, urlParameters);

        if (parameterParameters.size() > 0){
            addParameterParameters(parameterParameters);
        }

        if (headerParameters.size() > 0){
            addHeaderParameters(headerParameters);
        }

        if (bodyParameters.size() > 0) {
           addBodyParameters(bodyParameters);
        }

        addLastPart(service);

        try {
            Files.write(Paths.get(outputPath), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Parameter> parseParameters(List<Parameter> parameters, String parameterplace) {
        List<Parameter> result = new ArrayList<>();
        for (Parameter i : parameters) {
            if (i.getPlace().equals(parameterplace)) {
                result.add(i);
            }
        }
        return result;
    }

    private static void addHeadPart(RestService service, List<Parameter> parameters) {
        StringBuilder headPart = new StringBuilder("Процедура ");
        headPart.append(service.getName());
        headPart.append("(");
        for (int i = 0; i < parameters.size()-1; i++) {
            headPart.append(parameters.get(i).getName()); headPart.append(", ");
        }
        headPart.append(parameters.get(parameters.size()-1).getName());
        headPart.append(")\n");
        lines.add(headPart.toString());
    }

    private static void addUrl(RestService service, List<Parameter> urlParameters) {
        StringBuilder url = new StringBuilder("/");
        url.append(service.getUrl().split("/")[1]);
        url.append("/");
        StringBuilder urlPart = new StringBuilder("    url = СтрШаблон(\"");
        urlPart.append(url);
        if (urlParameters.size() == 0) {
            urlPart.append("\");\n");
        } else{
            urlPart.append("%1\", ");
            urlPart.append(urlParameters.get(0).getName());
            urlPart.append(");\n");
        }
        lines.add(urlPart.toString());

    }

    private static void addParameterParameters(List<Parameter> parameterParameters) {
        StringBuilder parameterPart = new StringBuilder("    url = url");
        for (Parameter i : parameterParameters) {
            String template = " + \"?%s=\" + %s";
            parameterPart.append(String.format(template, i.getName(), i.getName()));
        }
        parameterPart.append(";\n");
        lines.add(parameterPart.toString());

    }

    private static void addHeaderParameters(List<Parameter> headerParameters) {
        StringBuilder headerPart = new StringBuilder("    Заголовки = Новый Соответствие();\n");
        for (Parameter i : headerParameters) {
            String template = "    Заголовки.Добавить(\"%s\", %s);\n";
            headerPart.append(String.format(template, i.getName(), i.getName()));
        }
        headerPart.append("    Запрос = Новый HttpЗапрос(url, Заголовки);\n");
        lines.add(headerPart.toString());
    }

    private static void addBodyParameters(List<Parameter> bodyParameters) {
        StringBuilder bodyPart = new StringBuilder("    Тело = Новый Соответствие();\n");
        for (Parameter i : bodyParameters) {
            String template = "    Тело.Вставить(\"%s\", %s);\n";
            bodyPart.append(String.format(template, i.getName(), i.getName()));
        }
        bodyPart.append("    ЗаписьJson = Новый ЗаписьJson();\n");
        bodyPart.append("    Запрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n");
        lines.add(bodyPart.toString());
    }

    private static void addLastPart(RestService service) {
        StringBuilder lastPart = new StringBuilder("    Соединение = Новый HTTPСоединение(\"");
        lastPart.append(service.getHost());
        lastPart.append("\");\n");
        lastPart.append("    Ответ = Соединение.ВызватьHTTPМетод(\"");
        lastPart.append(service.getMethod());
        lastPart.append("\", Запрос);\n");
        lastPart.append("    Если Ответ.КодСостояния <> 200 Тогда\n");
        lastPart.append("        ВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"\");\n");
        lastPart.append("    КонецЕсли;\nКонецПроцедура\n");

        lines.add(lastPart.toString());
    }
}
