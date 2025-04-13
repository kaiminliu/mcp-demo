package com.demo;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class StdioTests {

    private static String jarFilePath;
    private McpSyncClient mcpClient;

    @BeforeAll
    public static void init() throws URISyntaxException {
        URL resource = StdioTests.class.getClassLoader().getResource("locate.properties"); // locate.properties是test resources下配置文件
        Assertions.assertNotNull(resource, "load junit classLoader context fail");

        Path targetPath = Paths.get(resource.toURI()).getParent().getParent();

        File files = targetPath.toFile();
        File[] files1 = files.listFiles(file -> (file.getName().endsWith(".jar")));

        Assertions.assertNotNull(files1, "Not validate directory");
        Assertions.assertNotEquals(0, files1.length, "Can not find the generated jar file under [target] directory");
        Assertions.assertEquals(1, files1.length, "Find more then one jar file under [target] directory");

        jarFilePath = files1[0].getAbsolutePath();
    }

    @BeforeEach
    public void initClient(){
        ServerParameters serverParameters = ServerParameters.builder("java")
                .args("-jar", "-Dfile.encoding=UTF-8", "-Dspring.ai.mcp.server.stdio=true", jarFilePath)
                .build();
        mcpClient = McpClient.sync(new StdioClientTransport(serverParameters)).build();

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
