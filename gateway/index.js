require("dotenv-safe").config();
const http = require('http');
const express = require('express');
const httpProxy = require('express-http-proxy');
const app = express();
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const helmet = require('helmet');
const cors = require('cors');
const axios = require('axios');


const corsOptions = {
    origin: ['http://localhost:4200'],
    allowedHeaders: ['Content-Type', 'x-access-token'],
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS']
};

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(logger('dev'));
app.use(helmet());
app.use(cors(corsOptions));

//app.options('*', cors(corsOptions));


// PROXIES

const authServiceProxy = httpProxy('http://localhost:8080');

const voosProxy = httpProxy('http://localhost:8081');

const clienteServiceProxy = httpProxy('http://localhost:8082'); 

const validateTokenProxy = (req, res, next) => {

    const token = req.headers['x-access-token'];
    if (!token) {
        return res.status(401).send({ message: 'Token not provided!' });
    }

    const validationReqOptions = {
        headers: {
            'Content-Type': 'application/json',
            'x-access-token': token
        },
        method: 'GET'
    };

    const validationReq = http.request('http://localhost:8080/auth/validate', validationReqOptions, (validationRes) => {
        let data = '';
        validationRes.on('data', (chunk) => {
            data += chunk;
        });
        validationRes.on('end', () => {
            if (validationRes.statusCode === 200 && data === 'O Token é valido!') {
                next();
            } else {
                res.status(401).send({ message: 'Token inválido!' });
            }
        });
    });

    validationReq.on('error', (err) => {
        res.status(500).send({ message: 'Validation token error', error: err.message });
    });

    validationReq.end();
};



// teste validacao token
app.get('/auth/validate', (req, res, next) => {
    authServiceProxy(req, res, next);
});

// R01 - Autocadastro
app.post('/clientes', (req, res, next) => {
    clienteServiceProxy(req, res, next);
});



// R11 - tela inicial funcionario
app.get('/voos', validateTokenProxy, (req, res, next) => {
    console.log('Query params recebidos:', req.query);
    voosProxy(req, res, next);
});
//R15-Cadastra voo
app.post('/voos', validateTokenProxy, (req, res, next) => {
    voosProxy(req, res, next);
});


// ********************************* API COMPOSITION ************************************************

// R02 - login
app.post('/auth/login', async (req, res) => {
    try {
        const authRes = await axios.post('http://localhost:8080/auth/login', req.body);

        const loginData = authRes.data;
        const userLogin = req.body.login; 
        const userType = loginData.tipo;
        let usuarioRes = null;
    

        if(userType === 'CLIENTE') {
            usuarioRes = await axios.get(`http://localhost:8082/clientes/${userLogin}`, {
                headers: { 'x-access-token': loginData.access_token }
            });
        } else {
            usuarioRes = await axios.get(`http://localhost:8083/funcionarios/${userLogin}`, {
                headers: { 'x-access-token': loginData.access_token }
            });
        }
     

        const usuarioData = usuarioRes.data;

        const responseComposta = {
            ...loginData,
            usuario: usuarioData
        };


        return res.status(200).json(responseComposta);

    } catch (err) {
        if (err.response && err.response.status === 401) {
            return res.status(401).json({ message: 'Login inválido' });
        }
        console.error('Erro no login via gateway:', err.message);
        return res.status(500).json({ message: 'Erro no login', error: err.message });
    }
});



// *********************************************************************************
var server = http.createServer(app);
server.listen(3000);
console.log('Server running on port 3000');