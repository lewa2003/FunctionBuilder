package com.lewa2003.functionbuilder;

import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This module can be used to generate 1C function in according to the given YAML description
 * to access the particular REST service.
 *
 *
 * YAML description example:
 *
 * name: МойСервис
 * host: http://myhost/
 * method: POST
 * url: /myservice/{п1}
 * parameters:
 *     -
 *         name: п1
 *         place: url
 *         type: string
 *     -
 *         name: п2
 *         place: parameter
 *         type: number
 *     -
 *         name: п3
 *         place: body
 *         type: number
 *     -
 *         name: п4
 *         place: header
 *         type: number
 *
 *
 * Usage example:
 *
 * String yamlInput = new String(); // this string contains YAML REST service description (eg. can be read from a file)
 * MethodBuilder mb = new MethodBuilder();
 * String 1cFunctionCode = mb.buildFunction(yamlInput); // 1cFunctionCode now contains 1C method which can be used to
 *                                                      // access the required REST service
 */
public class MethodBuilder {

    /**
     * Generates 1C function in according to the given YAML description.
     * Main steps:
     *   1. Request URL building in respect with the given parameters.
     *   2. Request body building in respect with the given parameters.
     *   3. Request headers building in respect with the given parameters.
     *   4. REST service access routine.
     *   5. REST service response processing routine.
     * @param input REST service YAML description.
     * @return string with a 1C function code to access the service.
     * @throws MalformedYaml in case the given YAML description has incorrect format the exception is thrown.
     */
    public String buildFunction(String input) throws MalformedYaml {

        RestService service = readYaml(input);

        validate(service);

        StringBuilder generatedMethod = new StringBuilder();

        List<Parameter> parameters = service.getParameters();

        addHeadPart(service, parameters, generatedMethod);

        List<Parameter> bodyParameters = parseParameters(parameters, "body");
        List<Parameter> headerParameters = parseParameters(parameters, "header");
        List<Parameter> parameterParameters = parseParameters(parameters, "parameter");
        List<Parameter> urlParameters = parseParameters(parameters, "url");

        addUrl(service, urlParameters, generatedMethod);

        if (parameterParameters.size() > 0 && service.getMethod().equals("GET")) {
            addParameterParameters(parameterParameters, generatedMethod);
        }

        addHeaderParameters(headerParameters, generatedMethod);

        if (service.getMethod().equals("POST")) {
            addBodyParameters(bodyParameters, generatedMethod);
        }

        addLastPart(service, generatedMethod);

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

    private RestService readYaml(String inputYaml) {
        Yaml yaml = new Yaml();

        InputStream inputStream = new ByteArrayInputStream(inputYaml.getBytes());
        RestService service = yaml.load(inputStream);
        return service;
    }

    private static void addHeadPart(RestService service, List<Parameter> parameters, StringBuilder generatedMethod) {
        StringBuilder headPart = new StringBuilder("Процедура ");
        headPart.append(service.getName());
        headPart.append("(");
        if (parameters.size() != 0) {
            for (int i = 0; i < parameters.size() - 1; i++) {
                headPart.append(parameters.get(i).getName());
                headPart.append(", ");
            }
            headPart.append(parameters.get(parameters.size() - 1).getName());
        }
        headPart.append(")\n");
        generatedMethod.append(headPart.toString());
    }

    private static void addUrl(RestService service, List<Parameter> urlParameters,
                               StringBuilder generatedMethod) throws MalformedYaml {

        String template = "/%s/";
        StringBuilder url = new StringBuilder(String.format(template,service.getUrl().split("/")[1]));
        StringBuilder urlPart = new StringBuilder();
        if (urlParameters.size() == 0) {
            urlPart.append("\turl = \"");
            urlPart.append(url);
            urlPart.append("\";\n");
        } else if (urlParameters.size() > 1) {
            throw new MalformedYaml("Only 1 url parameter is allowed");
        } else {
            urlPart.append("\turl = СтрШаблон(\"");
            urlPart.append(url);
            urlPart.append("%1\", ");
            urlPart.append(urlParameters.get(0).getName());
            urlPart.append(");\n");
        }
        generatedMethod.append(urlPart.toString());
    }

    private static void addParameterParameters(List<Parameter> parameterParameters, StringBuilder generatedMethod) {
        StringBuilder parameterPart = new StringBuilder("\turl = url");
        for (Parameter i : parameterParameters) {
            String template = " + \"?%s=\" + %s";
            parameterPart.append(String.format(template, i.getName(), i.getName()));
        }
        parameterPart.append(";\n");
        generatedMethod.append(parameterPart.toString());

    }

    private static void addHeaderParameters(List<Parameter> headerParameters, StringBuilder generatedMethod) {
        StringBuilder headerPart = new StringBuilder("\tЗаголовки = Новый Соответствие();\n");
        for (Parameter i : headerParameters) {
            String template = "\tЗаголовки.Добавить(\"%s\", %s);\n";
            headerPart.append(String.format(template, i.getName(), i.getName()));
        }
        headerPart.append("\tЗапрос = Новый HttpЗапрос(url, Заголовки);\n");
        generatedMethod.append(headerPart.toString());
    }

    private static void addBodyParameters(List<Parameter> bodyParameters, StringBuilder generatedMethod) {
        StringBuilder bodyPart = new StringBuilder("\tТело = Новый Соответствие();\n");
        for (Parameter i : bodyParameters) {
            String template = "\tТело.Вставить(\"%s\", %s);\n";
            bodyPart.append(String.format(template, i.getName(), i.getName()));
        }
        bodyPart.append("\tЗаписьJson = Новый ЗаписьJson();\n");
        bodyPart.append("\tЗапрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n");
        generatedMethod.append(bodyPart.toString());
    }

    private static void addLastPart(RestService service, StringBuilder generatedMethod) {
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

    private static void validate(RestService service) throws MalformedYaml {

        service.actualize();

        Set<String> parametersSet = new HashSet<>();
        for (Parameter i : service.getParameters()) {
            if (!parametersSet.add(i.getName())) {
                throw new MalformedYaml("Dublicated parameters");
            }
        }

        if (service.getName().length() == 0) {
            throw new MalformedYaml("No service name in yaml file");
        }
        if (service.getHost().length() == 0) {
            throw new MalformedYaml("No host in yaml file");
        }
        if (service.getMethod().length() == 0) {
            throw new MalformedYaml("No method in yaml file");
        }
        if (service.getUrl().length() == 0) {
            throw new MalformedYaml("No url in yaml file");
        }

    }
}
