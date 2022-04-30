#!/bin/sh

set -e
echo "Initializing {{ .Values.postgres.dbname }} database..."
#env
echo "Done initialization."
