package com.http;

import com.database.DataBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.model.Empl;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.util.JSONUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;

/**
 * Created by Artemie on 05.11.2016.
 */
public class CustomHttpServer {

    private static final Logger LOGGER = Logger.getLogger(CustomHttpServer.class);

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
        server.createContext("/employee/post", new PostHandler());
        server.createContext("/employee/get", new GetHandler());
        server.createContext("/employee/put", new PutHandler());
        server.createContext("/employee", new GetByOffsetAndLimitHandler());
        server.setExecutor(null);
        server.start();
        LOGGER.info("[SERVER] --> WAS STARTED SERVER");
    }

    private static class PostHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                    httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
            try {
                InputStream is = httpExchange.getRequestBody();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String requestBody = writer.toString();

                Empl empl = (Empl) JSONUtil.getJAVAObjectfromJSONString(requestBody, Empl.class);
                String response = "This id is used";
                int status = 500;

                MongoClient mongoClient = new MongoClient();
                MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
                mongoDatabase.createCollection("table");

                File file = new File("A:\\WorkSpace\\(PAD) laboratory\\Lab 5 PAD\\lab5pad\\src\\main\\resources\\db.json");
                InputStream inputStream = new FileInputStream(file);
                writer = new StringWriter();
                IOUtils.copy(inputStream, writer, "UTF-8");
                String jsonList = writer.toString();

                DataBase.getInstance().setEmpls((List<Empl>) JSONUtil.getJAVAObjectfromJSONStringList(jsonList, new TypeReference<List<Empl>>() {
                }));

                if (DataBase.getInstance().getEmpls().stream().filter(it -> it.getId().equals(empl.getId())).collect(Collectors.toList()).isEmpty()) {
                    response = format("Was successful uploaded employee with:\nFirst name=%s\nLast name=%s", empl.getFirstname(), empl.getLastname());
                    DataBase.getInstance().getEmpls().add(empl);
                    status = 200;

                    OutputStream output = new FileOutputStream(file);
                    output.write(JSONUtil.getJSONStringfromJAVAObject(DataBase.getInstance().getEmpls()).getBytes());
                    output.close();

                }

                httpExchange.sendResponseHeaders(status, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    ex.printStackTrace();
                    String msg = "SERVER ERROR";
                    httpExchange.sendResponseHeaders(500, msg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(msg.getBytes(), 0, msg.length());
                    os.close();
                } catch (Exception ex1) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static class PutHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                    httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
            try {
                InputStream is = httpExchange.getRequestBody();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String requestBody = writer.toString();

                Empl empl = (Empl) JSONUtil.getJAVAObjectfromJSONString(requestBody, Empl.class);
                String response = "Cannot update employee maybe is not exists";
                int status = 500;

                File file = new File("A:\\WorkSpace\\(PAD) laboratory\\Lab 5 PAD\\lab5pad\\src\\main\\resources\\db.json");
                InputStream inputStream = new FileInputStream(file);
                writer = new StringWriter();
                IOUtils.copy(inputStream, writer, "UTF-8");
                String jsonList = writer.toString();

                List<Empl> empls = (List<Empl>) JSONUtil.getJAVAObjectfromJSONStringList(jsonList, new TypeReference<List<Empl>>() {
                });

                for (Empl emp : empls) {
                    if (Objects.equals(emp.getId(), empl.getId())) {
                        emp.setFirstname(empl.getFirstname());
                        emp.setLastname(empl.getLastname());
                        emp.setAge(empl.getAge());
                        emp.setSalary(empl.getSalary());
                        emp.setCreatedDate(empl.getCreatedDate());
                        response = format("Was successful updated employee with:\nFirst name=%s\nLast name=%s", empl.getFirstname(), empl.getLastname());
                        status = 200;
                    }
                }

                OutputStream output = new FileOutputStream(file);
                output.write(JSONUtil.getJSONStringfromJAVAObject(empls).getBytes());
                output.close();

                httpExchange.sendResponseHeaders(status, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    ex.printStackTrace();
                    String msg = "SERVER ERROR";
                    httpExchange.sendResponseHeaders(500, msg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(msg.getBytes(), 0, msg.length());
                    os.close();
                } catch (Exception ex1) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static class GetHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                    httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
            try {
                Headers h = httpExchange.getResponseHeaders();
                h.add("Content-Type", "application/json");

                File file = new File("A:\\WorkSpace\\(PAD) laboratory\\Lab 5 PAD\\lab5pad\\src\\main\\resources\\db.json");
                byte[] bytearray = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytearray, 0, bytearray.length);

                httpExchange.sendResponseHeaders(200, file.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(bytearray, 0, bytearray.length);
                os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    ex.printStackTrace();
                    String msg = "SERVER ERROR";
                    httpExchange.sendResponseHeaders(500, msg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(msg.getBytes(), 0, msg.length());
                    os.close();
                } catch (Exception ex1) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static class GetByOffsetAndLimitHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                    httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
            try {
                Headers h = httpExchange.getResponseHeaders();
                h.add("Content-Type", "application/json");

                String query = httpExchange.getRequestURI().getQuery();

                String[] variables = query.split("&");

                Map<String, Integer> values = new HashMap();

                for (String variable : variables) {
                    String[] varAndValue = variable.split("=");
                    values.put(varAndValue[0], Integer.parseInt(varAndValue[1]));
                }

                File file = new File("A:\\WorkSpace\\(PAD) laboratory\\Lab 5 PAD\\lab5pad\\src\\main\\resources\\db.json");
                InputStream inputStream = new FileInputStream(file);
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStream, writer, "UTF-8");
                String jsonList = writer.toString();

                List<Empl> empls = (List<Empl>) JSONUtil.getJAVAObjectfromJSONStringList(jsonList, new TypeReference<List<Empl>>() {
                });

                if (values.size() == 1) {

                    Empl empl = new Empl();

                    empls.stream().filter(x -> x.getId().equals(values.get("id"))).forEach(x -> {
                        empl.setId(x.getId());
                        empl.setFirstname(x.getFirstname());
                        empl.setLastname(x.getLastname());
                        empl.setSalary(x.getSalary());
                        empl.setAge(x.getAge());
                        empl.setCreatedDate(x.getCreatedDate());
                    });

                    String jsonObject = JSONUtil.getJSONStringfromJAVAObject(empl);

                    httpExchange.sendResponseHeaders(200, jsonObject.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(jsonObject.getBytes(), 0, jsonObject.length());
                    os.close();
                } else {
                    List<Empl> sendEmpls = new ArrayList<>();
                    if (!empls.isEmpty()) {
                        IntStream.range(values.get("offset") - 1, values.get("limit")).forEach(value -> {
                            if (value >= 0 && value < empls.size()) {
                                sendEmpls.add(empls.get(value));
                            }
                        });
                    }

                    String jsonObjects = JSONUtil.getJSONStringfromJAVAObject(sendEmpls);
                    httpExchange.sendResponseHeaders(200, jsonObjects.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(jsonObjects.getBytes(), 0, jsonObjects.length());
                    os.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    ex.printStackTrace();
                    String msg = "SERVER ERROR";
                    httpExchange.sendResponseHeaders(500, msg.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(msg.getBytes(), 0, msg.length());
                    os.close();
                } catch (Exception ex1) {
                    ex.printStackTrace();
                }
            }

        }
    }


}