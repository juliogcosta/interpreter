## Comandos do bash para consultar credencial de conta de usuário


### Consultar credencial de contra dfe usuário:
```
curl -i -X POST -H "Content-Type: application/json" -d '{"username":"julio","password":"12345"}' http://localhost:8020/v1/auth/sign-in
```