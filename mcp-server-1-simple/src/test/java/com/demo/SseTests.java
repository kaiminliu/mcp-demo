package com.demo;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SseTests {

    private static String host;
    private static int port;
    private McpSyncClient mcpClient;
    private static Process startedProcess;

    @BeforeAll
    public static void init() throws URISyntaxException, IOException {
        // get test properties
        URL resource = SseTests.class.getClassLoader().getResource("locate.properties");
        Assertions.assertNotNull(resource, "load junit classLoader context fail");

        // get init port
        Properties properties = new Properties();
        properties.load(resource.openStream());
        port = Integer.parseInt(properties.getProperty("port", "8080"));
        host = properties.getProperty("host", "localhost");

        // find jar file
        Path targetPath = Paths.get(resource.toURI()).getParent().getParent();

        File files = targetPath.toFile();
        File[] files1 = files.listFiles(file -> (file.getName().endsWith(".jar")));

        // start the jar if not running.
        try (Socket ignored = new Socket(host, port)) {
            System.out.println("port " + port + " at host " + "localhost" + " is open。");
        } catch (IOException e) {
            // Check if not started.
            Assertions.assertNotNull(files1, "Not validate directory");
            Assertions.assertNotEquals(0, files1.length, "Can not find the generated jar file under [target] directory");
            Assertions.assertEquals(1, files1.length, "Find more then one jar file under [target] directory");

            String jarFilePath = files1[0].getAbsolutePath();

            System.out.println("port " + port + " at host " + "localhost" + "  is close or unreachable。opening...");
            ProcessBuilder process = new ProcessBuilder("java", "-jar", jarFilePath);
            startedProcess = process.start();
            System.out.println("service opened.");
        }

        // make sure program started
        Assertions.assertDoesNotThrow(() -> TimeUnit.SECONDS.sleep(5));

    }

    @AfterAll
    public static void destroy() {
        if (startedProcess != null) {
            startedProcess.destroy();
        }
    }

    @BeforeEach
    public void initClient() {
        mcpClient = McpClient.sync(new HttpClientSseClientTransport("http://" + host + ":" + port)).build();

        // 验证客户端是否正确初始化
        try {
            mcpClient.initialize();
            System.out.println(mcpClient.listTools());
        } catch (Exception e) {
            throw new RuntimeException("MCP客户端初始化失败", e);
        }
    }

    @AfterEach
    public void close() {
        if (mcpClient != null) {
            mcpClient.closeGracefully();
        }
    }

    @Test
    public void getWeather() {
        McpSchema.CallToolResult callToolResult = quickCall("getWeatherByCity", Map.of("cityName", "北京"));
        System.out.println(callToolResult);
    }


    private McpSchema.CallToolResult quickCall(String name, Map<String, Object> arguments) {
        return mcpClient.callTool(new McpSchema.CallToolRequest(name,arguments));
    }
}
