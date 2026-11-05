#!/bin/sh

#
# Copyright © 2015 the original authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# SPDX-License-Identifier: Apache-2.0
#

APP_HOME=$(cd -P "${0%/*}" > /dev/null && printf '%s\n' "$PWD") || exit
MAX_FD=maximum
warn () { echo "$*" >&2; }
die () { echo; echo "$*" >&2; echo; exit 1; }

if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
    [ -x "$JAVACMD" ] || die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
else
    JAVACMD=java
    command -v java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

if [ "$MAX_FD" = maximum ]; then
    MAX_FD=$(ulimit -H -n) || warn "Could not query maximum file descriptor limit"
fi
case $MAX_FD in ''|soft) :;; *) ulimit -n "$MAX_FD" || warn "Could not set maximum file descriptor limit to $MAX_FD";; esac

DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
set -- "-Dorg.gradle.appname=${0##*/}" -jar "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
eval "set -- $(printf '%s\n' "$DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS" | xargs -n1 | sed 's~[^-[:alnum:]+,./:=@_]~\\&~g' | tr '\n' ' ')" '"$@"'
exec "$JAVACMD" "$@"
