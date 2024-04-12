package vn.banking.academy.processor;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * class nhằm mục đích đọc 1 cuốn sách thành các trang rồi train cho chatGPT đọc
 */
public class ReadPDFFile {

    public static String contentPage(PdfReader reader, int page) {
        String rs = "";
        try {
            rs = PdfTextExtractor.getTextFromPage(reader, page);
            System.out.println("page == " + page);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rs;
    }
}
