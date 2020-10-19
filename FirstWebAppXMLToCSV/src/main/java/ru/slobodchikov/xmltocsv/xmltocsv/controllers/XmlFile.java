package ru.slobodchikov.xmltocsv.xmltocsv.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.slobodchikov.xmltocsv.xmltocsv.sevice.XmlToCsv;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class XmlFile {
    public XmlToCsv xmlToCsv;
    private static final Logger logger = LoggerFactory.getLogger(XmlFile.class);

    public XmlToCsv getXmlToCsv() {
        return xmlToCsv;
    }

    public void setXmlToCsv(XmlToCsv xmlToCsv) {
        this.xmlToCsv = xmlToCsv;
    }

    @GetMapping(value = "/upload")
    public String upload() {
        xmlToCsv = new XmlToCsv();
        return "upload";
    }

    /**
     * @param file
     * @param response
     */
    @PostMapping(value = "/upload", produces = "text/plain;charset=UTF-8")
    public void handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            if (!file.isEmpty()) {
                xmlToCsv.xmlReader(file.getInputStream());
                xmlToCsv.metalSort();
                try (OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
                    xmlToCsv.metalSort();
                    xmlToCsv.filter();
                    xmlToCsv.writeToCsvFile(outputStream, logger);
                    response.setContentType("text/csv");
                    response.setHeader("Content-Disposition", "attachment; filename=" + "metals.csv");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    outputStream.close();
                    response.flushBuffer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception exception) {
            String exStr = "Вам не удалось загрузить файл => " + exception.toString();
            logger.debug(exStr);
        }
    }

}
