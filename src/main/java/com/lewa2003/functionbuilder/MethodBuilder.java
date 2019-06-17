package com.lewa2003.functionbuilder;

import java.util.ArrayList;
import java.util.List;

class MethodBuilder {
    private static StringBuilder generatedMethod = new StringBuilder();

    static String buildFunction(RestService service) {

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

        return generatedMethod.toString();
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
            headPart.append(parameters.get(i).getName());
            headPart.append(", ");
        }
        headPart.append(parameters.get(parameters.size()-1).getName());
        headPart.append(")\n");
        generatedMethod.append(headPart.toString());
    }

    private static void addUrl(RestService service, List<Parameter> urlParameters) {
        String template = "/%s/";
        StringBuilder url = new StringBuilder(String.format(template,service.getUrl().split("/")[1]));
        StringBuilder urlPart = new StringBuilder("\turl = СтрШаблон(\"");
        urlPart.append(url);
        if (urlParameters.size() == 0) {
            urlPart.append("\");\n");
        } else{
            urlPart.append("%1\", ");
            urlPart.append(urlParameters.get(0).getName());
            urlPart.append(");\n");
        }
        generatedMethod.append(urlPart.toString());

    }

    private static void addParameterParameters(List<Parameter> parameterParameters) {
        StringBuilder parameterPart = new StringBuilder("\turl = url");
        for (Parameter i : parameterParameters) {
            String template = " + \"?%s=\" + %s";
            parameterPart.append(String.format(template, i.getName(), i.getName()));
        }
        parameterPart.append(";\n");
        generatedMethod.append(parameterPart.toString());

    }

    private static void addHeaderParameters(List<Parameter> headerParameters) {
        StringBuilder headerPart = new StringBuilder("\tЗаголовки = Новый Соответствие();\n");
        for (Parameter i : headerParameters) {
            String template = "\tЗаголовки.Добавить(\"%s\", %s);\n";
            headerPart.append(String.format(template, i.getName(), i.getName()));
        }
        headerPart.append("\tЗапрос = Новый HttpЗапрос(url, Заголовки);\n");
        generatedMethod.append(headerPart.toString());
    }

    private static void addBodyParameters(List<Parameter> bodyParameters) {
        StringBuilder bodyPart = new StringBuilder("\tТело = Новый Соответствие();\n");
        for (Parameter i : bodyParameters) {
            String template = "\tТело.Вставить(\"%s\", %s);\n";
            bodyPart.append(String.format(template, i.getName(), i.getName()));
        }
        bodyPart.append("\tЗаписьJson = Новый ЗаписьJson();\n");
        bodyPart.append("\tЗапрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n");
        generatedMethod.append(bodyPart.toString());
    }

    private static void addLastPart(RestService service) {
        StringBuilder lastPart = new StringBuilder("\tСоединение = Новый HTTPСоединение(\"");
        lastPart.append(service.getHost());
        lastPart.append("\");\n\tОтвет = Соединение.ВызватьHTTPМетод(\"");
        lastPart.append(service.getMethod());
        lastPart.append("\", Запрос);\n");
        lastPart.append("\tЕсли Ответ.КодСостояния <> 200 Тогда\n");
        lastPart.append("\t\tВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"");
        lastPart.append(service.getName());
        lastPart.append("\");\n\tКонецЕсли;\nКонецПроцедура\n");

        generatedMethod.append(lastPart.toString());
    }
}
