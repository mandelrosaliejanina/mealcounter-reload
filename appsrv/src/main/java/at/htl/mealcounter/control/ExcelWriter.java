//package at.htl.mealcounter.control;
//
//import at.htl.mealcounter.entity.Consumation;
//import at.htl.mealcounter.entity.Person;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//@ApplicationScoped
//public class ExcelWriter {
//
//
//    @Inject
//    PersonRepository personRepository;
//
//    @Inject
//    ConsumationRepository consumationRepository;
//
//    public void writeExcel() throws IOException {
//
//        Person person = personRepository.findById(1L);
//
//        System.out.println(person.toString());
//
//        Workbook workbook = new XSSFWorkbook();
//
//        Sheet sheet = workbook.createSheet("Persons");
//        sheet.setColumnWidth(0, 6000);
//        sheet.setColumnWidth(1, 4000);
//
//        Row header = sheet.createRow(0);
//
//        CellStyle headerStyle = workbook.createCellStyle();
//        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 16);
//        font.setBold(true);
//        headerStyle.setFont(font);
//
//        // Abrechnungszeitraum
//
//        Cell headerCell = header.createCell(0);
//        headerCell.setCellValue("Abrechnungszeitraum: ");
//
//        headerCell = header.createCell(1);
//        Calendar cal = Calendar.getInstance();
//        headerCell.setCellValue(new SimpleDateFormat("MMM").format(cal.getTime()) + " " + Calendar.getInstance().get(Calendar.YEAR));
//
//
//        // Klasse
//
//        Row row = sheet.createRow(1);
//        Cell cell = row.createCell(0);
//        cell.setCellValue("Klasse: ");
//
//        cell = row.createCell(1);
//        String classname = "klasse";
//        cell.setCellValue(classname);
//
//        // Preis
//
//        Row rowPrice = sheet.createRow(2);
//        Cell cellPrice = rowPrice.createCell(0);
//        cellPrice.setCellValue("Preis eines Menüs: ");
//
//        double preisMenu = 5;
//        cellPrice = rowPrice.createCell(1);
//        cellPrice.setCellValue(preisMenu);
//
//        // Datums Ausgaben
//
//        List<LocalDate> datesUntilList = new ArrayList<>();
//        Row rowOutput = sheet.createRow(3);
//        Cell cellOutput;
//
//        LocalDate startDate = LocalDate.of(2021, LocalDate.now().getMonth(), 1);
//        LocalDate endDate = startDate.plusMonths(1);
//
//
//        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
//        List<LocalDate> datesUntil = IntStream.iterate(0, i -> i + 1)
//                .limit(numOfDaysBetween)
//                .mapToObj(i -> startDate.plusDays(i))
//                .collect(Collectors.toList());
//
//        for (int i = 0; i < datesUntil.size(); i++) {
//
//            cellOutput = rowOutput.createCell(i+2);
//            cellOutput.setCellValue(datesUntil.get(i).toString());
//            datesUntilList.add(datesUntil.get(i));
//
//        }
//
//
//        // Anzahl der Menüs und Betrag
//
//        cellOutput = rowOutput.createCell(datesUntil.size()+1);
//        cellOutput.setCellValue("Anzahl der Menüs");
//        cellOutput = rowOutput.createCell(datesUntil.size()+2);
//        cellOutput.setCellValue("Betrag");
//
//        //
//
//        Row rowPerson;
//        Cell cellPerson;
//        int countMenu;
//        int[] countMeal = new int[personRepository.findAll().list().size()];
//
//        for (int i = 0; i < personRepository.findAll().list().size(); i++) {
//            rowPerson = sheet.createRow(i + 4);
//            cellPerson = rowPerson.createCell(0);
//            cellPerson.setCellValue(personRepository.findById((long)i+1).getFirstName());
//            cellPerson = rowPerson.createCell(1);
//            cellPerson.setCellValue(personRepository.findById((long)i+1).getLastName());
//
//            countMenu = 0;
//            for (int j = 0; j < datesUntilList.size() - 1 ; j++) {
//                cellPerson = rowPerson.createCell(j+2);
//
//                Consumation personCosumation = consumationRepository.canConsumeToday(personRepository.findById((long)i+1));
//
//                if(personCosumation != null){
//                    if(personCosumation.isHasConsumed()){
//                        cellPerson.setCellValue("1");
//                        countMenu++;
//
//                        countMeal[j] += 1;
//
//
//                    }else{
//                        cellPerson.setCellValue("0");
//                    }
//
//                }else{
//                    cellPerson.setCellValue("kein Wert");
//                }
//            }
//
//            cellPerson = rowPerson.createCell(datesUntilList.size() + 1);
//            cellPerson.setCellValue(countMenu);
//            cellPerson = rowPerson.createCell(datesUntilList.size() + 2);
//            cellPerson.setCellValue(countMenu * preisMenu );
//        }
//
//
//        Row countMealRow = sheet.createRow(104);
//        Cell countMealCell = countMealRow.createCell(1);
//        countMealCell.setCellValue("Anzahl Essen");
//
//        for (int i = 0; i < datesUntilList.size()  - 1; i++) {
//            countMealCell = countMealRow.createCell(2+i);
//            countMealCell.setCellValue(countMeal[i]);
//
//        }
//
//
//
//
//        File currDir = new File(".");
//        String path = currDir.getAbsolutePath();
//        String fileLocation = path.substring(0, path.length() - 1) + "test.xlsx";
//
//        FileOutputStream outputStream = new FileOutputStream(fileLocation);
//        workbook.write(outputStream);
//        workbook.close();
//
//
//    }
//}
