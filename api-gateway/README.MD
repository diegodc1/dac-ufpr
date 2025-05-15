## Para rodar a API Getaway: 
node server.js

## Basicamente funciona assim:

Lá no front (Angular), a gente faz a requisição por exemplo mais ou menos assim:

this.http.get('http://localhost:3000/auth/teste');

esta chamada não vai direto pro Back (Spring), ele primeiro passa pelo Getaway. Então a API Getaway recebe esta requisição, e lá tem essa configuração:

app.use('/auth', proxy('http://localhost:8080', {
  proxyReqPathResolver: req => '/auth' + req.url
}));

Basicamente ele ta 'capturando' qualquer requisição que começar com '/auth', e com isto ele manda a requisição "http://localhost:8080/auth/teste" lá pro back, e assim o back retorna os dados e tals...

Entao, se la no front a gente quiser pegar informações do microserviço de voos por exemplo, a requisição tem que começar com "/flight", se quiser de clientes, tem que começar com "/client", e assim por diante...


## Um ponto importante: Tem que ficar de olho nas portas que estão rodando os microserviços! 

As portas corretas são:
 Serviço de Autenticação: porta "8080", 
 Serviço de Clientes: porta "8091
 Serviço de Voos: porta "8093",
 Serviço de Reservas: porta "8095"
