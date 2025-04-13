package com.demo.tool;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider userTool1(UserTool userTool) {
        return MethodToolCallbackProvider.builder().toolObjects(userTool).build();
    }

    @Bean
    public ToolCallbackProvider projectTool1(ProjectTool projectTool) {
        return MethodToolCallbackProvider.builder().toolObjects(projectTool).build();
    }

    @Bean
    public ToolCallbackProvider projectScoreTool1(ProjectScoreTool projectScoreTool) {
        return MethodToolCallbackProvider.builder().toolObjects(projectScoreTool).build();
    }
}
