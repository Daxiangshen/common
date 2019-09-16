package com.core.util;

import com.google.common.collect.Lists;
import com.core.annotation.ExcelField;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * Export the Excel file
 */
public class ExportExcel {

    private static Logger log = LoggerFactory.getLogger(com.core.util.ExportExcel.class);

    private SXSSFWorkbook wb;
    private Sheet sheet;
    private Map<String, CellStyle> styles;
    private String sheetName;

    /**
     * current row number
     */
    private int rowNum;

    /**
     * Object[]{ ExcelField, Field/Method }
     */
    private List<Object[]> annotationList = Lists.newArrayList();

    /**
     * Constructor
     *
     * @param cls The entity object gets the title by annotation.ExportField
     */
    public ExportExcel(Class<?> cls) {
        this(null, cls, 1);
    }

    /**
     * Constructor
     *
     * @param sheetName sheet name
     * @param cls       The entity object gets the title by annotation.ExportField
     */
    public ExportExcel(String sheetName, Class<?> cls) {
        this(sheetName, cls, 1);
    }

    private List<String> class2List(Class<?> cls, int type, int... groups) {
        annotationList.clear();
        // Get annotation field
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == type || ef.type() == 0)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int g : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int efg : ef.groups()) {
                            if (efg == g) {
                                annotationList.add(new Object[]{ef, f});
                                inGroup = true;
                                break;
                            }
                        }
                    }
                } else {
                    annotationList.add(new Object[]{ef, f});
                }
            }
        }
        // Get annotation method
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            ExcelField ef = m.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == type)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int g : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int efg : ef.groups()) {
                            if (g == efg) {
                                inGroup = true;
                                annotationList.add(new Object[]{ef, m});
                                break;
                            }
                        }
                    }
                } else {
                    annotationList.add(new Object[]{ef, m});
                }
            }
        }
        // Field sorting
        annotationList.sort(Comparator.comparing(o -> ((ExcelField) o[0]).sort()));
        // Initialize
        List<String> headerList = Lists.newArrayList();
        for (Object[] os : annotationList) {
            String t = ((ExcelField) os[0]).title();
            if (type == 1) {
                String[] ss = StringUtils.split(t, "**", 2);
                if (ss.length == 2) {
                    t = ss[0];
                }
            }
            headerList.add(t);
        }
        return headerList;
    }

    /**
     * Constructor
     *
     * @param sheetName sheet name
     * @param cls       The entity object gets the title by annotation.ExportField
     * @param type      (1:export data；2:export template)
     * @param groups
     */
    public ExportExcel(String sheetName, Class<?> cls, int type, int... groups) {
        this.sheetName = sheetName;
        List<String> headerList = this.class2List(cls, type, groups);
        initialize(sheetName, headerList);
    }

    /**
     * Constructor
     *
     * @param sheetName sheetName
     * @param headers
     */
    public ExportExcel(String sheetName, String[] headers) {
        this.sheetName = sheetName;
        initialize(sheetName, Lists.newArrayList(headers));
    }

    /**
     * Constructor
     *
     * @param sheetName  sheetName
     * @param headerList 表头列表
     */
    public ExportExcel(String sheetName, List<String> headerList) {
        this.sheetName = sheetName;
        initialize(sheetName, headerList);
    }

    /**
     * Initialization function
     *
     * @param sheetName  sheetName
     *                   title)
     * @param headerList
     */
    private void initialize(String sheetName, List<String> headerList) {
        this.wb = new SXSSFWorkbook(500);
        if (StringUtils.isNotBlank(sheetName)) {
            this.sheet = wb.createSheet(sheetName);
        }
        this.styles = createStyles(wb);

        // Create header
        if (headerList == null) {
            throw new RuntimeException("no header error");
        }
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(16);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = this.sheet.createDrawingPatriarch()
                        .createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
            // sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        log.debug("Initialize success.");
    }


    /**
     * Initialization function
     *
     * @param sheetName sheetName
     *                  title)
     * @param cls
     */
    public void addSheet(String sheetName, Class<?> cls) {
        rowNum = 0;

        List<String> headerList = this.class2List(cls, 1);
        if (StringUtils.isNotBlank(sheetName)) {
            this.sheet = wb.createSheet(sheetName);
        }
        this.styles = createStyles(wb);
        // Create header
        if (headerList == null) {
            throw new RuntimeException("no header error");
        }
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(16);
        for (int j = 0; j < headerList.size(); j++) {
            Cell cell = headerRow.createCell(j);
            cell.setCellStyle(styles.get("header"));
            String[] ss = StringUtils.split(headerList.get(j), "**", 2);
            if (ss.length == 2) {
                Comment comment = this.sheet.createDrawingPatriarch()
                        .createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellValue(ss[0]);
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(j));
            }
            // sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        log.debug("Initialize success.");
    }

    /**
     * create table style
     *
     * @param wb
     * @return style map
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        styles.put("data3", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        // style.setWrapText(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * add row
     *
     * @return row
     */
    public Row addRow(String sheetName) {
        sheet = this.wb.getSheet(sheetName);
        return sheet.createRow(rowNum++);
    }

    /**
     * add cell
     *
     * @param row
     * @param column
     * @param val
     * @return cell
     */
    public Cell addCell(Row row, int column, Object val) {
        return this.addCell(row, column, val, 0, Class.class);
    }

    /**
     * add cell
     *
     * @param row
     * @param column
     * @param val
     * @param align  (1:left 2:center 3:right)
     * @return cell
     */
    public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType) {
        Cell cell = row.createCell(column);
        String cellFormatString = "@";
        try {
            if (val == null) {
                cell.setCellValue("");
            } else if (fieldType != Class.class) {
                cell.setCellValue((String) fieldType.getMethod("setValue", Object.class).invoke(null, val));
            } else {
                if (val instanceof String) {
                    cell.setCellValue((String) val);
                } else if (val instanceof Integer) {
                    cell.setCellValue((Integer) val);
                    cellFormatString = "0";
                } else if (val instanceof Long) {
                    cell.setCellValue((Long) val);
                    cellFormatString = "0";
                } else if (val instanceof Double) {
                    cell.setCellValue((Double) val);
                    cellFormatString = "0.00";
                } else if (val instanceof Float) {
                    cell.setCellValue((Float) val);
                    cellFormatString = "0.00";
                } else if (val instanceof Date) {
                    cell.setCellValue((Date) val);
                    cellFormatString = "yyyy-MM-dd HH:mm:ss";
                } else if (val instanceof BigDecimal) {
                    cell.setCellValue(((BigDecimal) val).doubleValue());
                    cellFormatString = "0.00";
                } else {
                    cell.setCellValue((String) Class
                            .forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
                                    "fieldtype." + val.getClass().getSimpleName() + "Type"))
                            .getMethod("setValue", Object.class).invoke(null, val));
                }
            }
            CellStyle style = styles.get("data_column_" + column);
            if (style == null) {
                style = wb.createCellStyle();
                style.cloneStyleFrom(styles.get("data" + (align >= 1 && align <= 3 ? align : "")));
                style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
                styles.put("data_column_" + column, style);
            }
            cell.setCellStyle(style);
        } catch (Exception ex) {
            assert val != null;
            log.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
            cell.setCellValue(val.toString());
        }
        return cell;
    }

    /**
     * add data
     *
     * @return list
     */
    public <E> ExportExcel setDataList(List<E> list) {
        return setDataList(this.sheetName, list);
    }

    /**
     * add data
     *
     * @return list
     */
    public <E> ExportExcel setDataList(String sheetName, List<E> list) {
        for (E e : list) {
            int colunm = 0;
            Row row = this.addRow(sheetName);
            StringBuilder sb = new StringBuilder();
            for (Object[] os : annotationList) {
                ExcelField ef = (ExcelField) os[0];
                Object val = null;
                // Get entity value
                try {
                    if (StringUtils.isNotBlank(ef.value())) {
                        val = Reflections.invokeGetter(e, ef.value());
                    } else {
                        if (os[1] instanceof Field) {
                            val = Reflections.invokeGetter(e, ((Field) os[1]).getName());
                        } else if (os[1] instanceof Method) {
                            val = Reflections.invokeMethod(e, ((Method) os[1]).getName(), new Class[]{},
                                    new Object[]{});
                        }
                    }
                    // If is dict, get dict label
                    if (StringUtils.isNotBlank(ef.dictType())) {
                        // val = DictUtils.getDictLabel(val==null?"":val.toString(), ef.dictType(), "");
                    }
                } catch (Exception ex) {
                    // Failure to ignore
                    log.info(ex.toString());
                    val = "";
                }
                this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
                sb.append(val + ", ");
            }
            log.debug("Write success: [" + row.getRowNum() + "] " + sb.toString());
        }
        return this;
    }

    /**
     * Output data stream
     *
     * @param os
     */
    public ExportExcel write(OutputStream os) throws IOException {
        wb.write(os);
        return this;
    }

    /**
     * Output to client
     *
     * @param fileName
     */
    public ExportExcel write(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        write(response.getOutputStream());
        return this;
    }

    /**
     * Output to file
     *
     * @param name
     */
    public ExportExcel writeFile(String name) throws FileNotFoundException, IOException {
        FileOutputStream os = new FileOutputStream(name);
        this.write(os);
        return this;
    }

    /**
     * Clear temporary files
     */
    public ExportExcel dispose() {
        wb.dispose();
        return this;
    }

}
