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
    allowedHeaders: ['Content-Type', 'x-access-token', 'Authorization'],
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS', 'PATCH']
};

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(logger('dev'));
app.use(helmet());
app.use(cors(corsOptions));

//app.options('*', cors(corsOptions));


// PROXIES - IMPORTANTE: NÃO RETIRAR OS proccess.env!... senão não vai funcionar no docker

const novoFunc = httpProxy(process.env.SAGA_SERVICE_URL || 'http://localhost:8087', {
    proxyReqPathResolver: (req) => {
        return '/saga/novo-funcionario';
    },
    preserveHostHdr: true
});

const removeFuncProxy = httpProxy(process.env.SAGA_SERVICE_URL || 'http://localhost:8087', {
    proxyReqPathResolver: (req) => {
        // Pega o código do funcionário dos parâmetros da rota
        const codigoFuncionario = req.params.codigoFuncionario;
        return `/saga/remover/${codigoFuncionario}`;
    },
    preserveHostHdr: true
});

const authServiceProxy = httpProxy(process.env.AUTH_SERVICE_URL || 'http://localhost:8080');

const voosProxy = httpProxy(process.env.VOOS_SERVICE_URL || 'http://localhost:8081');

const clienteServiceProxy = httpProxy(process.env.CLIENTE_SERVICE_URL || 'http://localhost:8082');

const funcionariosServiceProxy = httpProxy(process.env.FUNCIONARIOS_SERVICE_URL || 'http://localhost:8083', {
    proxyReqPathResolver: (req) => {
        return '/funcionario/funcionarios';
    },
    preserveHostHdr: true
});

//const funcionariosServiceProxy = httpProxy(process.env.FUNCIONARIOS_SERVICE_URL || 'http://localhost:8083');

const reservasServiceProxy = httpProxy(process.env.RESERVAS_SERVICE_URL || 'http://localhost:8084');

const validateTokenProxy = (req, res, next) => {

    let token = req.headers['x-access-token'];

    if (!token && req.headers['authorization']) {
        const authHeader = req.headers['authorization'];
        if (authHeader.startsWith('Bearer ')) {
            token = authHeader.substring(7);
        }
    }

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

    const urlAuthService = process.env.AUTH_SERVICE_URL || 'http://localhost:8080'
    const validationReq = http.request(urlAuthService + '/validate', validationReqOptions, (validationRes) => {
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



// R17 - Inserção de funcionário
app.post('/funcionarios', validateTokenProxy, (req, res, next) => {
    novoFunc(req, res, next);
});

// R16 - Lista de Funcionarios
app.get('/funcionarios', validateTokenProxy, (req, res, next) => {
    funcionariosServiceProxy(req, res, next);
});

// R19 - Remoção lógica do funcionário
app.delete('/funcionarios/:codigoFuncionario', validateTokenProxy, (req, res, next) => {
    removeFuncProxy(req, res, next);
});

// teste validacao token
app.get('/validate', (req, res, next) => {
    authServiceProxy(req, res, next);
});

// R01 - Autocadastro
app.post('/clientes', (req, res, next) => {
    clienteServiceProxy(req, res, next);
});

// para o teste de buscar todos os clientes
app.get('/clientes', (req, res, next) => {
    clienteServiceProxy(req, res, next);
});

// Para o teste de token incorreto e sem logar
app.get('/clientes/:codigoCliente', validateTokenProxy, (req, res, next) => {
    clienteServiceProxy(req, res, next);
});

// comprar milhas
app.put('/clientes/:codigoCliente/milhas', validateTokenProxy, (req, res, next) => {
    clienteServiceProxy(req, res, next);
})

// saldo milhas com as transações de milhas
app.get('/clientes/:codigoCliente/milhas', validateTokenProxy, (req, res, next) => {
    clienteServiceProxy(req, res, next);
})

// R02 - logout
app.post('/logout', (req, res, next) => {
    authServiceProxy(req, res, next);
});

// R11 - tela inicial funcionario
app.get('/voos', validateTokenProxy, (req, res, next) => {
    voosProxy(req, res, next);
});

// Buscar Voo
app.get('/voos/:codigoVoo', validateTokenProxy, (req, res, next) => {
    voosProxy(req, res, next);
});

//R15-Cadastra voo
app.post('/voos', validateTokenProxy, (req, res, next) => {
    voosProxy(req, res, next);
});

// R16 - Listar Funcionarios
app.get('/funcionarios', validateTokenProxy, (req, res, next) => {
    funcionariosServiceProxy(req, res, next);
});
//R14- Realizar Voo
app.patch('/voos/:codigoVoo/estado', validateTokenProxy, (req, res, next) => {
    voosProxy(req, res, next);
});

//R--- Busca aeroporto para cadastrar Voo
app.get('/aeroportos', validateTokenProxy, (req, res, next) => {
    console.log('Gateway: Recebida requisição GET /aeroportos. Encaminhando para voosProxy.');
    voosProxy(req, res, next);
});


// ********************************* API COMPOSITION ************************************************

// R02 - login
app.post('/login', async (req, res) => {
    try {
        const urlAuthService = process.env.AUTH_SERVICE_URL || 'http://localhost:8080'
        const authRes = await axios.post(`${urlAuthService}/login`, req.body);

        const loginData = authRes.data;
        const userLogin = req.body.login;
        const userType = loginData.tipo;
        let usuarioRes = null;


        if (userType === 'CLIENTE') {
            const urlClienteService = process.env.CLIENTE_SERVICE_URL || 'http://localhost:8082'
            usuarioRes = await axios.get(`${urlClienteService}/clientes/login/${userLogin}`, {
                headers: { 'x-access-token': loginData.access_token }
            });
        } else if (userType === 'FUNCIONARIO') {
            const urlFuncionarioService = process.env.FUNCIONARIOS_SERVICE_URL || 'http://localhost:8083'
            usuarioRes = await axios.get(`${urlFuncionarioService}/funcionario/${userLogin}`, {
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

// R03 - Tela Inicial do Cliente
app.get('/clientes/home', validateTokenProxy, async (req, res) => {
    try {
        const clienteId = req.query.clienteId; // ID do cliente enviado como query param
        if (!clienteId) {
            return res.status(400).send({ message: 'Cliente ID não fornecido!' });
        }

        // Obtem saldo de milhas
        const saldoMilhasResponse = await axios.get(`${process.env.CLIENTE_SERVICE_URL || 'http://localhost:8082'}/clientes/${clienteId}/saldo-milhas`, {
            headers: { 'x-access-token': req.headers['x-access-token'] }
        });

        // Lista reservas
        const reservasResponse = await axios.get(`${process.env.RESERVAS_SERVICE_URL || 'http://localhost:8084'}/reservas?clienteId=${clienteId}`, {
            headers: { 'x-access-token': req.headers['x-access-token'] }
        });

        // Lista voos feitos e cancelados
        const voosResponse = await axios.get(`${process.env.VOOS_SERVICE_URL || 'http://localhost:8081'}/voos?clienteId=${clienteId}`, {
            headers: { 'x-access-token': req.headers['x-access-token'] }
        });

        const responseComposta = {
            saldoMilhas: saldoMilhasResponse.data,
            reservas: reservasResponse.data,
            voos: voosResponse.data
        };

        return res.status(200).json(responseComposta);
    } catch (err) {
        console.error('Erro ao obter dados da tela inicial do cliente:', err.message);
        return res.status(500).json({ message: 'Erro ao obter dados da tela inicial do cliente', error: err.message });
    }
});

// R04 - Detalhes da Reserva
app.get('/reservas/:codigoReserva', validateTokenProxy, async (req, res) => {
    try {
        const codigoReserva = req.params.codigoReserva; // Código da reserva enviado como parâmetro de rota
        if (!codigoReserva) {
            return res.status(400).send({ message: 'Código da reserva não fornecido!' });
        }

        const reservaResponse = await axios.get(`${process.env.RESERVAS_SERVICE_URL || 'http://localhost:8084'}/reservas/${codigoReserva}`, {
            headers: { 'x-access-token': req.headers['x-access-token'] }
        });

        return res.status(200).json(reservaResponse.data);
    } catch (err) {
        console.error('Erro ao obter detalhes da reserva:', err.message);
        return res.status(500).json({ message: 'Erro ao obter detalhes da reserva', error: err.message });
    }
});

// R05 - Comprar Milhas
app.post('/clientes/comprar-milhas', validateTokenProxy, async (req, res) => {
    try {
        const { clienteId, valorEmReais } = req.body;

        if (!clienteId || !valorEmReais || valorEmReais <= 0) {
            return res.status(400).send({ message: 'Cliente ID e valor em reais são obrigatórios e devem ser válidos!' });
        }

        // Calcula a quantidade de milhas compradas
        const milhasCompradas = Math.floor(valorEmReais / 5);

        // Registra a transação no serviço de clientes
        const transacao = {
            dataHora: new Date().toISOString(),
            valorEmReais,
            milhas: milhasCompradas,
            descricao: 'COMPRA DE MILHAS',
        };

        const response = await axios.post(
            `${process.env.CLIENTE_SERVICE_URL || 'http://localhost:8082'}/clientes/${clienteId}/comprar-milhas`,
            transacao,
            {
                headers: { 'x-access-token': req.headers['x-access-token'] },
            }
        );

        return res.status(200).json(response.data);
    } catch (err) {
        console.error('Erro ao comprar milhas:', err.message);
        return res.status(500).json({ message: 'Erro ao comprar milhas', error: err.message });
    }
});
// RF06 - Extrato milhas 
app.get('/TransacaoMilhas/:email/extract', validateTokenProxy, (req, res, next) => {
    clienteServiceProxy(req, res, next);
});
// *********************************************************************************
var server = http.createServer(app);
server.listen(3000);
console.log('Server running on port 3000');