package org.mcp.tinyurl;


import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class McpJavaApplicationTests {

    @Test
    void contextLoads() {
        // 这里直接指定我们刚mcp server打包的jar的位置
        var stdioParams = ServerParameters.builder("java")
                .args("-jar", "D:/01_workspace/00_data_ai/tinyurl-mcp-java/target/tinyurl-mcp-java-0.0.1-SNAPSHOT.jar")
                .build();
        var stdioTransport = new StdioClientTransport(stdioParams);
        var mcpClient = McpClient.sync(stdioTransport).build();
        mcpClient.initialize();

        McpSchema.ListToolsResult toolsList = mcpClient.listTools();
        System.out.println("MCP tools集合：" + toolsList.tools());

        // 这里随机给一些经纬度参数
        McpSchema.CallToolResult weather = mcpClient.callTool(
                new McpSchema.CallToolRequest("getWeatherForecastByLocation",
                        Map.of("latitude", "47.6062", "longitude", "-122.3321")));
        System.out.println("根据经纬度查询天气信息：" + weather.content());

        McpSchema.CallToolResult alert = mcpClient.callTool(
                new McpSchema.CallToolRequest("getAlerts", Map.of("state", "NY")));
        System.out.println("获取天气状态信息：" + alert.content());
        mcpClient.closeGracefully();
    }

}

