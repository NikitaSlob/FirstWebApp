package ru.slobodchikov.xmltocsv.xmltocsv.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.slobodchikov.xmltocsv.xmltocsv.sevice.XmlToCsv;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Контроллер для преобазования файла xml в csv
 */
@Controller
class XmlFile {
    /**
     * Класс для преобразования
     */
    private final XmlToCsv xmlToCsv;
    private static final Logger logger = LoggerFactory.getLogger(XmlFile.class);

    public XmlFile(XmlToCsv xmlToCsv) {
        this.xmlToCsv = xmlToCsv;
    }

    @GetMapping("/")
    public String showPage() {
        return "/upload";
    }

    /**
     * @param file - файл который нужно изменить
     * @param response - ответ http
     */
    @PostMapping(value = "/upload", produces = "text/csv;charset=UTF-8")
    public void handleFileUpload(@RequestParam("file") final MultipartFile file, final HttpServletResponse response) {
        try {
            if (file.isEmpty()) {
                throw new Exception("Файл пустой");
            }

            try (final OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
                xmlToCsv.transform(file.getInputStream(), outputStream);
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=" + "metals.csv");
                response.setHeader("Content-Transfer-Encoding", "binary");
                outputStream.close();
                response.flushBuffer();
            } catch (IOException exception) {
                logger.error("Вам не удалось загрузить файл => " + exception.toString(), exception);
            }
        } catch (Exception exception) {
            logger.error("Вам не удалось загрузить файл => " + exception.toString(), exception);
        }
    }

}
