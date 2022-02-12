package backupzip.batch.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import backupzip.exception.IllegalInputFilePathException;
import backupzip.util.SelfProperties;

/**
 * バックアップ処理クラス
 */
final class BackUpZip {
    
    /**
     * メインとなる処理
     * @param propertiesPath 起動引数
     * @return
     */
    protected int doBatch(String propertiesPath) {
        int returnCode = 1;
        
        // プロパティロード
        if (SelfProperties.load(propertiesPath) == 1) {
            
            return returnCode;
        }

        // zip化対象ファイル格納用変数
        List<File> targetFiles = new ArrayList<>();
        
        // プロパティ「入力ファイルパス」先チェック
        try {
            targetFiles = checkInputFilePath();
        } catch (IllegalInputFilePathException e) {
            e.printStackTrace();
            
            return returnCode;
        }
        
        // プロパティ「出力ファイルパス」先チェック
        if (checkOutputFilePath() == 1) {
            return returnCode;
        };
        
        // zip化処理実行
        try {
            returnCode = compressExecute(targetFiles);
        } catch (IOException e) {
            System.err.println("想定外のエラーが発生しました。");
            e.printStackTrace();
        }
        
        return returnCode;
    }

    /**
     * zip化対象となるファイル(あれば複数)を取得する<br>
     * ※拡張子.zipについては対象外
     * 
     * @param inputFile プロパティ「入力ファイルパス」の値
     * @return
     * @throws IllegalInputFilePathException 
     */
    private List<File> getTargetFiles(final File parentDir, final List<File> files) throws IllegalInputFilePathException {
        
        try {
            for(File f : parentDir.listFiles()) {
                
                if (f.isFile()) {
                    files.add(f);
                }
                
                if (f.isDirectory()) {
                    getTargetFiles(f, files);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("入力ファイルパスの値が不正です。");
            throw new IllegalInputFilePathException();
        }
        
        return files;
        
    }

    /**
     * プロパティ「入力ファイルパス」先のチェックを実施する
     * @return
     * @throws IllegalInputFilePathException
     */
    private List<File> checkInputFilePath() throws IllegalInputFilePathException {
        
        // 入力ファイルパスチェック
        List<File> targetFiles = getTargetFiles(
                        Paths.get(SelfProperties.getInputProperties()).toFile(), new ArrayList<File>());
        
        // 入力ファイル存在チェック
        inputFileExist(targetFiles);
        
        return targetFiles;
    }

    /**
     * 入力ファイルパス先にzip対象となるファイルが存在しない場合エラーとする
     * 
     * @param targetFiles
     * @throws IllegalInputFilePathException
     */
    private void inputFileExist(List<File> targetFiles) throws IllegalInputFilePathException {
        
        if (targetFiles.size() < 1) {
            System.err.println("入力ファイルパス先にファイルが存在しません。");
            throw new IllegalInputFilePathException();
        }
        
    }

    /**
     * プロパティ「出力ファイルパス」先チェック
     * <br><br>
     * 出力ファイルパス先に「(実行日時)_bk.zip」が存在する場合エラーとし、trueを返す
     * @return
     */
    private int checkOutputFilePath() {
        
        // 出力ファイルルートパス存在チェック
        outputDirExists();
        
        // 出力ファイル同名存在チェック
        if (sameOutputFileNameExists()) {
            return 1;
        };
        
        return 0;
    }

    /**
     * 出力ファイル同名存在チェック
     * @return
     */
    private boolean sameOutputFileNameExists() {
        
        String backUpFileName = getBackUpFileName();
        String[] files = new File(SelfProperties.getOutputProperties()).list();
        if (null == files) {
            
            return false;
        }
        for (int i = 0; i < files.length; i++) {
            if (backUpFileName.equals(files[i])) {
                System.err.println("既に同名のバックアップファイルが存在します。");
                
                return true;
            }
        }
        
        return false;
        
    }

    /**
     * 出力ファイルルートパス存在チェック
     * <br><br>
     * 存在しない場合新規でディレクトリを作成する
     */
    private void outputDirExists() {
        
        File file = Paths.get(SelfProperties.getOutputProperties()).toFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        
    }

    /**
     * zip化後のファイル名を返す
     * 
     * @return
     */
    private String getBackUpFileName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MMdd");
        return now.format(dtf) + "_bk.zip";
    }

    /**
     * zip化処理実行
     * @param targetFiles
     * @return
     * @throws IOException
     */
    private int compressExecute(List<File> targetFiles) throws IOException {
    
        byte[] buf = new byte[1024];
        String fullPath = SelfProperties.getOutputProperties() + "/" + getBackUpFileName();
    
        try (ZipOutputStream zos = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(fullPath)), Charset.forName("UTF-8"));){
    
            // 全ファイルをZIPに格納
            for (File file : targetFiles) {
    
                // ZIP化実施ファイルの情報をオブジェクトに設定
                ZipEntry entry = new ZipEntry(
                                    file.getAbsolutePath().replace(
                                            Paths.get(SelfProperties.getInputProperties()).toFile().getAbsolutePath() + File.separator,
                                            ""));
                
                zos.putNextEntry(entry);
    
                // ZIPファイルに情報を書き込む
                try (InputStream is = new BufferedInputStream(new FileInputStream(file))){
                    
                    int len = 0;
                    while ((len = is.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                }
            }
            
            return 0;
        }
    }

}
