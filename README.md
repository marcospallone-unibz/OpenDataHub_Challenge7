# OpenDataHub_Challenge7

- File di configurazione: 
api_1:
  endpoint: "https://mobility.api.com"
  field: "coords"
api_2:
  endpoint: "https://tourism.api.com"
  field: "coordinate"

- cerco gli oggetti che hanno coords = coordinate
- li metto insieme in un nuovo oggetto response ed elimino "coords" o "coordinate". 


Steps: 
- creare 2 classi: la prima per api1 e l'altra per api2
- .

##Example of configuration file

```JSON
{
  "apiClients": [
    {
      "name": "mobility",
      "type": "OpenDataHubApiDatabase1",
      "url": "https://api-database1.com",
      "keysWhereFindDuplicates": {
        "key": "dataKey",
        "path": "item>location"
      }
    },
    {
      "name": "tourism",
      "type": "OpenDataHubApiDatabase2",
      "url": "https://api-database2.com",
      "keysWhereFindDuplicates": {
        "key": "dataKey",
        "path": "item2>location2"
      }
    }
  ]
}
```