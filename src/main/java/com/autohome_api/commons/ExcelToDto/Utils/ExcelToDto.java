package com.autohome_api.commons.ExcelToDto.Utils;

import com.autohome_api.commons.ExcelToDto.ExcelInterface.ExcelColunm;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class ExcelToDto {

    public static <T> List<T> readExcel(File file, Class<T> cls) throws IllegalAccessException, InstantiationException, IOException, InvalidFormatException {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
        if (Objects.equals("xls", extension) || Objects.equals("xlsx", extension)) {
            return readExcel(WorkbookFactory.create(file), cls);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    private static <T> List<T> readExcel(Workbook workbook, Class<T> cls) throws IllegalAccessException, InstantiationException {
        Sheet sheet = workbook.getSheetAt(0);
        Row firstRow = sheet.getRow(0);
        List<Field> list = Arrays.asList(cls.getDeclaredFields());
        Map<String, Integer> pojoMapping = new HashMap<>();

        for (int i = 0; i < firstRow.getPhysicalNumberOfCells(); i++) {
            String headerCell = ExcelUtils.getCellValue(firstRow.getCell(i));
            if (StringUtils.isNotBlank(headerCell)) {
                if(!pojoMapping.containsKey(headerCell)) pojoMapping.put(headerCell, i);
            }
        }
        List<T> resList = new ArrayList<>();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            T obj = null;
            for (Field f : list) {
                if (f.isAnnotationPresent(ExcelColunm.class) && pojoMapping.containsKey(f.getDeclaredAnnotation(ExcelColunm.class).colName())) {
                    if (obj == null) obj = cls.newInstance();
                    f.setAccessible(true);
                    f.set(obj, ExcelUtils.getCellValue(row.getCell(pojoMapping.get(f.getDeclaredAnnotation(ExcelColunm.class).colName()))));
                }
            }
            if (obj != null) resList.add(obj);
        }
        return resList;
    }

}
