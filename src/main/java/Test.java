import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        createContext(server);
        server.setExecutor(null);
        server.start();
    }

    public static void createContext(HttpServer server) {
        for (Method m : Routes.class.getMethods()) {
            if (m.isAnnotationPresent(WebRoute.class)) {
                String path = m.getAnnotation(WebRoute.class).path();
                server.createContext(path, new myHandler(m));
            }
        }
    }

    static class myHandler implements HttpHandler {
        Method methodToInvoke;

        public myHandler(Method methodToInvoke) {
            this.methodToInvoke = methodToInvoke;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            try {
                String response = (String) methodToInvoke.invoke(Routes.class.newInstance());
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}

