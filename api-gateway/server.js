const http = require('http');
const express = require('express');
const proxy = require('express-http-proxy');
const app = express();
const cors = require('cors');

app.use(express.json());
app.use(cors({
  origin: 'http://localhost:4200',
  methods: ['GET', 'POST', 'PUT', 'DELETE'],
  allowedHeaders: ['*'],
  credentials: true
}));


// validação de token
const validateToken = (req, res, next) => {
    const token = req.headers['x-access-token'];
    if (!token) return res.status(401).json({ message: 'Token não informado.' });

    // para validar o token com o auth-service
    http.get(`http://localhost:8090/auth/validate`, {
        headers: { 'x-access-token': token }
    }, (response) => {
        if (response.statusCode === 200) next();
        else res.status(401).json({ message: 'Token inválido.' });
    }).on('error', () => {
        res.status(500).json({ message: 'Erro ao validar token.' });
    });
};


app.use('/auth', proxy('http://localhost:8080', {
  proxyReqPathResolver: req => '/auth' + req.url
}));


app.use('/client', proxy('http://localhost:8091', {
  proxyReqPathResolver: req => '/client' + req.url
}));


app.use('/flight', validateToken, proxy('http://localhost:8093', { // aqui ele ta validando o token tambem
  proxyReqPathResolver: req => '/flight' + req.url
}));

app.use('/booking', proxy('http://localhost:8095', { 
  proxyReqPathResolver: req => '/booking' + req.url
}));



const server = http.createServer(app);
server.listen(3000, () => console.log('Gateway rodando na porta 3000'));
