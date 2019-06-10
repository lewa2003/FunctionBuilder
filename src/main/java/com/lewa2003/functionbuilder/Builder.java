package com.lewa2003.functionbuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class Builder {
    static void BuildFunction (RestService service, String outputPath) {
        List<String> lines = new ArrayList<>();

        String headPart = "Процедура " + service.getName() + "(";
        List<Parameter> parameters = service.getParameters();
        for (int i = 0; i < parameters.size()-1; i++) {
            headPart += parameters.get(i).getName() + ", ";
        }
        headPart += parameters.get(parameters.size()-1).getName() + ")\n";
        lines.add(headPart);

        List<Parameter> bodyParameters = ParseParameters(parameters, "body");
        List<Parameter> headerParameters = ParseParameters(parameters, "header");
        List<Parameter> parameterParameters = ParseParameters(parameters, "parameter");
        List<Parameter> urlParameterss = ParseParameters(parameters, "url");

        if (parameterParameters.size() > 0){
            String urlPart = "    url = url";
            for (int i = 0; i < parameterParameters.size(); i++) {
                urlPart += " + \"?" + parameterParameters.get(i).getName() +
                        "=\" + " + parameterParameters.get(i).getName();
            }
            urlPart += ";\n";
            lines.add(urlPart);
        }

        if (headerParameters.size() > 0){
            String headerPart = "    Заголовки = Новый Соответствие();\n";
            for (Parameter i : headerParameters) {
                headerPart += "    Заголовки.Добавить(\"" + i.getName() + "\", " + i.getName() + ");\n";
            }
            headerPart += "    Запрос = Новый HttpЗапрос(url, Заголовки);\n";
            lines.add(headerPart);
        }

        if (bodyParameters.size() > 0) {

            String bodyPart = "    Тело = Новый Соответствие();\n";
            for (Parameter i : bodyParameters) {
                bodyPart += "    Тело.Вставить(\"" + i.getName() + "\", " + i.getName() + ");\n";
            }
            bodyPart += "    ЗаписьJson = Новый ЗаписьJson();\n" +
                    "    Запрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n";
            lines.add(bodyPart);
        }

        String lastPart = "    Соединение = Новый HTTPСоединение(Хост);\n" +
                "    Ответ = Соединение.ВызватьHTTPМетод(Метод, Запрос);\n" +
                "    Если Ответ.КодСостояния <> 200 Тогда\n" +
                "        ВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"\");\n" +
                "    КонецЕсли;\n" +
                "КонецПроцедура\n";

        lines.add(lastPart);


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
