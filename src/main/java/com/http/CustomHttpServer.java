package com.http;

import com.database.DataBase;
import com.model.Empl;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.util.JSONUtil;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CustomHttpServer {

    public void lol() {
        //        Model model = new Model();
        //        model.setNodeId("asd");
        //        String json = JSONUtil.getJSONStringfromJAVAObject(model);
        //        System.out.println(json);
        //        Model model2 = (Model)JSONUtil.getJAVAObjectfromJSONString(json,Model.class);
        //        System.out.println(model2.getNodeId());
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
        server.createContext("/employee/post", new PostHandler());
        server.createContext("/employee/get", new GetHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("asd");
    }

    private static class PostHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            try {
                InputStream is = httpExchange.getRequestBody();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String requestBody = writer.toString();

                Empl empl = (Empl)JSONUtil.getJAVAObjectfromJSONString(requestBody,Empl.class);
                String response = "This id is used";
                int status = 500;

                if(DataBase.getInstance().getEmpls().stream().filter(it -> it.getId().equals(empl.getId())).collect(Collectors.toList()).isEmpty()){
                    response = String.format("Was successful uploaded employee with:\nFirst name=%s\nLast name=%s",empl.getFirstname(), empl.getLastname());
                    DataBase.getInstance().getEmpls().add(empl);
                    status = 200;

                    File file = new File("/home/win/Workspace/Untitled Folder/src/main/resources/db.json");
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
            }
        }

    }

    private static class GetHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            try {
                Headers h = httpExchange.getResponseHeaders();
                h.add("Content-Type", "application/json");

                File file = new File("/home/win/Workspace/Untitled Folder/src/main/resources/db.json");
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
            }
        }
    }
}