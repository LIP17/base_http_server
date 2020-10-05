#!/bin/bash

set -euxo pipefail  # https://vaneyckt.io/posts/safer_bash_scripts_with_set_euxo_pipefail/

_term() {
  echo "SIGTERM signal!"
  kill -TERM "$child" 2>/dev/null
}

_hup() {
  echo "SIGHUP signal!"
  kill -HUP "$child" 2>/dev/null
}

_quit() {
  echo "SIGQUIT signal!"
  kill -QUIT "$child" 2>/dev/null
}

_int() {
  echo "SIGINT signal!"
  kill -INT "$child" 2>/dev/null
}


trap _term SIGTERM
trap _hup SIGHUP
trap _quit SIGQUIT
trap _int SIGINT

"$@" &  # equal to "$1" "$2" ... &

child=$!  # get most recently executed background pipeline
wait "$child"
