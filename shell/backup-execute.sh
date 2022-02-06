#!/bin/bash

if [ $# -gt 2 -o $# -eq 0 ]; then
  echo 'shell起動時の引数の数が正しくありません。'
  echo
  echo '実行方法'
  echo '$ コマンド [-d] プロパティファイルパス'
  exit $?
fi

# 後処理で何もしない場合
if [[ $# -eq 1 ]]; then

  java -cp クラスパスを記載 backupzip.main.BackUpZip $1
  echo 'call zip target not delete'

# 後処理でzip化対象ファイルを削除する場合
elif [[ $1 = -d ]]; then

  java -cp クラスパスを記載 backupzip.main.BackUpZipAfterDelete $2 
  echo 'call zip target delete'

else

  echo 'shell起動時の引数の指定方法に誤りがあります。'
  echo 
  echo '実行方法'
  echo '$ コマンド [-d] プロパティファイルパス'
  exit $?

fi