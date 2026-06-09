monitoring
├── prometheus
├── grafana

loki
├── loki

metrics-backend
├── mimir

observability-pipeline
├── otel-collector

Loki
yaml's uitvoeren

kubectl port-forward --namespace loki svc/loki 3100:3100

curl -H "Content-Type: application/json" -XPOST -s "http://127.0.0.1:3100/loki/api/v1/push"  \
--data-raw "{\"streams\": [{\"stream\": {\"job\": \"test\"}, \"values\": [[\"$(date +%s)000000000\", \"fizzbuzz\"]]}]}"

curl "http://127.0.0.1:3100/loki/api/v1/query_range" --data-urlencode 'query={job="test"}' | jq .data.result

Prometheus
yaml's uitvoeren

kubectl port-forward --namespace monitoring svc/prometheus 9090:9090


Tempo
yaml's uitvoeren

Otel
yaml's uitvoeren

kubectl port-forward --namespace observability svc/otel-collector 4318:4318

####

http://loki.default.svc.cluster.local:3100/


curl 'http://localhost:9090/api/v1/query?query=up'

helm install tempo grafana/tempo -f tempo-values.yaml

port-forward svc/tempo 3200:3200
curl http://localhost:3200/ready

helm install grafana grafana/grafana -f grafana-values.yaml
export POD_NAME=$(kubectl get pods --namespace default -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=grafana" -o jsonpath="{.items[0].metadata.name}")
kubectl --namespace default port-forward $POD_NAME 3000

Prometheus → http://prometheus-server
Loki → http://loki:3100
Tempo → meestal http://tempo:3200