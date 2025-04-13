package com.demo.util;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelUtil {
    
    public static <T> void exportExcel(HttpServletResponse response, OutputStream outputStream, List<T> dataList, String fileName) throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("数据列表不能为空");
        }
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        T firstData = dataList.get(0);
        Field[] fields = firstData.getClass().getDeclaredFields();
        
        for (int i = 0; i < fields.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fields[i].getName());
        }
        
        // 填充数据
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            T data = dataList.get(i);
            
            for (int j = 0; j < fields.length; j++) {
                Cell cell = row.createCell(j);
                Field field = fields[j];
                field.setAccessible(true);
                
                try {
                    Object value = field.get(data);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                } catch (IllegalAccessException e) {
                    cell.setCellValue("");
                }
            }
        }


        if (outputStream == null) {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");
            outputStream = response.getOutputStream();
        }

        // 写入响应流
        workbook.write(outputStream);
        workbook.close();
    }
} 