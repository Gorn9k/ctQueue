package VSTU.ctQueue.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.TempFile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.entity.Reservation;

@Service
public class ReportService {

    @Autowired
    private ReservationService reservationService;

    private static final int FONT_HEIGHT = 12;

    private static final String FONT_NAME = "Times New Roman";

    private static final int HEADER_END = 3;

    private static final int COLUMN_COUNT = 6;

    /**
     * ��������� ������ �� ������������ ����.
     * 
     * @param date ���� (yyyy-MM-dd) �� ������� ������������ �����
     * @return {@link File} <b>���������</b> ���� ������
     * @throws IOException
     * @throws ParseException
     */
    public File generate(final Date date) throws IOException, ParseException {
        File tempFile = TempFile.createTempFile("poi-sxssf-template", ".xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        generateHeader(date, workbook, sheet);
        generateBody(date, workbook, sheet);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            workbook.write(fos);
        }
        return tempFile;
    }

    /**
     * ��������� ���������� ������: id, ���, ������� �����������.
     * 
     * @param date     ���� (yyyy-MM-dd) �� ������� ������������ �����
     * @param workbook ��������, � ������� ���� ��������� ������
     * @param sheet    �������� �� ������� ���� ��������� ������
     */
    private void generateBody(final Date date, final XSSFWorkbook workbook, final XSSFSheet sheet) {
        XSSFCell cell = null;
        int rowEnd = HEADER_END;
        for (Reservation reservation : reservationService.findByDate(date)) {
            List<XSSFRow> rows = new ArrayList<>();
            int mainCounter = reservationService.getMainCounter(reservation),
                    reserveCounter = reservationService.getReserveCounter(reservation),
                    maxLenght = mainCounter > reserveCounter ? mainCounter : reserveCounter;
            for (int i = rowEnd; i <= rowEnd + maxLenght; i++) {
                XSSFRow row = sheet.createRow(i);
                for (int j = 0; j <= 6; j++) {
                    XSSFCell initCell = row.createCell(j);
                    initCell.setCellStyle(createStyle(workbook));
                    CellUtil.setAlignment(initCell, HorizontalAlignment.CENTER);
                }
                rows.add(row);
            }

            cell = rows.get(0).getCell(0);
            cell.setCellValue(new SimpleDateFormat("HH:mm").format(reservation.getReservationDate()));
            cell.setCellStyle(createStyle(workbook));
            CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
            CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
            if (maxLenght > 1)
                sheet.addMergedRegion(new CellRangeAddress(rowEnd, rowEnd + maxLenght - 1, 0, 0));
            rowEnd = rowEnd + maxLenght;
            for (Entrant entrant : reservation.getEntrants()) {
                if (entrant.isMain() && entrant.getActivationCode() == null) {
                    for (XSSFRow row : rows) {
                        if (row.getCell(2).getStringCellValue().isEmpty()) {
                            row.getCell(1).setCellValue(entrant.getId());
                            row.getCell(2).setCellValue(String.format("%s %s %s", entrant.getSurname(),
                                    entrant.getFirstname(), entrant.getPatronymic()));
                            row.getCell(3).setCellValue(String.valueOf(entrant.getPhone()));
                            break;
                        }
                    }
                } else if (entrant.isReserve()) {
                    for (XSSFRow row : rows) {
                        if (row.getCell(5).getStringCellValue().isEmpty()) {
                            row.getCell(4).setCellValue(entrant.getId());
                            row.getCell(5).setCellValue(String.format("%s %s %s", entrant.getSurname(),
                                    entrant.getFirstname(), entrant.getPatronymic()));
                            row.getCell(6).setCellValue(String.valueOf(entrant.getPhone()));
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i <= COLUMN_COUNT; i++)
            sheet.autoSizeColumn(i);
    }

    /**
     * ��������� ��������� ������� ������.
     * 
     * @param date     ���� (yyyy-MM-dd) �� ������� ������������ �����
     * @param workbook ��������, � ������� ���� ��������� ������
     * @param sheet    �������� �� ������� ���� ��������� ������
     * @throws ParseException
     */
    private void generateHeader(final Date date, final XSSFWorkbook workbook, final XSSFSheet sheet)
            throws ParseException {
        XSSFCell cell = null;
        XSSFRow row = null;
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 6; j++) {
                if (i == 0 && j == 0) {
                    cell = sheet.createRow(i).createCell(i);
                    cell.setCellValue(new SimpleDateFormat("dd.MM.yyyy").format(date));
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                }
                if (i == 1 && j == 0) {
                    row = sheet.createRow(i);
                    cell = row.createCell(j);
                    cell.setCellValue("\u0412\u0440\u0435\u043C\u044F");
                    sheet.addMergedRegion(new CellRangeAddress(i, i + 1, 0, 0));
                }
                if (i == 1 && j == 1) {
                    cell = row.createCell(j);
                    cell.setCellValue(
                            "\u042D\u043B\u0435\u043A\u0442\u0440\u043E\u043D\u043D\u0430\u044F\u0020\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044F");
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
                }
                if (i == 1 && j == 4) {
                    cell = row.createCell(j);
                    cell.setCellValue(
                            "\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044F\u0020\u043E\u043F\u0435\u0440\u0430\u0442\u043E\u0440\u043E\u043C");
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 6));
                }
                if (i == 2 && j == 0)
                    row = sheet.createRow(i);
                if (i == 2 && (j == 1 || j == 4)) {
                    cell = row.createCell(j);
                    cell.setCellValue("id");
                }
                if (i == 2 && (j == 2 || j == 5)) {
                    cell = row.createCell(j);
                    cell.setCellValue("\u0424\u0418\u041E");

                }
                if (i == 2 && (j == 3 || j == 6)) {
                    cell = row.createCell(j);
                    cell.setCellValue("\u0422\u0435\u043B\u0435\u0444\u043E\u043D");
                }
                cell.setCellStyle(createStyle(workbook));
                CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
            }
        }
    }

    /**
     * ����� ������, ���������� � �������.
     * 
     * @param workbook ��������, � ������� ���� ��������� ������
     * @return {@link XSSFCellStyle} ������ � ����������� ������� ������
     */
    private XSSFCellStyle createStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        XSSFCellStyle style = workbook.createCellStyle();
        font.setFontHeightInPoints((short) FONT_HEIGHT);
        font.setFamily(FontFamily.ROMAN);
        font.setFontName(FONT_NAME);
        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

}
