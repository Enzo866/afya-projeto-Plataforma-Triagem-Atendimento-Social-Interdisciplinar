const http = require('node:http');
const servicos = [
  {id:'1',nome:'Psicologia',descricao:'Conheça o serviço de Psicologia.'},
  {id:'2',nome:'Fisioterapia',descricao:'Conheça o serviço de Fisioterapia.'},
  {id:'3',nome:'Farmácia',descricao:'Conheça o serviço de Farmácia.'},
  {id:'4',nome:'Direito',descricao:'Conheça o serviço de Direito.'}
];
let falhar = false;
const server = http.createServer((req, res) => {
  const caminho = new URL(req.url, 'http://127.0.0.1:8080').pathname;
  res.setHeader('Content-Type', 'application/json; charset=utf-8');
  if (caminho === '/falhar') falhar = true;
  if (caminho === '/normal') falhar = false;
  if (caminho === '/especialidades') {
    setTimeout(() => {
      res.writeHead(falhar ? 503 : 200);
      res.end(JSON.stringify(falhar ? {erro:'Falha simulada'} : servicos));
    }, 1000);
  } else if (['/falhar','/normal'].includes(caminho)) {
    res.end(JSON.stringify({falhaSimulada:falhar}));
  } else {
    res.writeHead(404);
    res.end(JSON.stringify({erro:'Rota não encontrada'}));
  }
});
server.on('error', err => {
  console.error('Não foi possível iniciar:', err.message);
  process.exitCode = 1;
});
server.listen(8080, '127.0.0.1', () => {
  console.log('Mock: http://127.0.0.1:8080/especialidades');
  console.log('Falha: /falhar | Recuperar: /normal | Parar: Ctrl+C');
});
