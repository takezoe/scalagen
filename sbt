#!/bin/sh
cd `dirname $0`
java -Dsbt.log.noformat=true -Xmx512M -Xss2M -XX:+CMSClassUnloadingEnabled -jar sbt-launch.jar "$@"
