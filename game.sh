#!/bin/sh
step() {
    echo $1
    input tap $2 $3
#    sleep 1
}

buzhuo(){
    echo 'bu zhuo start'
    input tap 833 390
    input tap 833 390
    echo 'bu zhuo end'
}


LOGF='/sdcard/game.log'
echo $LOGF
while true; do

touch $LOGF
echo `date` > $LOGF
echo "pid is $$" >> $LOGF
if [ -f "/sdcard/game" ];then
echo start >> $LOGF
step 'task continue' 1122 328
step 'animal dead' 829 636
buzhuo
step 'task continue' 1122 328
buzhuo
step 'task continue' 1122 328
buzhuo
step 'task continue' 1122 328
buzhuo
step 'zhan dou jie shu' 779 617
step 'ling qu jiang li' 1081 590
else
echo sleep >> $LOGF
echo sleep 5
sleep 5

fi

done
