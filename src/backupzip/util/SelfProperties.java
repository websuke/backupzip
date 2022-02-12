package backupzip.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * プロパティ操作クラス
 */
public class SelfProperties{
    
    private static Properties singletonProperties = new Properties();
    
    private SelfProperties() {};
    
    public static Properties getInstance() {
        return singletonProperties;
    }
    
    /**
     * プロパティファイルの読み込みを実施する
     * 
     * @param propertiesPath プロパティファイルのパス
     * @return
     */
    public static int load(String propertiesPath) {
        Properties prop = SelfProperties.getInstance();
        try {
            prop.load(
                    Files.newBufferedReader(
                            Paths.get(propertiesPath), StandardCharsets.UTF_8));
        } catch (NoSuchFileException e) {
            System.err.println("プロパティファイルが存在しません。");
            
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            
            return 1;
        }
        
        return 0;
    }

    
    /**
     * 「入力ファイルパス」のキー名を返す
     * @return
     */
    public static String getInputPropertiesKey() {
        return "input.filepath";
    }
    
    /**
     * 「出力ファイルルートパス」のキー名を返す
     * @return
     */
    public static String getOutputPropertiesKey() {
        return "output.root.filepath";
    }
    
    /**
     * プロパティキー「入力ファイルパス」の値を取得する
     * @return
     */
    public static String getInputProperties() {
        return SelfProperties.getInstance().getProperty(SelfProperties.getInputPropertiesKey());
    }

    /**
     * プロパティキー「出力ファイルルートパス」の値を取得する
     * @return
     */
    public static String getOutputProperties() {
        return SelfProperties.getInstance().getProperty(SelfProperties.getOutputPropertiesKey());
    };
}
