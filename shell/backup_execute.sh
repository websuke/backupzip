#!/bin/bash

# プロパティファイル存在チェック関数
propExists()
{
  if [ ! -f "$1" ]; then
    echo "プロパティファイルが存在しません"
    exit 1
  fi
}

# バッチ実行関数
execute()
{

  # shellscriptからみたjarへのパスを指定
  local CLASSPATH=./backupzip.jar
  
  if [[ $# -eq 1 ]]; then
    
    propExists $1
    
    java -cp $CLASSPATH backupzip.batch.main.BackUpZipMain $1
    echo 'called zip target not delete'
    exit 0
  
  elif [[ $1 = -d ]]; then
    
    propExists $2
    
    java -cp $CLASSPATH backupzip.batch.main.BackUpZipMain $2
    java -cp $CLASSPATH backupzip.batch.delete.AfterDeleteMain $2 
    echo 'called zip target delete'
    exit 0
    
  else
  
    echo 'shell起動時の引数の指定方法に誤りがあります。'
    echo 
    echo '正しい実行方法'
    echo '$ コマンド [-d] プロパティファイルパス'
    exit 1
  
  fi
}

# 引数チェック
if [ $# -gt 2 -o $# -eq 0 ]; then

  echo 'shell起動時の引数の数が正しくありません。'
  echo
  echo '正しい実行方法'
  echo '$ コマンド [-d] プロパティファイルパス'
  exit 1

fi

# 実行
execute $@