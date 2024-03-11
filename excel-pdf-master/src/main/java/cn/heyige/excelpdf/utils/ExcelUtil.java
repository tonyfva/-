package cn.heyige.excelpdf.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;

/**
 * excel操作工具类
 *
 * @author Administrator
 * @date 2023/09/22
 */
@Slf4j
public class ExcelUtil {

    public static <T> List<T> getExcelModelData(final InputStream inputStream, Class<T> clazz) {
        if (null == inputStream) {
            throw new NullPointerException("the inputStream is null!");
        }
        ExcelReaderBuilder result = EasyExcel.read(inputStream, clazz, null);
        ExcelReaderSheetBuilder sheet1 = result.sheet();
        return sheet1.doReadSync();
    }
}
