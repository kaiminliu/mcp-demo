package com.demo.servcie;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private final RestClient restClient;
    private final Map<String, String> cityCoordinates;

    public WeatherService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.weather.gov")
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
                .build();

        // 初始化城市坐标映射
        this.cityCoordinates = new HashMap<>();
        cityCoordinates.put("北京", "39.9042,-116.4074");
        cityCoordinates.put("上海", "31.2304,121.4737");
        cityCoordinates.put("广州", "23.1291,113.2644");
        cityCoordinates.put("深圳", "22.5431,114.0579");
        // 可以添加更多城市坐标
    }

    @Tool(name = "getWeatherByCity", description = "根据城市名称获取天气信息，支持中英文城市名称")
    public String getWeatherByCity(@ToolParam(description = "城市名称") String cityName) {
        cityName = cityName.trim();
        try {
            // 获取城市坐标
            String coordinates = cityCoordinates.get(cityName);
            if (coordinates == null) {
                return "暂不支持该城市的天气查询";
            }

            // 获取网格点信息
            JsonNode pointsResponse = restClient.get()
                    .uri("/points/{coordinates}", coordinates)
                    .retrieve()
                    .body(JsonNode.class);

            if (pointsResponse == null) {
                return "无法获取天气信息";
            }

            // 获取预报URL
            String forecastUrl = pointsResponse.path("properties").path("forecast").asText();

            // 获取天气预报
            JsonNode forecastResponse = restClient.get()
                    .uri(forecastUrl)
                    .retrieve()
                    .body(JsonNode.class);

            if (forecastResponse != null) {
                JsonNode periods = forecastResponse.path("properties").path("periods");
                if (periods.isArray() && periods.size() > 0) {
                    JsonNode currentPeriod = periods.get(0);
                    String name = currentPeriod.path("name").asText();
                    String detailedForecast = currentPeriod.path("detailedForecast").asText();
                    String temperature = currentPeriod.path("temperature").asText();
                    String temperatureUnit = currentPeriod.path("temperatureUnit").asText();
                    String windSpeed = currentPeriod.path("windSpeed").asText();
                    String windDirection = currentPeriod.path("windDirection").asText();

                    return String.format("%s的天气情况：%s\n温度：%s%s\n风速：%s，风向：%s",
                        cityName, detailedForecast, temperature, temperatureUnit, windSpeed, windDirection);
                }
            }
            return "无法获取天气信息";
        } catch (Exception e) {
            return "获取天气信息失败：" + e.getMessage();
        }
    }

    @Tool(name = "getWeatherByLocation", description = "根据经纬度获取天气信息")
    public String getWeatherByLocation(@ToolParam(description = "纬度") double lat, @ToolParam(description = "经度") double lon) {
        try {
            // 获取网格点信息
            JsonNode pointsResponse = restClient.get()
                    .uri("/points/{lat},{lon}", lat, lon)
                    .retrieve()
                    .body(JsonNode.class);

            if (pointsResponse == null) {
                return "无法获取天气信息";
            }

            // 获取预报URL
            String forecastUrl = pointsResponse.path("properties").path("forecast").asText();

            // 获取天气预报
            JsonNode forecastResponse = restClient.get()
                    .uri(forecastUrl)
                    .retrieve()
                    .body(JsonNode.class);

            if (forecastResponse != null) {
                JsonNode periods = forecastResponse.path("properties").path("periods");
                if (periods.isArray() && periods.size() > 0) {
                    JsonNode currentPeriod = periods.get(0);
                    String name = currentPeriod.path("name").asText();
                    String detailedForecast = currentPeriod.path("detailedForecast").asText();
                    String temperature = currentPeriod.path("temperature").asText();
                    String temperatureUnit = currentPeriod.path("temperatureUnit").asText();
                    String windSpeed = currentPeriod.path("windSpeed").asText();
                    String windDirection = currentPeriod.path("windDirection").asText();

                    return String.format("当前位置的天气情况：%s\n温度：%s%s\n风速：%s，风向：%s",
                        detailedForecast, temperature, temperatureUnit, windSpeed, windDirection);
                }
            }
            return "无法获取天气信息";
        } catch (Exception e) {
            return "获取天气信息失败：" + e.getMessage();
        }
    }
}
