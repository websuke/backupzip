package backupzip.main;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 派生クラス
 */
public final class BackUpZipAfterDelete extends AbstractBackUpZipBase {
    public static void main(String[] args) {
        
        // 起動引数チェック
        if (args.length != 1) {
            System.err.println("起動引数の数が1ではありません。");
            System.exit(1);
        }
        
        System.exit(new BackUpZipAfterDelete().doBatch(args[0]));
    }

    @Override
    protected String getInputPropertiesKey() {
        return "input.filepath";
    }

    @Override
    protected String getOutputPropertiesKey() {
        return "output.root.filepath";
    }

    @Override
    protected void postprocess() {
        File file = Paths.get(getInputProperties()).toFile();
        allDelete(file);
        
    }

    /**
     * 入力ファイルパス配下のファイルを全て削除する
     * 
     * @param file
     */
    private void allDelete(File file) {
        
        if (file.isFile()) {
            file.delete();
        }
        if (file.isDirectory()) {
            List<File> files = new ArrayList<>(Arrays.asList(file.listFiles()));
            for (File f : files) {
                allDelete(f);
            }
        }
        
    }
}
