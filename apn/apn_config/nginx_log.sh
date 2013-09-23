#!/bin/bash
save_path="/data/log/"
logs_path="/var/log/nginx/"
pid_path="/run/nginx.pid"
seqfile=${logs_path}nginx_seq

d=$(date -d "now" +"%d")
seqDate=${d}
seq=1
if test -e ${seqfile}; then
        seqDate=`awk -F "," '{print $1}' ${seqfile}`
        seq=`awk -F "," '{print $2}' ${seqfile}`
        if [ "$seqDate" -eq "$d" ]; then
                seq=$((seq+1))
        else
                seqDate=$d
                seq=1
        fi
fi

mv ${logs_path}access.log ${save_path}web_$(date -d "now" +"%d%m%Y_%H%M%S")_`printf %05d ${seq}`.log
echo "${seqDate},${seq}">${seqfile}
kill -USR1 `cat ${pid_path}`
