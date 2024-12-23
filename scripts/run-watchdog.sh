#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# Ir al directorio raíz del proyecto
cd "$DIR/.."

# Cargar variables de entorno
source .env
java -jar bitcoin-watchdog.jar