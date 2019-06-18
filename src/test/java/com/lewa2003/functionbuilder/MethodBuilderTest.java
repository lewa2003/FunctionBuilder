package com.lewa2003.functionbuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.jupiter.api.Assertions.*;

class MethodBuilderTest {
    private MethodBuilder methodBuilder;

    @BeforeEach
    void runBeforeEach() {
        methodBuilder = new MethodBuilder();
    }

    @Test
    void buildDefaultValidPOSTServiceFunctionTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: POST\nurl: /myservice/{п1}\nparameters:\n" +
                "-\n name: п1\n place: url\n type: string\n" +
                "-\n name: п2\n place: body\n type: number\n" +
                "-\n name: п3\n place: body\n type: string\n" +
                "-\n name: п4\n place: header\n type: number\n" +
                "-\n name: п5\n place: header\n type: string\n";

        String originFunction = "Процедура МойСервис(п1, п2, п3, п4, п5)\n" +
                "\turl = СтрШаблон(\"/myservice/%1\", п1);\n" +
                "\tЗаголовки = Новый Соответствие();\n" +
                "\tЗаголовки.Добавить(\"п4\", п4);\n" +
                "\tЗаголовки.Добавить(\"п5\", п5);\n" +
                "\tЗапрос = Новый HttpЗапрос(url, Заголовки);\n" +
                "\tТело = Новый Соответствие();\n" +
                "\tТело.Вставить(\"п2\", п2);\n" +
                "\tТело.Вставить(\"п3\", п3);\n" +
                "\tЗаписьJson = Новый ЗаписьJson();\n" +
                "\tЗапрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n" +
                "\tСоединение = Новый HTTPСоединение(\"http://myhost/\");\n" +
                "\tОтвет = Соединение.ВызватьHTTPМетод(\"POST\", Запрос);\n" +
                "\tЕсли Ответ.КодСостояния <> 200 Тогда\n" +
                "\t\tВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"МойСервис\");\n" +
                "\tКонецЕсли;\n" +
                "КонецПроцедура\n";

        try {
            assertEquals(methodBuilder.buildFunction(restService), originFunction);
        } catch (Exception e) {
            fail("Invalid exception was thrown");
        }
    }

    @Test
    void buildDefaultInvalidPOSTServiceFunctionTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: POST\nurl: /myservice/{п1}\nparameters:\n" +
                "-\n name: п1\n place: url\n type: string\n" +
                "-\n name: п2\n place: parameter\n type: number\n" +
                "-\n name: п3\n place: parameter\n type: string\n" +
                "-\n name: п4\n place: header\n type: number\n" +
                "-\n name: п5\n place: header\n type: string\n";

        String originFunction = "Процедура МойСервис(п1, п2, п3, п4, п5)\n" +
                "\turl = СтрШаблон(\"/myservice/%1\", п1);\n" +
                "\tЗаголовки = Новый Соответствие();\n" +
                "\tЗаголовки.Добавить(\"п4\", п4);\n" +
                "\tЗаголовки.Добавить(\"п5\", п5);\n" +
                "\tЗапрос = Новый HttpЗапрос(url, Заголовки);\n" +
                "\tТело = Новый Соответствие();\n" +
                "\tЗаписьJson = Новый ЗаписьJson();\n" +
                "\tЗапрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n" +
                "\tСоединение = Новый HTTPСоединение(\"http://myhost/\");\n" +
                "\tОтвет = Соединение.ВызватьHTTPМетод(\"POST\", Запрос);\n" +
                "\tЕсли Ответ.КодСостояния <> 200 Тогда\n" +
                "\t\tВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"МойСервис\");\n" +
                "\tКонецЕсли;\n" +
                "КонецПроцедура\n";

        try {
            assertEquals(originFunction, methodBuilder.buildFunction(restService));
        } catch (Exception e) {
            fail("Invalid exception was thrown");
        }
    }

    @Test
    void buildDefaultInvalidGETServiceFunctionTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: GET\nurl: /myservice/{п1}\nparameters:\n" +
                "-\n name: п1\n place: url\n type: string\n" +
                "-\n name: п2\n place: body\n type: number\n" +
                "-\n name: п3\n place: body\n type: string\n" +
                "-\n name: п4\n place: header\n type: number\n" +
                "-\n name: п5\n place: header\n type: string\n";

        String originFunction = "Процедура МойСервис(п1, п2, п3, п4, п5)\n" +
                "\turl = СтрШаблон(\"/myservice/%1\", п1);\n" +
                "\tЗаголовки = Новый Соответствие();\n" +
                "\tЗаголовки.Добавить(\"п4\", п4);\n" +
                "\tЗаголовки.Добавить(\"п5\", п5);\n" +
                "\tЗапрос = Новый HttpЗапрос(url, Заголовки);\n" +
                "\tСоединение = Новый HTTPСоединение(\"http://myhost/\");\n" +
                "\tОтвет = Соединение.ВызватьHTTPМетод(\"GET\", Запрос);\n" +
                "\tЕсли Ответ.КодСостояния <> 200 Тогда\n" +
                "\t\tВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"МойСервис\");\n" +
                "\tКонецЕсли;\n" +
                "КонецПроцедура\n";
        try {
            assertEquals(originFunction, methodBuilder.buildFunction(restService));
        } catch (Exception e) {
            fail("Invalid exception was thrown");
        }
    }

    @Test
    void buildDefaultValidGETServiceFunctionTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: GET\nurl: /myservice/{п1}\nparameters:\n" +
                "-\n name: п1\n place: url\n type: string\n" +
                "-\n name: п2\n place: parameter\n type: number\n" +
                "-\n name: п3\n place: parameter\n type: string\n" +
                "-\n name: п4\n place: header\n type: number\n" +
                "-\n name: п5\n place: header\n type: string\n";

        String originFunction = "Процедура МойСервис(п1, п2, п3, п4, п5)\n" +
                "\turl = СтрШаблон(\"/myservice/%1\", п1);\n" +
                "\turl = url + \"?п2=\" + п2 + \"?п3=\" + п3;\n" +
                "\tЗаголовки = Новый Соответствие();\n" +
                "\tЗаголовки.Добавить(\"п4\", п4);\n" +
                "\tЗаголовки.Добавить(\"п5\", п5);\n" +
                "\tЗапрос = Новый HttpЗапрос(url, Заголовки);\n" +
                "\tСоединение = Новый HTTPСоединение(\"http://myhost/\");\n" +
                "\tОтвет = Соединение.ВызватьHTTPМетод(\"GET\", Запрос);\n" +
                "\tЕсли Ответ.КодСостояния <> 200 Тогда\n" +
                "\t\tВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"МойСервис\");\n" +
                "\tКонецЕсли;\n" +
                "КонецПроцедура\n";

        try {
            assertEquals(originFunction, methodBuilder.buildFunction(restService));
        } catch (Exception e) {
            fail("Invalid exception was thrown");
        }
    }

    @Test
    void redundantUrlParameterTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: POST\nurl: /myservice/{п1}/{п2}\nparameters:\n" +
                "-\n name: п1\n place: url\n type: string\n" +
                "-\n name: п2\n place: url\n type: string\n";

        assertThrows(MalformedYaml.class, () -> methodBuilder.buildFunction(restService));
    }

    @Test
    void emptyHostTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\n" +
                "method: POST\nurl: /myservice/{п2}\nparameters:\n" +
                "-\n name: п2\n place: url\n type: string\n";

        assertThrows(MalformedYaml.class, () -> methodBuilder.buildFunction(restService));
    }

    @Test
    void emptyNameTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "host: http://myhost/\n" +
                "method: POST\nurl: /myservice/{п2}\nparameters:\n" +
                "-\n name: п2\n place: url\n type: string\n";

        assertThrows(MalformedYaml.class, () -> methodBuilder.buildFunction(restService));
    }

    @Test
    void emptyMethodTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "url: /myservice/{п2}\nparameters:\n" +
                "-\n name: п2\n place: url\n type: string\n";

        assertThrows(MalformedYaml.class, () -> methodBuilder.buildFunction(restService));
    }

    @Test
    void emptyUrlTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: POST\nparameters:\n" +
                "-\n name: п2\n place: url\n type: string\n";

        assertThrows(MalformedYaml.class, () -> methodBuilder.buildFunction(restService));
    }

    @Test
    void mismatchingUrlParameterNameTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: POST\nurl: /myservice/{п1}\nparameters:\n" +
                "-\n name: п2\n place: url\n type: string\n";

        String originFunction = "Процедура МойСервис(п2)\n" +
                "\turl = СтрШаблон(\"/myservice/%1\", п2);\n" +
                "\tЗаголовки = Новый Соответствие();\n" +
                "\tЗапрос = Новый HttpЗапрос(url, Заголовки);\n" +
                "\tТело = Новый Соответствие();\n" +
                "\tЗаписьJson = Новый ЗаписьJson();\n" +
                "\tЗапрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n" +
                "\tСоединение = Новый HTTPСоединение(\"http://myhost/\");\n" +
                "\tОтвет = Соединение.ВызватьHTTPМетод(\"POST\", Запрос);\n" +
                "\tЕсли Ответ.КодСостояния <> 200 Тогда\n" +
                "\t\tВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"МойСервис\");\n" +
                "\tКонецЕсли;\n" +
                "КонецПроцедура\n";
        try {
            assertEquals(originFunction, methodBuilder.buildFunction(restService));
        } catch (Exception e) {
            fail("Invalid exception was thrown");
        }
    }

    @Test
    void emptyParametersTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: POST\nurl: /myservice/";


        String originFunction = "Процедура МойСервис()\n" +
                "\turl = \"/myservice/\";\n" +
                "\tЗаголовки = Новый Соответствие();\n" +
                "\tЗапрос = Новый HttpЗапрос(url, Заголовки);\n" +
                "\tТело = Новый Соответствие();\n" +
                "\tЗаписьJson = Новый ЗаписьJson();\n" +
                "\tЗапрос.УстановитьТелоИзСтроки(ЗаписатьJson(ЗаписьJson, Тело));\n" +
                "\tСоединение = Новый HTTPСоединение(\"http://myhost/\");\n" +
                "\tОтвет = Соединение.ВызватьHTTPМетод(\"POST\", Запрос);\n" +
                "\tЕсли Ответ.КодСостояния <> 200 Тогда\n" +
                "\t\tВызватьИсключение СтрШаблон(\"Ошибка % вызова сервиса %\", Ответ.КодСостояния, \"МойСервис\");\n" +
                "\tКонецЕсли;\n" +
                "КонецПроцедура\n";
        try {
            assertEquals(originFunction, methodBuilder.buildFunction(restService));
        } catch (Exception e) {
            fail("Invalid exception was thrown");
        }
    }

    @Test
    void duplicatedUrlParameterTest() {
        String restService = "!!com.lewa2003.functionbuilder.RestService\n" +
                "name: МойСервис\nhost: http://myhost/\n" +
                "method: POST\nurl: /myservice/{п1}\nparameters:\n" +
                "-\n name: п1\n place: url\n type: string\n" +
                "-\n name: п2\n place: parameter\n type: string\n" +
                "-\n name: п2\n place: parameter\n type: string\n";


       assertThrows(MalformedYaml.class, () -> methodBuilder.buildFunction(restService));
    }
}