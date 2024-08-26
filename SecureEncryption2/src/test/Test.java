package test;

import encryption2.SecureEncryption;
import utils.Random;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws IOException {

        SecureEncryption se = new SecureEncryption(4);

        se.initialize();

        System.out.println(se.decrypt(se.encrypt("hello")));

//        byte[] array = se.decrypt(se.encrypt("hello".getBytes()));
//        System.out.println(Arrays.toString(array));

//        byte[] bytes = {71, 13, 10, 26, 10,71};
//        byte[] bytes2 = {-107, 99, 101, -14, -39, 14};
//        byte[] bytes3 = {-71, 33, 60, 20, 47, -119
//        int hash = se.hash(bytes);
//        int hash2 = se.hash(bytes2);
//
//        System.out.println(hash+","+hash2);

//        Random rng = new Random(1231423);
//        for(int i=0;i<1000;i++){
//            rng.nextBytes(bytes);
//            long hash2=se.hash(bytes);
//            if(hash == hash2)System.out.println(Arrays.toString(bytes));
//        }

//        int[] encrypted = se.encrypt(bytes);
//        byte[] decrypted = se.decrypt(encrypted);
//
//        System.out.println(Arrays.toString(bytes));
//        System.out.println(Arrays.toString(decrypted));



//        byte[] realImageData = Files.readAllBytes(Paths.get("C:/Users/admin/Desktop/ui.png"));
//        byte[] imageData = se.decrypt(se.encrypt(realImageData));

//        for(int i=0;i< realImageData.length;i++)
//            if(realImageData[i]!= imageData[i]) System.out.println(realImageData[i]+"|"+imageData[i]);


//        System.out.println(Arrays.toString(imageData));

//        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
//        ImageIO.write(img,"png",new File("img.png"));




//        int[] ar1 = se.encrypt("this is ayon".getBytes());
//        int[] ar2 = se.encrypt("this is byon".getBytes());
//
//
//
//
//
//        String em1 = (se.arrayToString(ar1));
//
//        String em2 = (se.arrayToString(ar2));
//
//        System.out.println(em1);



//        System.out.println(em2);
//
//        System.out.println(Arrays.toString(se.encrypt("ayon".getBytes())));
//        System.out.println(Arrays.toString("ayon".getBytes()));
//        System.out.println(Arrays.toString(se.decrypt(se.encrypt("ayon".getBytes()))));


//        System.out.println(Arrays.toString(se.stringToArray(em1)));
//        System.out.println(Arrays.toString(ar1));
//        System.out.println(Arrays.toString(se.stringToArray(em2)));
//        System.out.println(Arrays.toString(ar2));

////        System.out.println(Arrays.toString(bytes));
//
//        byte[] fileByte = Files.readAllBytes(Paths.get("C:/Users/admin/Desktop/ui.png"));



//        System.out.println(Arrays.toString(Base64.getEncoder().encode(fileByte)));


//        System.out.println(new Random(1612).nextInt(-1,2));

//        System.out.println(Arrays.toString(Base64.getEncoder().encode(new byte[]{-1,2,23})));


//        System.out.println(Base64.getEncoder().encode(bytes).length == bytes.length);




//        try{

//            Class.forName("org.sqlite.JDBC");

//            Connection conn = DriverManager.getConnection("jdbc:sqlite:db","","");
//            PreparedStatement initStatement = conn.prepareStatement("create table data(array blob);");
//            PreparedStatement pushStatement = conn.prepareStatement("insert into data values(?)");
//            pushStatement.set
//            PreparedStatement statement = conn.prepareStatement("select * from data");



//        }catch (Exception ex){
//            ex.printStackTrace();
//        }



    }

}
