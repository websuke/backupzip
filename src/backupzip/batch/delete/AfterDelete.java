package backupzip.batch.delete;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import backupzip.util.SelfProperties;


/**
 * 後処理削除処理クラス
 */
final class AfterDelete{
    
    int doBatch(String propertiesPath) {
        int returnCode = 1;
        
        // プロパティロード
        if (SelfProperties.load(propertiesPath) == 1) {
            
            return returnCode;
        }
        
        File inputFile = Paths.get(SelfProperties.getInputProperties()).toFile();
        returnCode = allDelete(inputFile);
        
        return returnCode;
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
