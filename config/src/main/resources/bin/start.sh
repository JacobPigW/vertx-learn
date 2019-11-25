#!/usr/bin/env bash

module_dir=$(dirname "$0")/..
bin_dir=$module_dir/bin
conf_dir=$module_dir/conf
log_dir=$module_dir/logs
lib_dir=$module_dir/lib

sub_module="@subModule@"

port="$1"
if [ -z "$port" ]; then
    port=8080
fi

pid_file=${log_dir}/${sub_module}_pid_$port

function exec-jar {
    java -Xms256m \
         -Xmx512m \
         -server -Xloggc:$log_dir/gc.log \
         -verbose:gc -XX:+PrintGCDetails \
         -XX:+HeapDumpOnOutOfMemoryError \
         -XX:HeapDumpPath=$log_dir \
         -Dcom.sun.management.jmxremote \
         -Dcom.sun.management.jmxremote.port=8089 \
         -Dcom.sun.management.jmxremote.rmi.port=8090 \
         -Dcom.sun.management.jmxremote.ssl=false \
         -Dcom.sun.management.jmxremote.local.only=false \
         -Dcom.sun.management.jmxremote.authenticate=false \
         -Dlogging.config=$conf_dir/logback.xml \
         -Dlogging.path=$log_dir \
         -Dlogging.file=$sub_module \
         -Dserver.port=$port \
         -Dspring.config.location=$conf_dir/application.yaml,$conf_dir/jdbc.yaml,$conf_dir/$sub_module.yaml \
         -jar $lib_dir/$sub_module.jar > /dev/null 2>&1 &

    sleep 2s

    echo "$!" > $pid_file
}

function check-start {
    local info_log=${log_dir}/${sub_module}_info.log
    while [ -f "$info_log" ]
    do
        local tail_info=$(tail -1 "$info_log")
        local result=$(echo "$tail_info" | grep -w "Started")
        local err_info=$(echo "$tail_info" | grep "Error starting ApplicationContext")
        if [[ "${result}" != "" ]]; then
            echo "" && echo "${sub_module} ${port} is running (pid $(cat $pid_file))"
            sleep 1
            echo "$result" | awk -F " - " '{print $2}'
            break
        else
            echo -n "."
            sleep 1
        fi

        if [[ "${err_info}" != "" ]]; then
            echo "" && echo "${sub_module} ${port} boot failure !! please check ${log_dir}/${sub_module}_error.log"
            rm "$pid_file"
            break
        fi
    done
}

function check-stop {
    local info_log=${log_dir}/${sub_module}_info.log
    while [ -f "$info_log" ]
    do
        local result=$(grep -w " Shutdown completed." "$info_log")
        if [[ "${result}" != "" ]]; then
            sleep 1s
            echo "" && echo "${sub_module} ${port} stopped"
            break
        else
            echo -n "."
            sleep 0.001
        fi
    done
}

case "$2" in
    start)
        if [ -s "${pid_file}" ]; then
            echo "${sub_module} ${port} is already running (pid $(cat $pid_file))"
            exit 0
        else
            echo -n -e "Starting ${sub_module} ${port}."
            exec-jar && check-start
        fi
        ;;
    stop)
        if [ -s "${pid_file}" ]; then
            echo -n -e "${sub_module} ${port} is stopping."
            kill -15 "$(cat $pid_file)"
            check-stop
            rm "${pid_file}"
        else
            echo "${sub_module} ${port} is not running."
        fi
        ;;
    restart)
        ${0} "$1" stop
        sleep 1s
        ${0} "$1" start
        ;;
    status)
        if [ -s "${pid_file}" ]; then
            echo "${sub_module} ${port} is running (pid $(cat $pid_file))."
        else
            echo "${sub_module} ${port} is not running."
        fi
        ;;
    *)
        echo "Usage: ${bin_dir}/${sub_module} {port} {start|stop|restart|status}" >&2
        exit 1
        ;;
esac
