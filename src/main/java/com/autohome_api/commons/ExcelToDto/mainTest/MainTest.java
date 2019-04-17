package com.autohome_api.commons.ExcelToDto.mainTest;

import com.autohome_api.commons.BatchParameterNormalization;
import com.autohome_api.commons.ExcelToDto.dto.ExcelDto;
import com.autohome_api.commons.ExcelToDto.Utils.ExcelToDto;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class MainTest {
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, InvalidFormatException {
        File f = new File("E:\\upload\\zhb.xlsx");
        ExcelToDto e = new ExcelToDto();
        //System.out.println(e.readExcel(f, ExcelDto.class));
        List<ExcelDto> list = e.readExcel(f, ExcelDto.class);
        BatchParameterNormalization parameterNormalization = new BatchParameterNormalization();
        HashMap<String,String> map = parameterNormalization.paramOverWrite(list.get(0).getRequestBody());
        for(Map.Entry entry : map.entrySet()){
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        System.out.println(list.get(0));
    }
    public SortedMap get(){
        return null;
    }
}
