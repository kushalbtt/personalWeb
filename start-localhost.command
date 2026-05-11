#!/bin/bash
cd "$(dirname "$0")" || exit 1

PORT="${1:-8000}"

while ! python3 - "$PORT" <<'PY'
import socket
import sys

port = int(sys.argv[1])
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    try:
        sock.bind(("127.0.0.1", port))
    except OSError:
        sys.exit(1)
PY
do
  PORT=$((PORT + 1))
done

URL="http://localhost:${PORT}/index.html"

echo "Serving this website at ${URL}"
echo "Press Ctrl+C in this window to stop the local server."

if command -v open >/dev/null 2>&1; then
  sleep 1
  open "${URL}" &
fi

python3 -m http.server "${PORT}"
