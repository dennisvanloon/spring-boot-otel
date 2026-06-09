## CA private key aanmaken
`openssl genrsa -out ca.key 4096`

## CA self signed  certificate aanmaken
`openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 -subj "/CN=elastic-ca" -out ca.crt`

## Node key
`openssl genrsa -out node.key 2048`

## CSR aanmaken
`openssl req -new -key node.key -subj "/CN=elasticsearch" -out node.csr`

## CSR signeren door de CA
`openssl x509 -req -in node.csr -CA ca.crt -CAkey ca.key -CAcreateserial -days 825 -sha256 -out node.crt`