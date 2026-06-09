### Installatie Elasticsearch
We gaan een elasticsearch cluster met 3 nodes installeren. Hiervoor staan de nodige templates in 01-elasticsearch. De nodes zullen voor onderlinge communicatie gebruik maken van certificaten. Volg hiervoor eerst de README in 01-elasticsearch/certs.  
Vul de gegevens in in het elasticsearch-certificates secret in 02-secret.yaml.  
Apply de yaml's in 01-elasticsearch voor:
- namespace
- secrets
- configmap
- service

Controleer in headlamp dat je deze objecten allemaal in de elastic namespace ziet verschijnen.

Apply nu de statefulset yaml en wacht tot de drie pods zijn gestart. Als het goed is moet dit een cluster met status groen opleveren, dit kun je via een terminal op een van pods controleren met:

`curl -s -u "elastic:${ELASTIC_PASSWORD}" http://localhost:9200/_cluster/health`

De waarde voor elastic_password kun je vinden in het secret dat je hebt aangemaakt.

Als laatste moet de init job nog worden gedraaid. Deze al het password instellen voor kibana en kibana_system op basis van de waarde die je in het secret hebt staan. Check na afloop in de logs van de pod die heeft gedraaid om de job dit zonder problemen heeft uitgevoerd.

### Installatie Kibana
Voor de installatie van Kibana kunnen de de yaml's in 02-kibana in een keer worden toegepast. Wacht vervolgens tot de Kibana pod op in Headlamp op groen staat.

Om gebruik te maken van Kibana moet je een port forward doen:  
`kubectl port-forward -n elastic svc/kibana 5601:5601`

Vervolgens kun je inloggen op http://localhost:5601 en hierbij kun je gebruik maken van de user elastic en wederom het password dat in het secret staat.

### Installatie APM
Ook voor APM kunnen de yaml's (in 03-apm) in een keer worden toegepast. Wachten tot de apm server pod op groen staat en voer daarna de volgende check uit om te zien of APM gegevens richting Elasticsearch kan sturen.

`kubectl port-forward svc/apm-server 8200:8200 -n elastic`

Voer vervolgens de call uit die staat in `04-check-apm.http` en controleer via Kibana of je de data terugziet in de index `traces-apm-default`.

### Installatie OTEL Collector
Voor de installatie van de otel collector kunnen de yaml's (in 04-otel-collector) in een keer worden toegepast. Wachten tot de collector pod op groen staat en voer daarna de volgende check uit om te zien of de OTEL Collector gegevens richting Elasticsearch kan sturen.

`kubectl port-forward svc/otel-collector 4318:4318 -n elastic`

Voer vervolgens de call uit die staat in `04-check-otel.http` en controleer via Kibana of je de data terugziet in de index `traces-apm-default`.

Ook deze data zou daarna te vinden moeten zijn in Kibana in de index traces-apm-default.

Starten van de applicatie
port-forward svc/otel-collector 4318:4318 -n elastic
Zorg dat de configuratie in de applicatie verwijst naar localhost:4318



Alternatieven

kubectl create secret generic elasticsearch-certs \
-n elastic \
--from-file=ca.crt=/tmp/elastic-certs/ca.crt \
--from-file=node.crt=/tmp/elastic-certs/node.crt \
--from-file=node.key=/tmp/elastic-certs/node.key

