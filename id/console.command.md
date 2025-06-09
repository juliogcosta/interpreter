## Comandos do bash para persistir dados associados ao agregado Usuario.


### Registrar uma nova instância de Usuario:

```
curl --location 'http://localhost:8040/usuario' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "julio",
    "password": "12345",
    "nome": "Julio Gustavo",
    "telefone": {
        "tipo": "CELULAR",
        "numero": "(84)98830-0101"
    },
    "email": {
        "email": "julio@gmail.com"
    },
    "status": "ATIVO"
}'
```


### Registrar uma nova instância de Papel:
```
curl --location 'http://localhost:8040/papel' \
--header 'Content-Type: application/json' \
--data '{
    "nome": "GERENTE",
    "status": "ATIVO"
}'
```


## Vincular uma instância de PapelDeUsuario a uma instância de Usuario:
```
curl --location 'http://localhost:8040/usuario/1/papelDeUsuario' \
--header 'Content-Type: application/json' \
--data '{
    "papelId": 1
}'
```