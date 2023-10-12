package com.example.G4_DailyReport.validation;

import com.example.G4_DailyReport.exception.ImportEmailFailedException;
import com.example.G4_DailyReport.util.Constants;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Slf4j
public abstract class ImportExcelValidator<S, T> {
    private List<S> sourceObjects;
    private final List<T> destinationObjects;
    private final MultipartFile multipartFile;
    private final int headerStart;
//    private final Locale locale;
    private final int lastCellNum;
    private final List<ErrorDetail> errorsDetail;

    public ImportExcelValidator(MultipartFile file, int headerStart, int lastCellNum) {
        // Convert Excel to Object
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
                .rawData(true)
                .trimCellValue(true)
                .headerStart(headerStart)
                .dateFormatter(DateTimeFormatter.ofPattern(Constants.PRINT_DATE_FORMAT))
                .build();

        try {
            Class<S> sourceType = (Class<S>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            this.sourceObjects = Poiji.fromExcel(file.getInputStream(), PoijiExcelType.XLSX, sourceType, options);
        } catch (IOException e) {
            // add return lỗi
        }

        this.errorsDetail = new ArrayList<>();
        this.destinationObjects = new ArrayList<>();
        this.multipartFile = file;
        this.headerStart = headerStart;
//        this.locale = locale;
        this.lastCellNum = lastCellNum;
    }

     public ImportExcelValidator<S, T> validatePre() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
//        ReloadableResourceBundleMessageSource messageSource = messageSource();

        int temp = 0;

        for (S dto: this.getSourceObjects()) {
            ErrorDetail errorDetail = new ErrorDetail();
            List<String> comments = new ArrayList<>();
            temp++;

            // Pre validation
            Set<ConstraintViolation<S>> violations = validator.validate(dto);

            if (violations.size() > 0) {

                for (ConstraintViolation<S> violation: violations) {
                    comments.add(
//                            messageSource.getMessage(
                                    violation.getMessage()
//                                    , null, locale)
                    );
                }

                errorDetail.setIndex(temp);
                errorDetail.setMessage(String.join(", ", comments));
                errorsDetail.add(errorDetail);
            }
        }

        return this;
    }

    public void errorMessengers(List<String> keyComments, int temp) {
        List<String> comments = new ArrayList<>();
//        ReloadableResourceBundleMessageSource messageSource = messageSource();

        for (String str: keyComments) {
            comments.add(
                    // messageSource.getMessage(
                    str
                    //, null, locale)
            );
        }

        String comment = String.join(", ", comments);

        if (!ObjectUtils.isEmpty(comment)) {
            ErrorDetail errorDetail = new ErrorDetail(temp, comment);
            this.getErrorsDetail().add(errorDetail);
        }
    }

    public void removeComment(XSSFSheet sheet) {
        // Get comment exists
        Map<CellAddress, XSSFComment> oldComments = sheet.getCellComments();
        Cell cell;

        for (Map.Entry<CellAddress, XSSFComment> oldComment: oldComments.entrySet()) {
            cell = sheet.getRow(oldComment.getKey().getRow()).getCell(oldComment.getKey().getColumn());
            cell.removeCellComment();

            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }
    }

    public void setCommentStyle(XSSFWorkbook workbook, XSSFSheet sheet, Map<Integer, String> mapErrorDetail) {
        Row row; Cell cell; Comment comment;

        CellStyle cellStyle = workbook.getCellStyleAt(0);
        cellStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();

        for (Map.Entry<Integer, String> errorDetail: mapErrorDetail.entrySet()) {
            int rowNumber = errorDetail.getKey() + headerStart;
            row = sheet.getRow(rowNumber);
            int lastColumn = Math.max(row.getLastCellNum(), lastCellNum);
            RichTextString errorMessenger = factory.createRichTextString(errorDetail.getValue());

            for (int i = 0; i < lastColumn; i++) {
                cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellStyle(cellStyle);

                comment = sheet.createDrawingPatriarch().createCellComment(anchor);
                comment.setString(errorMessenger);
                cell.setCellComment(comment);
            }
        }
    }

        public ImportExcelValidator<S, T> thenReturnExcelIfError(HttpServletResponse response) throws ImportEmailFailedException {
        if (!ObjectUtils.isEmpty(errorsDetail)) {
            throw new ImportEmailFailedException(this.getErrorsDetail());
//            try {
//                XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
//                XSSFSheet sheet = workbook.getSheetAt(0);
//
//                // remove comment
//                this.removeComment(sheet);
//                // map row index and message error
//                Map<Integer, String> mapErrorDetail = errorsDetail.stream().collect(Collectors.toMap(ErrorDetail::getIndex, ErrorDetail::getMessage));
//                // Set comment and style in sheet
//                this.setCommentStyle(workbook, sheet, mapErrorDetail);
//
//                response.setHeader("Content-Disposition", "attachment; filename=" + multipartFile.getOriginalFilename() + ".xlsx");
//                ServletOutputStream out = response.getOutputStream();
//
//                workbook.write(out);
//                out.flush();
//                out.close();
//            } catch (IOException e) {
//                // add return Error when create new file excel
//            }
            // add return validate error
        }

        return this;
    }

    public abstract ImportExcelValidator<S, T> validate();

    /**
     *  Handle data after pass all validate
     */
    public abstract List<T> thenMappingAndReturnDestinationObjects();
}
