## Comandos do bash para consultar dados do agregado Usuario


### Recuperar uma instância de Usuario a partir de seu username:

Sem gateway:

```
curl -i -X GET http://localhost:8040/usuario/username/julio
```

Com gateway:

```
curl -i -X GET -H "Authorization: Bearer $TOKEN" http://localhost:8020/v1/id/usuario/username/julio
```



### Recuperar uma instância de Usuario a partir de seu id:

Sem gateway:

```
curl -i -X GET http://localhost:8040/usuario/1
```

Com gateway:

```
curl -i -X GET -H "Authorization: Bearer $TOKEN" http://localhost:8020/v1/id/usuario/3
```