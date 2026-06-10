grafana
├── grafana

logging
├── loki

metrics
├── mimir

tracing
├── tempo

observability
├── otel-collector

## Loki installeren
yaml's uitvoeren in 01-loki en checken of je alles terugziet in de kubernetes resource in podman desktop

kubectl port-forward --namespace logging svc/loki 3100:3100

curl -H "Content-Type: application/json" -XPOST -s "http://127.0.0.1:3100/loki/api/v1/push"  \
--data-raw "{\"streams\": [{\"stream\": {\"job\": \"test\"}, \"values\": [[\"$(date +%s)000000000\", \"fizzbuzz\"]]}]}"

curl "http://127.0.0.1:3100/loki/api/v1/query_range" --data-urlencode 'query={job="test"}' | jq .data.result

## Mimir installeren
yaml's uitvoeren in 02-mimir


## Tempo installeren
yaml's uitvoeren in 03-temp

## Otel-collector installeren
yaml's uitvoeren in 04-otel-collector

kubectl port-forward --namespace observability svc/otel-collector 4318:4318

## Grafana installeren
yaml's uitvoeren in 05-grafana

Check of alle pods die in de verschillende namespaces goed zijn gestart door naar de Ready status te kijken:
kubectl get pods --namespace grafana  
kubectl get pods --namespace logging  
kubectl get pods --namespace metrics  
kubectl get pods --namespace tracing  
kubectl get pods --namespace observability  

Forward vervolgens de volgende porten alvorens verder te gaan met het starten van de applicatie (zie algemene README)
Beschikbaar maken grafana UI op port 3000  
`kubectl port-forward --namespace grafana svc/grafana 3000:3000`

Beschikbaar maken otel-collector port voor de applicatie  
`kubectl port-forward --namespace observability svc/otel-collector 4318:4318`
