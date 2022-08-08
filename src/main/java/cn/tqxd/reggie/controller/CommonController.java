package cn.tqxd.reggie.controller;

import cn.tqxd.reggie.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {


    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    //这里必须用file。框架限制的   name="file"
    public Result<String> upload(MultipartFile file) {
        //file 是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid重新生成文件名，防止文件名重复造成文件覆盖
        String fileName = UUID.randomUUID() + suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File("F:\\hello.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try{
            //输入流，通过输出 流读读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0 ;
            byte[] bytes = new byte[1024];
            while ((len= fileInputStream.read(bytes))!= -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
