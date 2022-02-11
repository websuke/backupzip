package backupzip.batch.delete;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import backupzip.batch.main.BackUpZip;
import backupzip.util.SelfProperties;


/**
 * 派生クラス
 */
public final class AfterDelete{
    
    public int doBatch(String propertiesPath) {
        int returnCode = 1;
        
        // プロパティロード
        if (SelfProperties.load(propertiesPath) == 1) {
            
            return returnCode;
        }
        
        String inputFilePath = SelfProperties.getInstance().getProperty(SelfProperties.getInputPropertiesKey());
        File inputFile = Paths.get(inputFilePath).toFile();
        returnCode = allDelete(inputFile);
        
        return returnCode;
    }
    
    public static void main(String[] args) {
        
        // 起動引数チェック
        if (args.length != 1) {
            System.err.println("起動引数の数が1ではありません。");
            System.exit(1);
        }
        
        System.exit(new AfterDelete().doBatch(args[0]));

    }

    /**
     * 入力ファイルパス配下のファイルを全て削除する
     * 
     * @param file
     */
    private int allDelete(File file) {
        
        if (file.isFile()) {
            file.delete();
        }
        if (file.isDirectory()) {
            List<File> files = new ArrayList<>(Arrays.asList(file.listFiles()));
            for (File f : files) {
                allDelete(f);
            }
        }
        return 0;
    }
}
