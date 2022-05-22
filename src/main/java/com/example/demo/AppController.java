package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    private FileRepo repo;

    @GetMapping("/")
    public String HomePage(Model model) {
        List<BootFile> fileList = repo.findAll();
        model.addAttribute("fileList", fileList);
        return "home";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("uploadFile")
                                     MultipartFile multipartFile,
                             RedirectAttributes redirectAttributes) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        BootFile bootFile = new BootFile();
        bootFile.setName(fileName);
        bootFile.setContent(multipartFile.getBytes());
        bootFile.setSize(multipartFile.getSize());
        bootFile.setUploadTime(new Date());

        repo.save(bootFile);

        redirectAttributes.addFlashAttribute("message",
                "the boot file has been successfully uploaded.");

        return "redirect:/";
    }

    @GetMapping("/download")
    public void downloadFile(@Param("id") Long id, HttpServletResponse response) throws Exception {
        Optional<BootFile> result = repo.findById(id);
        if (!result.isPresent()) {
            throw new Exception("Could not find");
        }

        BootFile bootFile = result.get();
        response.setContentType("app/stream");
        String headerKey = "Content_Disposition";
        String headerValue = "attachment; filename=" + bootFile.getName();
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bootFile.getContent());
        outputStream.close();
    }
}
