package com.dulcejosefina.utils;
import com.dulcejosefina.entity.ImagenProducto;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
public class Imagen {    
  public Imagen(){}
  public String[] procesarImagen(byte[] longitudImagen,String nameImage,String magnitud) {
        String arrayImageProperties[] = null;
        try {            
                String extension=nameImage.substring(nameImage.indexOf(".")+1, nameImage.length());       
                            String nameImg=nameImage.substring(0, nameImage.indexOf("."));                            
                            ByteArrayInputStream bis = new ByteArrayInputStream(longitudImagen);
                            Iterator<?> readers = ImageIO.getImageReadersByFormatName(extension);
                            ImageReader reader = (ImageReader) readers.next();
                            Object source = bis;
                            ImageInputStream iis = ImageIO.createImageInputStream(source);
                            reader.setInput(iis, true);
                            ImageReadParam param = reader.getDefaultReadParam();
                            Image image = reader.read(0, param);
                            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2 = bufferedImage.createGraphics();
                            g2.drawImage(image, null, null);                            
                            File imageFile = new File(ResourceBundle.getBundle("config").getString("PATH_IMAGE")+nameImage);
                            ImageIO.write(bufferedImage, extension, imageFile);
                arrayImageProperties = new String[]{imageFile.getPath(),extension,magnitud,nameImg};
                   
        } catch (IOException ex) {
            Logger.getLogger("ERROR AL PROCESAR LA IMAGEN "+ex.getMessage());
        }       
        return arrayImageProperties;
    }
    
    public byte[] obtenerImagenByteArray(List<ImagenProducto> listaDeImagenesProducto){
        byte[]retorno = new byte[5];
        String pathImage = "";        
            if(listaDeImagenesProducto.size()>0){ 
                    for(ImagenProducto imagen:listaDeImagenesProducto){
                        
                           pathImage=ResourceBundle.getBundle("config").getString("PATH_IMAGE")+imagen.getNombreImagen()+"."+imagen.getExtension();                                        
                           
                    }                                  
                                    File file = new File(pathImage);
                                            if(!file.exists()){                                   
                                                    retorno = procesarImagen(new File(ResourceBundle.getBundle("config").getString("PATH_IMAGE")+"android-logo-400x300.jpg"));                                                
                                            }else{                                   
                                                    retorno = procesarImagen(file);                                                
                                            }
            }   
            
            return retorno;
    }    
    private byte[] procesarImagen(File file){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {            
            fis = new FileInputStream(file);            
            byte [] buffer = new byte[1_024];            
                for(int readNum;(readNum=fis.read(buffer))!=-1;){
                    bos.write(buffer,0,readNum);
                }   
        } catch (FileNotFoundException ex) {
            Logger.getLogger("Error al Procesar Imagen archivo no encontrado");
        } catch (IOException ex) {
            Logger.getLogger("Error al Procesar Imagen entrada salida");
        }
        return bos.toByteArray();        
    }    
}