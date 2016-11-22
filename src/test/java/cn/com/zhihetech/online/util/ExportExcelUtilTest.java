package cn.com.zhihetech.online.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileInputStream;


/**
 * Created by ShenYunjie on 2016/6/29.
 */
public class ExportExcelUtilTest {
    @Test
    public void readExcelData() throws Exception {
        Workbook workbook = new HSSFWorkbook(new FileInputStream("D:\\alipay.xls"));
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum() + 1;
        int startIndex = 5;
        int endIndex = rowCount - 7 - 1;
        int currentRowIndex = startIndex;
        while (currentRowIndex <= endIndex) {
            Row row = sheet.getRow(currentRowIndex);
            Cell codeCell = row.getCell(1);
            Cell priceCell = row.getCell(9);
            String code = codeCell.getStringCellValue();
            Double price = NumberUtils.doubleScale(2, priceCell.getNumericCellValue());
            System.out.println(code + " || " + price);
            currentRowIndex++;
        }
        workbook.close();
    }

    @Test
    public void readWXExcelData() throws Exception {
        Workbook workbook = new HSSFWorkbook(new FileInputStream("D:\\wx.xls"));
        Sheet sheet = workbook.getSheetAt(0);
        int startIndex = 5;
        int endIndex = sheet.getLastRowNum();
        int currentRowIndex = startIndex;
        while (currentRowIndex <= endIndex) {
            Row row = sheet.getRow(currentRowIndex);
            Cell codeCell = row.getCell(2);
            Cell priceCell = row.getCell(11);
            String code = codeCell.getStringCellValue();
            Double price = NumberUtils.doubleScale(2, priceCell.getNumericCellValue());
            System.out.println(code + " || " + price);
            currentRowIndex++;
        }
        workbook.close();
    }
}