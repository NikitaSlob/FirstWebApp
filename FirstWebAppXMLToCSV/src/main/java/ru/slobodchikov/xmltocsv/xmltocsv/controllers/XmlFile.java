package ru.slobodchikov.xmltocsv.xmltocsv.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.slobodchikov.xmltocsv.xmltocsv.XmlToCsv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class XmlFile {
    public XmlToCsv xmlToCsv;

    public XmlToCsv getXmlToCsv() {
        return xmlToCsv;
    }

    public void setXmlToCsv(XmlToCsv xmlToCsv) {
        this.xmlToCsv = xmlToCsv;
    }

    @GetMapping(value="/upload")
    public String Upload() {
        return "upload";
    }
    /**
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping(value="/upload",produces = "text/plain;charset=UTF-8")
    public void handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!file.isEmpty()) {
                xmlToCsv.xmlReader(file.getInputStream());
                xmlToCsv.MetalSort();
                OutputStream outputStream=new BufferedOutputStream(response.getOutputStream());
                xmlToCsv.MetalSort();
                xmlToCsv.Filter();
                xmlToCsv.writeToCsvFile(outputStream);
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=" +"metals.csv");
                response.setHeader("Content-Transfer-Encoding", "binary");
                outputStream.close();
                response.flushBuffer();
            }
        } catch (Exception exception) {
            String s = "Вам не удалось загрузить файл => " + exception.toString();
            System.out.println(s);
        }
    }

}
