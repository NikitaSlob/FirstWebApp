package ru.slobodchikov.xmltocsv.XmltoCsv.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.slobodchikov.xmltocsv.XmltoCsv.XmlToCsv;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class CsvFile {
    @GetMapping(value="/download")
    public String Upload() {
        return "download";
    }


    public XmlToCsv getXmlToCsv() {
        return xmlToCsv;
    }

    public void setXmlToCsv(XmlToCsv xmlToCsv) {
        this.xmlToCsv = xmlToCsv;
    }

    private XmlToCsv xmlToCsv;

    @GetMapping(value = "/downloadf")
    public void getFile(HttpServletResponse response) {
        System.out.print("Hi");
        String workingDir = System.getProperty("user.dir");
        System.out.print("Hoi");
        xmlToCsv.xmlReader(workingDir + "\\tempres\\metals.xml");
        System.out.print("Hai");
        File metals = xmlToCsv.writeToCsvFile(workingDir + "\\tempres\\metals.csv");
        System.out.print("Hei");
        response.setHeader("Content-disposition", "attachment;filename=" + metals.getName());
        response.setContentType("text/csv");
        System.out.print("Hji");
        try {
            Files.copy(metals.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
