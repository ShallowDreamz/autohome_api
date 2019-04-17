package com.autohome_api.commons.ExcelToDto.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtils {
    public static void intToLetter(int num, StringBuffer letter) {
        int z = num / 26;
        int y = num % 26;
        if (y == 0) {
            y = 26;
            z--;
        }
        if (z > 26) {
            letter.insert(0, (char) (y + 64));
            intToLetter(z, letter);
        } else {
            letter.insert(0, (char) (y + 64));
            if (z > 0) {
                letter.insert(0, (char) (z + 64));
            }
        }
    }

    public static int letterToInt(String letter) {
        int length = letter.length();
        int result = 0;
        for (int i = 0; i < length; i++) {
            int num = letter.charAt(i) - 64;
            result += num * Math.pow(26, length - i - 1);
        }
        return result;
    }


    public static int letterToRow(String letter) {
        int i = 0;
        while (letter.charAt(i) <= 'Z' && letter.charAt(i) >= 'A') {
            i++;
        }
        int row = Integer.parseInt(StringUtils.substring(
                letter, i, letter.length()));
        return row;
    }

    public static int letterToCol(String letter) {
        int i = 0;
        while (letter.charAt(i) <= 'Z' && letter.charAt(i) >= 'A') {
            i++;
        }
        int col = letterToInt(StringUtils.substring(
                letter, 0, i));
        return col;
    }

    public static Map<String, Integer> lettrToCell(String letter) {
        int i = 0;
        while (letter.charAt(i) <= 'Z' && letter.charAt(i) >= 'A') {
            i++;
        }
        int col = letterToInt(StringUtils.substring(
                letter, 0, i));
        int row = Integer.parseInt(StringUtils.substring(
                letter, i, letter.length()));
        Map<String, Integer> cellInfo = new HashMap<String, Integer>();
        cellInfo.put("row", row);
        cellInfo.put("col", col);
        return cellInfo;
    }

    public static String cellToLettr(int row, int col) {
        StringBuffer lettr = new StringBuffer("");
        ExcelUtils.intToLetter(col, lettr);
        return lettr + String.valueOf(row);

    }

    public static String addRow(String letter, int add) {
        int i = 0;
        while (letter.charAt(i) <= 'Z' && letter.charAt(i) >= 'A') {
            i++;
        }
        int row = Integer.parseInt(StringUtils.substring(
                letter, i, letter.length()));
        return StringUtils.substring(
                letter, 0, i) + (row + add);
    }

    public static String addCol(String letter, int add) {
        int i = 0;
        while (letter.charAt(i) <= 'Z' && letter.charAt(i) >= 'A') {
            i++;
        }
        int col = letterToInt(StringUtils.substring(
                letter, 0, i));
        int row = Integer.parseInt(StringUtils.substring(
                letter, i, letter.length()));
        StringBuffer cm = new StringBuffer("");
        intToLetter(col + add, cm);
        return cm.toString() + row;
    }

    public static Map<String, Integer> addRowLetter(String letter, int add) {
        int i = 0;
        while (letter.charAt(i) <= 'Z' && letter.charAt(i) >= 'A') {
            i++;
        }
        int row = Integer.parseInt(StringUtils.substring(
                letter, i, letter.length()));
        return lettrToCell(StringUtils.substring(
                letter, 0, i) + (row + add));
    }

    public static String addRow(int col, int row, int add) {
        StringBuffer cm = new StringBuffer("");
        intToLetter(col, cm);
        return cm.append((row + add)).toString();
    }

    // 获取单元格内容
    public static String getCellValue(Cell cell) {
        if(cell == null) return "";
        String cellValue = "";

        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_NUMERIC: // 数值型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是date类型则 ，获取该cell的date值
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {// 纯数字
                    DecimalFormat df = new DecimalFormat("0.#######");
                    cellValue = df.format(cell.getNumericCellValue());
                }
                break;
            /* 此行表示单元格的内容为string类型 */
            case Cell.CELL_TYPE_STRING: // 字符串型
                cellValue = cell.getStringCellValue().trim();
                break;
            case Cell.CELL_TYPE_FORMULA:// 公式型
                // 读公式计算值
                try {
                    try {
                        DecimalFormat df = new DecimalFormat("0.#######");
                        cellValue = df.format(cell.getNumericCellValue());
                    }catch (Exception e){
                        cellValue = String.valueOf(cell.getNumericCellValue()).trim();
                    }
                } catch (IllegalStateException e) {
                    return "";
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:// 布尔
                cellValue = "" + cell.getBooleanCellValue();
                break;
            /* 此行表示该单元格值为空 */
            case Cell.CELL_TYPE_BLANK: // 空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                cellValue = "";
                break;
            default:
                cellValue = cell.getStringCellValue().toString().trim();
        }

        return cellValue;
    }

}
