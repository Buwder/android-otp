package pem.com.pemvideo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Object LOCK = new Object();
    private static Properties props = null;

    private final static void setProperties(String path){
        props = new Properties();
        InputStream inputStream  =   null ;
        try {
            inputStream  =  PropertiesUtil.class.getResourceAsStream(path);
            if(inputStream!=null){
                props.load(inputStream);
            }
        } catch (IOException e) {
            props = null;
            e.printStackTrace();
        }finally{
            try {
                if(inputStream!=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public final static Properties getProperties(){
        if(props == null){
            synchronized (LOCK) {
                setProperties("/Config.properties");
            }
        }
        return props;
    }
}
