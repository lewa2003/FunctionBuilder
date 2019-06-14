package com.lewa2003.functionbuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class MethodBuilder {
    static void BuildFunction (RestService service, String outputPath) {
        List<String> lines = new ArrayList<>();

        StringBuilder headPart = new StringBuilder("Процедура ");
        headPart.append(service.getName());
        headPart.append("(");
        List<Parameter> parameters = service.getParameters();
        for (int i = 0; i < parameters.size()-1; i++) {
            headPart.append(parameters.get(i).getName()); headPart.append(", ");
        }
        headPart.append(parameters.get(parameters.size()-1).getName());
        headPart.append(")\n");
        lines.add(String.valueOf(headPart));

        List<Parameter> bodyParameters = ParseParameters(parameters, "body");
        List<Parameter> headerParameters = ParseParameters(parameters, "header");
        List<Parameter> parameterParameters = ParseParameters(parameters, "parameter");
        List<Parameter> urlParameters = ParseParameters(parameters, "url");

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
        lines.add(String.valueOf(urlPart));

        if (parameterParameters.size() > 0){
            StringBuilder parameterPart = new StringBuilder("    url = url");
            for (Parameter i : parameterParameters) {
                parameterPart.append(" + \"?");
                parameterPart.append(i.getName());
                parameterPart.append("=\" + ");
                parameterPart.append(i.getName());
            }
            parameterPart.append(";\n");
            lines.add(String.valueOf(parameterPart));
        }

        if (headerParameters.size() > 0){
            StringBuilder headerPart = new StringBuilder("    Заголовки = Новый Соответствие();\n");
            for (Parameter i : headerParameters) {
                headerPart.append("    Заголовки.Добавить(\"");
                headerPart.append(i.getName());
                headerPart.append("\", ");
                headerPart.append(i.getName());
                headerPart.append(");\n");
            }
            headerPart.append("    Запрос = Новый HttpЗапрос(url, Заголовки);\n");
            lines.add(String.valueOf(headerPart));
        }

        if (bodyParameters.size() > 0) {
            StringBuilder bodyPart = new StringBuilder("    Тело = Новый Соответствие();\n");
            for (Parameter i : bodyParameters) {
                bodyPart.append("    Тело.Вставить(\"");
                bodyPart.append(i.getName());
                bodyPart.append("\", ");
                bodyPart.append(i.getName());
                bodyPart.append(");\n");
            }
            bodyPart.append("    ЗаписьJson = Новый ЗаписьJson();\n");
            bodyPart.append("    Запрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n");
            lines.add(String.valueOf(bodyPart));
        }

        StringBuilder lastPart = new StringBuilder("    Соединение = Новый HTTPСоединение(\"");
        lastPart.append(service.getHost());
        lastPart.append("\");\n");
        lastPart.append("    Ответ = Соединение.ВызватьHTTPМетод(\"");
        lastPart.append(service.getMethod());
        lastPart.append("\", Запрос);\n");
        lastPart.append("    Если Ответ.КодСостояния <> 200 Тогда\n");
        lastPart.append("        ВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"\");\n");
        lastPart.append("    КонецЕсли;\nКонецПроцедура\n");

        lines.add(String.valueOf(lastPart));


        try {
            Files.write(Paths.get(outputPath), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<Parameter> ParseParameters(List<Parameter> parameters, String parameterplace) {
        List<Parameter> result = new ArrayList<>();
        for (Parameter i : parameters) {
            if (i.getPlace().equals(parameterplace)) {
                result.add(i);
            }
        }
        return result;
    }
}
