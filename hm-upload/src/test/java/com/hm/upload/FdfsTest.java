//package com.hm.upload;
//
//import com.hm.upload.service.UploadService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.FileNotFoundException;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = UploadService.class)
//public class FdfsTest {
//
//    @Autowired
//    private FastFileStorageClient storageClient;
//
//    @Autowired
//    private ThumbImageConfig thumbImageConfig;
//
//    @Test
//    public void testUpload() throws FileNotFoundException {
//        File file = new File("D:\\test\\baby.png");
//        // 上传并且生成缩略图
//        StorePath storePath = this.storageClient.uploadFile(
//                new FileInputStream(file), file.length(), "png", null);
//        // 带分组的路径
//        System.out.println(storePath.getFullPath());
//        // 不带分组的路径
//        System.out.println(storePath.getPath());
//    }
//
//    @Test
//    public void testUploadAndCreateThumb() throws FileNotFoundException {
//        File file = new File("D:\\test\\baby.png");
//        // 上传并且生成缩略图
//        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
//                new FileInputStream(file), file.length(), "png", null);
//        // 带分组的路径
//        System.out.println(storePath.getFullPath());
//        // 不带分组的路径
//        System.out.println(storePath.getPath());
//        // 获取缩略图路径
//        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
//        System.out.println(path);
//    }
//}