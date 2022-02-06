#!/bin/bash

if [ $# -gt 2 -o $# -eq 0 ]; then

  echo 'shell起動時の引数の数が正しくありません。'
  echo
  echo '実行方法'
  echo '$ コマンド [-d] プロパティファイルパス'
  exit 1

fi

# 後処理で何もしないbatchを呼び出す場合
if [[ $# -eq 1 ]]; then

  java -cp クラスパスを記載 backupzip.main.BackUpZip $1
  echo 'call zip target not delete'
  exit $?

# 後処理でzip化対象ファイルを削除するbatchを呼び出す場合
elif [[ $1 = -d ]]; then

  java -cp クラスパスを記載 backupzip.main.BackUpZipAfterDelete $2 
  echo 'call zip target delete'
  exit $?
  
else

  echo 'shell起動時の引数の指定方法に誤りがあります。'
  echo 
  echo '実行方法'
  echo '$ コマンド [-d] プロパティファイルパス'
  exit 1

fi