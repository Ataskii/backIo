//package io.backQL.BackIo.report;
//
//<<<<<<< HEAD
//import io.backQL.BackIo.domain.Customer;
//import lombok.extern.slf4j.Slf4j;
////import org.apache.poi.xssf.usermodel.XSSFSheet;
////import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.util.List;
//
//@Slf4j
//public class CustomerReport {
////    private XSSFWorkbook xssfWorkbook;
////    private XSSFSheet;
////    private List<Customer>;
//=======
//
//import io.backQL.BackIo.domain.Customer;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.util.List;
//import java.util.stream.IntStream;
//
//public class CustomerReport {
//    private XSSFWorkbook workbook;
//    private XSSFSheet sheet;
//    private List<Customer> customers;
//    private static String[] HEADERS = { "ID","Name","Email","Type","Status","Address","Phone","Created_At"};
//    public CustomerReport(List<Customer> customers){
//        this.customers = customers;
//        workbook = new XSSFWorkbook();
//        sheet = workbook.createSheet("Customers");
//    }
//
//    public void setHeader(){
//        Row headerRow = sheet.createRow(0);
//        CellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeight(14);
//        style.setFont(font);
//        IntStream.range(0, HEADERS.length).forEach(index -> {
//            Cell cell = headerRow.createCell(index);
//            cell.setCellValue(HEADERS[index]);
//            cell.setCellStyle(style);
//        });
//    }
//>>>>>>> tail
//}
