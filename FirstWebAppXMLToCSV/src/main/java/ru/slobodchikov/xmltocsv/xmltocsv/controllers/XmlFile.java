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
@Controller
class XmlFile {
    public final XmlToCsv xmlToCsv=new XmlToCsv();
    private static final Logger logger = LoggerFactory.getLogger(XmlFile.class);
    @GetMapping("/")
    public String showPage() {
        return "/upload";
    }

    /**
     * @param file
     * @param response
     */
    @PostMapping(value = "/upload", produces = "text/plain;charset=UTF-8")
    public void handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            if (!file.isEmpty()) {
                try (OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
                    xmlToCsv.transform(file.getInputStream(), outputStream);
                    response.setContentType("text/csv");
                    response.setHeader("Content-Disposition", "attachment; filename=" + "metals.csv");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    outputStream.close();
                    response.flushBuffer();
                } catch (IOException exception) {
                    String exStr = "Вам не удалось загрузить файл => " + exception.toString();
                    logger.debug(exStr);
                }
            } else {
                throw new Exception("Файл пустой");
            }
        } catch (Exception exception) {
            String exStr = "Вам не удалось загрузить файл => " + exception.toString();
            logger.debug(exStr);
        }
    }

}
